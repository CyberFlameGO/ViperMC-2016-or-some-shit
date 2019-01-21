package com.viperhcf.minssentials.utils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.base.Preconditions;

public class ItemStackUtils {
    public static ItemStack setItemLore(ItemStack item, List<String> lore) {
        Preconditions.checkNotNull(item);
        Preconditions.checkNotNull(lore);
        Preconditions.checkState(item.getType() != Material.AIR);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack setItemTitle(ItemStack item, String title) {
        Preconditions.checkNotNull(item);
        Preconditions.checkNotNull(title);
        Preconditions.checkState(item.getType() != Material.AIR);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(title);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static void updateInventory(final Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.updateInventory();
            }
        }.runTaskLater(JavaPlugin.getProvidingPlugin(ItemStackUtils.class), 1);
    }

}
