package net.libhalt.dev.plugin.armor.listener;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.libhalt.bukkit.kaede.event.ScoreboardTextAboutToUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.dev.plugin.armor.ArmorPlugin;
import net.libhalt.dev.plugin.armor.utils.Armor;
import net.libhalt.dev.plugin.armor.utils.ItemStackUtils;
import net.syuu.common.event.ScoreboardPreRenderEvent;
import net.syuu.common.utils.MilliToSecondFormatter;
import net.syuu.popura.PopuraPlugin;

public class ArcherListener implements Listener
{
    private ArmorPlugin plugin;
    private Map<Player, Long> archerTagged;
    private FixedMetadataValue fixedMetadataValue;
    
    public ArcherListener(final ArmorPlugin plugin) {
        this.archerTagged =Maps.newHashMap();
        this.plugin = plugin;
        this.fixedMetadataValue = new FixedMetadataValue(plugin.getPlugin(), (Object)Boolean.TRUE);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void getArrow(final EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player)event.getEntity();
            if (ItemStackUtils.isWearingFull(player, Armor.LEATHER) && this.plugin.getActiveArmor(player) == Armor.LEATHER) {
                if (event.getForce() > 0.7) {
                    if (event.getProjectile() instanceof Arrow) {
                        event.getProjectile().setMetadata("ARROW_FULL_SHOT", (MetadataValue)this.fixedMetadataValue);
                    }
                }
                else {
                    player.sendMessage(this.plugin.getText("ARCHER_NOT_FULL"));
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamagedByEntity(final EntityDamageByEntityEvent event) {

        if(event.isCancelled()){
            return;
        }
        if (event.getDamager() instanceof Arrow) {
            final Arrow arrow = (Arrow)event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                final Player player = (Player)arrow.getShooter();
                if (ItemStackUtils.isWearingFull(player, Armor.LEATHER) && this.plugin.getActiveArmor(player) == Armor.LEATHER) {
                    final Entity damagedEntity = event.getEntity();
                    if (damagedEntity instanceof Player) {
                        final Player damaged = (Player)damagedEntity;
                        final boolean strongshot = arrow.hasMetadata("ARROW_FULL_SHOT");
                        final boolean tagged = this.archerTagged.containsKey(damaged);
                        final double damage;
                        if (!tagged) {
                            if (strongshot) {
                                damage = 2.0;
                            }
                            else {
                                damage = 1.0;
                            }
                        }
                        else if (strongshot) {
                            damage = 3.0;
                        }
                        else {
                            damage = 2.0;
                        }
                        final double distance = flatDistance(player.getLocation(), arrow.getLocation());
                        player.sendMessage(ChatColor.BLUE + "[Archer w/ Range (" + (int)distance + ")]:" + ChatColor.YELLOW + " Dealt " + damage / 2.0 + " Damage");
                        event.setDamage(0.0);
                        new BukkitRunnable() {
                            public void run() {
                                if(event.isCancelled()){
                                    return;
                                }
                                Damageable damagedvar = (Damageable) damaged;
                                damaged.setHealth(Math.max(0.0, damagedvar.getHealth() - damage));
                            }
                        }.runTask(this.plugin.getPlugin());
                        if (strongshot && !tagged) {
                            if (ItemStackUtils.isWearingFull(damaged, Armor.LEATHER) && this.plugin.getActiveArmor(damaged) == Armor.LEATHER) {
                                return;
                            }
                            this.archerTagged.put(damaged, System.currentTimeMillis() + 10000L);
                            this.updateTag();
                            new BukkitRunnable() {
                                public void run() {
                                    ArcherListener.this.archerTagged.remove(damaged);
                                    PopuraPlugin.getInstance().getPopura().getNameTagManager().updatePlayerForOnlinePlayers(damaged);
                                }
                            }.runTaskLater(HCFactionPlugin.getInstance(), 200L);
                        }
                    }
                }
            }
        }
        if (this.archerTagged.containsKey(event.getEntity())) {
            event.setDamage(event.getDamage() * 1.2);
        }
    }
    
    public static double flatDistance(final Location one, final Location other) {
        return Math.sqrt((one.getX() - other.getX()) * (one.getX() - other.getX()) + (one.getZ() - other.getZ()) * (one.getZ() - other.getZ()));
    }
    
    @EventHandler
    public void onScoreboardEntryUpdate(final ScoreboardTextAboutToUpdateEvent event) {
        final Long value = this.archerTagged.get(event.getPlayer());
        if(event.getText() != null){
            if (value != null) {
                event.setText(event.getText().replace("%archer_tag%" ,  MilliToSecondFormatter.format(value - System.currentTimeMillis())));
            }else if(event.getText().contains("%archer_tag%")){
                event.setText(null);
            }
        }
    }
    
    public void updateTag() {
        final List<String> taagged = (List<String>)Lists.newArrayList((Iterable)Collections2.transform((Collection)this.archerTagged.keySet(), (Function)new Function<Player, String>() {
            public String apply(@Nullable final Player player) {
                return player.getName();
            }
        }));
        for (final Player other : Bukkit.getOnlinePlayers()) {
            PopuraPlugin.getInstance().getPopura().getNameTagManager().removeAndCreateClientSide(other, "archer-tagged", ChatColor.DARK_PURPLE.toString());
            PopuraPlugin.getInstance().getPopura().getNameTagManager().addPlayerClientSideBulk(other, (List)taagged, "archer-tagged");
        }
    }
}
