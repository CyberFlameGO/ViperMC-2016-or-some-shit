package net.libhalt.dev.plugin.armor.task;

import org.bukkit.scheduler.*;
import net.libhalt.dev.plugin.armor.*;
import org.bukkit.entity.*;
import net.libhalt.dev.plugin.armor.utils.*;
import net.syuu.popura.*;
import net.syuu.popura.claim.*;
import net.syuu.popura.faction.*;
import org.bukkit.plugin.*;
import java.util.*;
import net.libhalt.dev.plugin.armor.kit.*;
import org.bukkit.*;
import net.syuu.popura.faction.bean.*;

public class TaskClassChecker extends BukkitRunnable
{
    private ArmorPlugin plugin;
    private WeakHashMap<Player, Armor> pendingKit;
    
    public TaskClassChecker(final ArmorPlugin plugin) {
        this.pendingKit = new WeakHashMap<Player, Armor>();
        this.plugin = plugin;
    }
    
    public void run() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            try {
                if (ItemStackUtils.isWearingFull(player, Armor.GOLD) || ItemStackUtils.isWearingFull(player, Armor.LEATHER) || ItemStackUtils.isWearingFull(player, Armor.IRON) || ItemStackUtils.isWearingFull(player, Armor.CHAIN_MAIL)) {
                    final Armor armor = ItemStackUtils.getArmor(player.getInventory().getArmorContents()[0].getType());
                    final AbstractClass kit = this.plugin.getHandler(armor);
                    if(!kit.isEnabled()){
                        continue;
                    }
                    if (this.plugin.hasActiveKit(player)) {
                        if (armor == this.plugin.getActiveArmor(player)) {

                            kit.applyPassive(player);
                            final Location location = player.getLocation();
                            final ClaimedRegion claimedRegion = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(location.getWorld().getName(), location.getBlockX(), location.getBlockZ()));
                            if (claimedRegion != null && claimedRegion.getOwner().getFactionType() == FactionType.SAFEZONE) {
                                continue;
                            }
                            kit.applyHeldEffect(player, player.getItemInHand());
                        }
                        else {
                            this.plugin.setActive(player, null);
                            player.sendMessage(this.plugin.getText("CLASS_REMOVED").replace("{class}", armor.toKit()));
                        }
                    }
                    else {
                        if (this.pendingKit.containsKey(player)) {
                            continue;
                        }
                        this.pendingKit.put(player, armor);
                        player.sendMessage(this.plugin.getText("CLASS_WARMUP").replace("{class}", armor.toKit()));
                        new BukkitRunnable() {
                            public void run() {
                                if (TaskClassChecker.this.pendingKit.containsKey(player)) {
                                    final Armor armor = TaskClassChecker.this.pendingKit.get(player);
                                    if (ItemStackUtils.isWearingFull(player, armor)) {
                                        TaskClassChecker.this.plugin.setActive(player, armor);
                                        player.sendMessage(TaskClassChecker.this.plugin.getText("CLASS_ACTIVE").replace("{class}", armor.toKit()));
                                    }
                                    else {
                                        player.sendMessage(TaskClassChecker.this.plugin.getText("CLASS_REMOVED").replace("{class}", armor.toKit()));
                                    }
                                    TaskClassChecker.this.pendingKit.remove(player);
                                }
                            }
                        }.runTaskLater(this.plugin.getPlugin(), (long)(this.plugin.getClassCooldown() * 20));
                    }
                }
                else {
                    if (!this.plugin.hasActiveKit(player)) {
                        continue;
                    }
                    final Armor armor = this.plugin.getActiveArmor(player);
                    this.plugin.setActive(player, null);
                    player.sendMessage(this.plugin.getText("CLASS_REMOVED").replace("{class}", armor.toKit()));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
