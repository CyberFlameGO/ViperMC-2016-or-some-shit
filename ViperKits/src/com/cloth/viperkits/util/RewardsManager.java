package com.cloth.viperkits.util;

import com.cloth.viperkits.config.ConfigurationSetup;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sun.security.krb5.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brennan on 6/22/2017.
 */
public class RewardsManager
{
    public static ArrayList<ItemStack> getItems(String args){
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        if(Util.kitExists(args)){
            String kit = Util.getKit(args);
            try{
                if(!ConfigurationSetup.kits.getConfigurationSection(kit + ".Items").getKeys(false).isEmpty()){
                    for(String x : ConfigurationSetup.kits.getConfigurationSection(kit + ".Items").getKeys(false)){

                        ItemStack item = null;

                        if(x.startsWith("SPEED") || x.startsWith("INSTANT_HEAL") || x.startsWith("FIRE_RESISTANCE") || x.startsWith("INVISIBILITY"))
                        {
                            int level = ConfigurationSetup.kits.getInt(kit + ".Items." + x + ".Amplifier");
                            int amount = ConfigurationSetup.kits.getInt(kit + ".Items." + x + ".Amount");

                            if(x.startsWith("INSTANT_HEAL"))
                            {
                                if(level == 1){
                                    item = new ItemStack(Material.POTION, 1, (byte) 16453);
                                    Potion potion = Potion.fromItemStack(item);
                                    potion.setSplash(true);
                                    potion.apply(item);
                                }

                                if(level == 2){
                                    item = new ItemStack(Material.POTION, 1, (byte) 16421);
                                    Potion potion = Potion.fromItemStack(item);
                                    potion.setSplash(true);
                                    potion.apply(item);
                                }
                            }

                            if(x.startsWith("SPEED"))
                            {
                                if(level == 1){
                                    item = new ItemStack(Material.POTION, 1, (byte) 8194);
                                }

                                if(level == 2){
                                    item = new ItemStack(Material.POTION, 1, (byte) 8226);
                                }
                            }

                            if(x.startsWith("FIRE_RESISTANCE"))
                            {
                                item = new ItemStack(Material.POTION, 1, (byte) 8259);
                            }

                            if(x.startsWith("INVISIBILITY"))
                            {
                                item = new ItemStack(Material.POTION, 1, (byte)8238);
                                PotionMeta meta = (PotionMeta) item.getItemMeta();
                                meta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 32400, 1), true);
                                item.setItemMeta(meta);
                            }

                        } else {
                            item = new ItemStack(Material.getMaterial(x), ConfigurationSetup.kits.getInt(kit + ".Items." + x + ".Amount"));
                        }

                        ItemMeta meta = item.getItemMeta();
                        try{

                            if(ConfigurationSetup.kits.contains(kit + ".Items." + x + ".Name"))
                            {
                                meta.setDisplayName(ConfigurationSetup.kits.getString(kit + ".Items." + x + ".Name").replaceAll("&", "§"));
                            }

                            if(ConfigurationSetup.kits.contains(kit + ".Items." + x + ".Lore"))
                            {
                                ArrayList<String> no_replacements = (ArrayList<String>) ConfigurationSetup.kits.getList(kit + ".Items." + x + ".Lore");
                                ArrayList<String> replacements = new ArrayList<String>();

                                for(String lore : no_replacements){
                                    replacements.add(lore.replaceAll("&", "§"));
                                }

                                meta.setLore(replacements);
                            }

                            if(ConfigurationSetup.kits.contains(kit + ".Items." + x + ".Enchantments"))
                            {
                                for(String enchantments : ConfigurationSetup.kits.getConfigurationSection(kit + ".Items." + x + ".Enchantments").getKeys(false))
                                {
                                    Enchantment enchantment = Enchantment.getByName(enchantments);
                                    int level = ConfigurationSetup.kits.getInt(kit + ".Items." + x + ".Enchantments." + enchantments + ".Level");
                                    meta.addEnchant(enchantment, level, true);
                                }
                            }

                        } catch(Exception error){ error.printStackTrace(); }

                        item.setItemMeta(meta);
                        items.add(item);
                    }
                }
            } catch(Exception error){}
        }
        return items;
    }

    public static ArrayList<ItemStack> getArmor(String args){
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        if(Util.kitExists(args)){
            String kit = Util.getKit(args);
            try{
                if(!ConfigurationSetup.kits.getConfigurationSection(kit + ".Armor").getKeys(false).isEmpty()){
                    for(String x : ConfigurationSetup.kits.getConfigurationSection(kit + ".Armor").getKeys(false)){
                        ItemStack item = new ItemStack(Material.getMaterial(x), ConfigurationSetup.kits.getInt(kit + ".Armor." + x + ".Amount"));
                        ItemMeta meta = item.getItemMeta();
                        try{

                            if(ConfigurationSetup.kits.contains(kit + ".Armor." + x + ".Name"))
                            {
                                meta.setDisplayName(ConfigurationSetup.kits.getString(kit + ".Armor." + x + ".Name").replaceAll("&", "§"));
                            }

                            if(ConfigurationSetup.kits.contains(kit + ".Armor." + x + ".Lore"))
                            {
                                ArrayList<String> no_replacements = (ArrayList<String>) ConfigurationSetup.kits.getList(kit + ".Armor." + x + ".Lore");
                                ArrayList<String> replacements = new ArrayList<String>();
                                for(String lore : no_replacements){
                                    replacements.add(lore.replaceAll("&", "§"));
                                }
                                meta.setLore(replacements);
                            }

                            if(ConfigurationSetup.kits.contains(kit + ".Armor." + x + ".Enchantments"))
                            {
                                for(String enchantments : ConfigurationSetup.kits.getConfigurationSection(kit + ".Armor." + x + ".Enchantments").getKeys(false))
                                {
                                    Enchantment enchantment = Enchantment.getByName(enchantments);
                                    int level = ConfigurationSetup.kits.getInt(kit + ".Armor." + x + ".Enchantments." + enchantments + ".Level");
                                    meta.addEnchant(enchantment, level, true);
                                }
                            }

                        } catch(Exception error){}

                        item.setItemMeta(meta);
                        items.add(item);
                    }
                }
            } catch(NullPointerException error) {}
        }
        return items;
    }
}
