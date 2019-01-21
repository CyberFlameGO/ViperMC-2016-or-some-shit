package com.cloth.viperkits.util;

import com.cloth.viperkits.config.ConfigurationSetup;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.security.auth.login.Configuration;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Brennan on 6/22/2017.
 */
public class InventoryManager
{
    public static void setupInventory(Player p)
    {
       Inventory inv = Bukkit.createInventory(null, ConfigurationSetup.gui.getInt("Size"), ConfigurationSetup.gui.getString("Title").replaceAll("&", "§"));
       boolean border = ConfigurationSetup.gui.getBoolean("Border");

       for(String x : ConfigurationSetup.gui.getConfigurationSection("Item").getKeys(false))
       {
            String displayname = ConfigurationSetup.gui.getString("Item." + x + ".Name").replaceAll("&", "§");
            int slot = ConfigurationSetup.gui.getInt("Item." + x + ".Slot");
            String typestring = ConfigurationSetup.gui.getString("Item." + x + ".Type");
            ArrayList<String> lore_no_replacements = (ArrayList<String>) ConfigurationSetup.gui.getList("Item." + x + ".Lore");
            ArrayList<String> lore_replacements = new ArrayList<String>();

            double cooldown = ConfigurationSetup.kits.getDouble(x + ".Cooldown");
            double available = ConfigurationSetup.playerdata.getDouble(p.getName().toLowerCase() + "." + x.toLowerCase() + "_cooldown");

            String cooldownstring = Util.cooldownToString(cooldown);
            String availablestring = Util.cooldownToString(available);

            for(String lore : lore_no_replacements){
                String tempString = lore;
                tempString = tempString.replace("&", "§");
                tempString = tempString.replaceAll("%cooldown%", cooldownstring);
                tempString = tempString.replaceAll("%available%", availablestring);
                lore_replacements.add(tempString);
            }

            ItemStack item;
            ItemMeta itemMeta;

            if(isGlass(typestring)){
                item = getGlass(typestring);
                itemMeta = item.getItemMeta();
            } else {
                item = new ItemStack(Material.getMaterial(typestring));
                itemMeta = item.getItemMeta();
            }

            itemMeta.setDisplayName(displayname);
            itemMeta.setLore(lore_replacements);
            item.setItemMeta(itemMeta);
            inv.setItem(slot, item);
       }

        if(border){
           addborder(inv);
        }

       p.openInventory(inv);
       return;
    }

    private static void addborder(Inventory inv){
        ItemStack borderglass = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData());
        for(int i = 0; i < inv.getSize(); i++){
            if(inv.getItem(i) == null){
                inv.setItem(i, borderglass);
            } else {
                if(inv.getItem(i).getType() == Material.AIR){
                    inv.setItem(i, borderglass);
                }
            }
        }
    }

    private static ItemStack getGlass(String type){

        if(type.equalsIgnoreCase("BLUE_GLASS"))
        {
            return new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLUE.getData());
        }

        if(type.equalsIgnoreCase("RED_GLASS"))
        {
            return new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
        }

        if(type.equalsIgnoreCase("PINK_GLASS"))
        {
            return new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.PINK.getData());
        }

        if(type.equalsIgnoreCase("GREEN_GLASS"))
        {
            return new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GREEN.getData());
        }

        if(type.equalsIgnoreCase("PURPLE_GLASS"))
        {
            return new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.PURPLE.getData());
        }

        if(type.equalsIgnoreCase("YELLOW_GLASS"))
        {
            return new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.YELLOW.getData());
        }

        if(type.equalsIgnoreCase("WHITE_GLASS"))
        {
            return new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.WHITE.getData());
        }

        return null;
    }

    private static boolean isGlass(String type){
        if(type.equalsIgnoreCase("BLUE_GLASS")
                || type.equalsIgnoreCase("RED_GLASS")
                || type.equalsIgnoreCase("PINK_GLASS")
                || type.equalsIgnoreCase("GREEN_GLASS")
                || type.equalsIgnoreCase("PURPLE_GLASS")
                || type.equalsIgnoreCase("YELLOW_GLASS")
                || type.equalsIgnoreCase("WHITE_GLASS"))
        {
            return true;
        }
        return false;
    }
}
