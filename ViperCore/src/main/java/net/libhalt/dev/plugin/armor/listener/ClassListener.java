package net.libhalt.dev.plugin.armor.listener;

import net.libhalt.dev.plugin.armor.*;
import net.syuu.popura.*;
import net.syuu.popura.claim.*;
import net.syuu.popura.faction.*;
import net.libhalt.dev.plugin.armor.utils.*;
import net.libhalt.dev.plugin.armor.kit.*;
import org.bukkit.*;
import net.syuu.popura.faction.bean.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.inventory.*;

public class ClassListener implements Listener
{
    private ArmorPlugin plugin;
    
    public ClassListener(final ArmorPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerItemHeld(final PlayerItemHeldEvent event) {
        final Armor armor = this.plugin.getActiveArmor(event.getPlayer());
        if (armor != null) {
            final AbstractClass kit = this.plugin.getHandler(armor);
            if (kit != null) {
                final Location location = event.getPlayer().getLocation();
                final ClaimedRegion claimedRegion = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(location.getWorld().getName(), location.getBlockX(), location.getBlockZ()));
                if (claimedRegion != null && claimedRegion.getOwner().getFactionType() == FactionType.SAFEZONE) {
                    return;
                }
                kit.applyHeldEffect(event.getPlayer(), event.getPlayer().getItemInHand());
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final Armor armor = this.plugin.getActiveArmor(event.getPlayer());
            if (armor != null) {
                final AbstractClass kit = this.plugin.getHandler(armor);
                if (kit != null) {
                    final Location location = event.getPlayer().getLocation();
                    final ClaimedRegion claimedRegion = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(location.getWorld().getName(), location.getBlockX(), location.getBlockZ()));
                    if (claimedRegion != null && claimedRegion.getOwner().getFactionType() == FactionType.SAFEZONE) {
                        return;
                    }
                    final ItemStack item = event.getPlayer().getItemInHand();
                    if (kit.applyClickEffect(event.getPlayer(), item)) {
                        if (item.getAmount() > 1) {
                            item.setAmount(item.getAmount() - 1);
                        }
                        else {
                            event.getPlayer().setItemInHand((ItemStack)null);
                        }
                    }
                }
            }
        }
    }
}
