package com.confinement.filter.listeners;

import org.bukkit.*;
import com.confinement.filter.commands.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class StoneListener implements Listener
{
    @EventHandler
    public void onPlayerPickup(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem().getItemStack();
        final Material itemType = item.getType();
        if (itemType == Material.STONE && StoneCommand.cantPickupStone.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onLeave(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        StoneCommand.cantPickupStone.remove(player.getUniqueId());
    }
}
