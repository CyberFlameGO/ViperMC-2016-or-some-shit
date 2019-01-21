package com.viperhcf.minssentials.utils;

import com.viperhcf.minssentials.Minssentials;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Brennan on 6/27/2017.
 */
public class NicknameUtil
{
    public static Inventory setupGUI()
    {
        String title = Minssentials.getInstance().getConfig().getString("nickname-gui-title").replaceAll("&", "§");
        Inventory inventory = Bukkit.createInventory(null, 27, title);
        setItems(inventory);
        setBorder(inventory);
        return inventory;
    }

    public static void setItems(Inventory inv)
    {
        ItemStack dark_green = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GREEN.getData());
        ItemMeta dark_green_meta = dark_green.getItemMeta();
        dark_green_meta.setDisplayName("§2Dark Green");
        dark_green.setItemMeta(dark_green_meta);

        ItemStack light_green = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.LIME.getData());
        ItemMeta light_green_meta = light_green.getItemMeta();
        light_green_meta.setDisplayName("§aLight Green");
        light_green.setItemMeta(light_green_meta);

        ItemStack aqua = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.LIGHT_BLUE.getData());
        ItemMeta aqua_meta = aqua.getItemMeta();
        aqua_meta.setDisplayName("§bAqua");
        aqua.setItemMeta(aqua_meta);

        ItemStack blue = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.CYAN.getData());
        ItemMeta blue_meta = blue.getItemMeta();
        blue_meta.setDisplayName("§9Blue");
        blue.setItemMeta(blue_meta);

        ItemStack purple = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.PURPLE.getData());
        ItemMeta purple_meta = purple.getItemMeta();
        purple_meta.setDisplayName("§5Purple");
        purple.setItemMeta(purple_meta);

        ItemStack pink = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.PINK.getData());
        ItemMeta pink_meta = pink.getItemMeta();
        pink_meta.setDisplayName("§dPink");
        pink.setItemMeta(pink_meta);

        ItemStack yellow = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.YELLOW.getData());
        ItemMeta yellow_meta = yellow.getItemMeta();
        yellow_meta.setDisplayName("§eYellow");
        yellow.setItemMeta(yellow_meta);

        ItemStack orange = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.ORANGE.getData());
        ItemMeta orange_meta = orange.getItemMeta();
        orange_meta.setDisplayName("§6Gold");
        orange.setItemMeta(orange_meta);

        ItemStack red = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
        ItemMeta red_meta = red.getItemMeta();
        red_meta.setDisplayName("§cRed");
        red.setItemMeta(red_meta);

        ItemStack white = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.WHITE.getData());
        ItemMeta white_meta = white.getItemMeta();
        white_meta.setDisplayName("§fWhite");
        white.setItemMeta(white_meta);

        ItemStack gray = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta gray_meta = gray.getItemMeta();
        gray_meta.setDisplayName("§8Dark Grey");
        gray.setItemMeta(gray_meta);

        ItemStack silver = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.SILVER.getData());
        ItemMeta silver_meta = silver.getItemMeta();
        silver_meta.setDisplayName("§7Light Grey");
        silver.setItemMeta(silver_meta);

        inv.setItem(1, dark_green);
        inv.setItem(2, aqua);
        inv.setItem(3, red);
        inv.setItem(4, purple);
        inv.setItem(5, silver);
        inv.setItem(6, gray);
        inv.setItem(7, blue);
        inv.setItem(11, light_green);
        inv.setItem(12, yellow);
        inv.setItem(13, pink);
        inv.setItem(14, orange);
        inv.setItem(15, white);
    }

    public static void setBorder(Inventory inv)
    {
        ItemStack clear = new ItemStack(Material.THIN_GLASS);
        inv.setItem(0, clear);
        inv.setItem(8, clear);
        inv.setItem(9, clear);
        inv.setItem(10, clear);
        inv.setItem(16, clear);
        inv.setItem(17, clear);
        for(int i = 18; i < 27; i++){
            inv.setItem(i, clear);
        }
    }
}
