package com.viperhcf.minssentials.utils;

import com.viperhcf.minssentials.Minssentials;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by Brennan on 6/30/2017.
 */
public class TagUtil
{
    public static void openGUI(Player p)
    {
        int tags = 0;

        if(Minssentials.getInstance().tags.getConfigurationSection("").getKeys(false) != null)
        {
            tags = Minssentials.getInstance().tags.getConfigurationSection("").getKeys(false).size();
        }

        Inventory inventory = Bukkit.createInventory(null, 54, "§aTags §7»§f " + tags);

        setupButtons(inventory, p);
        setupTags(inventory);

        p.openInventory(inventory);
    }

    public static void setupTags(Inventory inventory)
    {
        ArrayList<String> lore = new ArrayList<String>();
        if(Minssentials.getInstance().tags.getConfigurationSection("").getKeys(false) != null)
        {
           for(String x : Minssentials.getInstance().tags.getConfigurationSection("").getKeys(false))
           {
               lore.clear();
               ItemStack tempItem = new ItemStack(Material.NAME_TAG);
               ItemMeta tempMeta = tempItem.getItemMeta();
               tempMeta.setDisplayName("§aTag §f» " + x);
               lore.add(Minssentials.getInstance().tags.getString(x + ".name").replaceAll("&", "§"));
               lore.add("§7Purchase this tag on our store at §estore.vipermc.net §7or win it in-game!");
               tempMeta.setLore(lore);
               tempItem.setItemMeta(tempMeta);
               inventory.addItem(tempItem);
           }
        }
    }

    public static void setupButtons(Inventory inventory, Player p)
    {

        boolean hasTag = false;
        String tag = "";

        if(!Minssentials.getInstance().playerdata.getString(p.getName() + ".tag").equals("None")){
            hasTag = true;
            tag = Minssentials.getInstance().playerdata.getString(p.getName() + ".tag").replaceAll("&", "§");
        }


        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§fExit the tags menu");

        ItemStack door = new ItemStack(Material.IRON_DOOR);
        ItemMeta doorMeta = door.getItemMeta();
        doorMeta.setDisplayName("§aClick to exit");
        doorMeta.setLore(lore);
        door.setItemMeta(doorMeta);

        lore.clear();

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte)3);
        ItemMeta skullMeta = skull.getItemMeta();

        if(hasTag){
            skullMeta.setDisplayName("§aCurrent Tag §f» " + tag);
            lore.add(Minssentials.getInstance().tags.getString(tag + ".name").replaceAll("&", "§"));
            lore.add("Click to remove your current tag");
            skullMeta.setLore(lore);
        } else {
            skullMeta.setDisplayName("§aYou don't have a tag set!");
            lore.add("§f§oClick a tag above to select one!");
            skullMeta.setLore(lore);
        }

        skull.setItemMeta(skullMeta);

        inventory.setItem(50, door);
        inventory.setItem(49, skull);
        inventory.setItem(48, door);
    }
}


