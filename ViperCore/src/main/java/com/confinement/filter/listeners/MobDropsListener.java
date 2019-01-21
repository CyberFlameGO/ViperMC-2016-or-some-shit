package com.confinement.filter.listeners;

import org.bukkit.*;
import com.confinement.filter.commands.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class MobDropsListener implements Listener
{
    @EventHandler
    public void onPlayerPickup(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem().getItemStack();
        final Material itemType = item.getType();
        if ((itemType == Material.BONE || itemType == Material.ROTTEN_FLESH || itemType == Material.BOW ||
                itemType == Material.SPIDER_EYE || itemType == Material.STRING || itemType == Material.ARROW ||
                itemType == Material.POTATO
                /*||itemType == Material.IRON_HELMET || itemType == Material.IRON_HELMET ||
                itemType == Material.IRON_CHESTPLATE || itemType == Material.IRON_LEGGINGS || itemType == Material.IRON_BOOTS
                || itemType == Material.GOLD_HELMET || itemType == Material.GOLD_CHESTPLATE || itemType == Material.GOLD_LEGGINGS ||
                itemType == Material.GOLD_BOOTS || itemType == Material.CHAINMAIL_HELMET || itemType == Material.CHAINMAIL_CHESTPLATE ||
                itemType == Material.CHAINMAIL_LEGGINGS || itemType == Material.CHAINMAIL_BOOTS || itemType == Material.LEATHER_HELMET ||
                itemType == Material.LEATHER_CHESTPLATE || itemType == Material.LEATHER_LEGGINGS || itemType == Material.LEATHER_BOOTS */) && MobDropsCommand.cantPickupMobs.contains(player.getUniqueId().toString())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onLeave(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        MobDropsCommand.cantPickupMobs.remove(player.getUniqueId().toString());
    }
}
