package com.confinement.filter.listeners;

import org.bukkit.*;
import com.confinement.filter.commands.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class CobblestoneListener implements Listener
{
    @EventHandler
    public void onPlayerPickup(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem().getItemStack();
        final Material itemType = item.getType();
        if (itemType == Material.COBBLESTONE && CobblestoneCommand.cantPickup.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onLeave(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        CobblestoneCommand.cantPickup.remove(player.getUniqueId());
    }
}
