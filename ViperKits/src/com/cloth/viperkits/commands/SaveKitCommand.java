package com.cloth.viperkits.commands;

import com.cloth.viperkits.config.ConfigurationSetup;
import com.cloth.viperkits.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import sun.security.krb5.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Brennan on 6/22/2017.
 */
public class SaveKitCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Only players can use that command.");
            return true;
        }

        Player p = (Player) sender;

        if(!p.hasPermission("viperkits.save")){
            String msg = ConfigurationSetup.messages.getString("no_permission").replaceAll("&", "§");
            p.sendMessage(msg);
            return true;
        }

        if(args.length != 1){
            String msg = ConfigurationSetup.messages.getString("invalid_usage").replaceAll("&", "§");
            p.sendMessage(msg.replace("%usage%", "§a/savekit <kit>"));
            return true;
        }

        if(!Util.kitExists(args[0])){
            String msg = ConfigurationSetup.messages.getString("invalid_kit").replaceAll("&", "§");
            p.sendMessage(msg);
            return true;
        }

        String kit = Util.getKit(args[0]);
        String msg = ConfigurationSetup.messages.getString("kit_saved").replaceAll("&", "§");

        for(ItemStack item : p.getInventory().getArmorContents()){
            if(item != null){
                if(item.getType() != Material.AIR){

                    ConfigurationSetup.kits.set(kit + ".Armor." + item.getType().toString() + ".Amount", item.getAmount());

                    if(item.hasItemMeta()){
                        if(item.getItemMeta().hasDisplayName()){
                            ConfigurationSetup.kits.set(kit + ".Armor." + item.getType().toString() + ".Name", item.getItemMeta().getDisplayName().replaceAll("§", "&"));
                        }
                        if(item.getItemMeta().hasLore()){
                            List<String> no_replacements = item.getItemMeta().getLore();
                            List<String> replacements = new ArrayList<String>();
                            for(String x : no_replacements){
                                replacements.add(x.replaceAll("§", "&"));
                            }
                            ConfigurationSetup.kits.set(kit + ".Armor." + item.getType().toString() + ".Lore", replacements);
                        }
                        if(item.getItemMeta().hasEnchants()){
                            for(Enchantment e : item.getItemMeta().getEnchants().keySet())
                            {
                                ConfigurationSetup.kits.set(kit + ".Armor." + item.getType().toString() + ".Enchantments." + e.getName() + ".Level", item.getItemMeta().getEnchantLevel(e));
                            }
                        }
                    }
                }
            }
        }

        for(ItemStack item : p.getInventory().getContents()){
            if(item != null){
                if(item.getType() != Material.AIR){
                    if(item.getType() == Material.POTION)
                    {
                        Potion potion = Potion.fromItemStack(item);

                        int i = 1;

                        while(ConfigurationSetup.kits.contains(kit + ".Items." + potion.getType().toString() + i + ".Amplifier"))
                        {
                            i++;
                        }

                        ConfigurationSetup.kits.set(kit + ".Items." + potion.getType().toString() + i + ".Amplifier", potion.getLevel());
                    } else {
                        ConfigurationSetup.kits.set(kit + ".Items." + item.getType().toString() + ".Amount", item.getAmount());

                        if(item.hasItemMeta()){
                            if(item.getItemMeta().hasDisplayName()){
                                ConfigurationSetup.kits.set(kit + ".Items." + item.getType().toString() + ".Name", item.getItemMeta().getDisplayName().replaceAll("§", "&"));
                            }
                            if(item.getItemMeta().hasLore()){
                                List<String> no_replacements = item.getItemMeta().getLore();
                                List<String> replacements = new ArrayList<String>();
                                for(String x : no_replacements){
                                    replacements.add(x.replaceAll("§", "&"));
                                }
                                ConfigurationSetup.kits.set(kit + ".Items." + item.getType().toString() + ".Lore", replacements);
                            }
                            if(item.getItemMeta().hasEnchants()){
                                for(Enchantment e : item.getItemMeta().getEnchants().keySet())
                                {
                                    ConfigurationSetup.kits.set(kit + ".Items." + item.getType().toString() + ".Enchantments." + e.getName() + ".Level", item.getItemMeta().getEnchantLevel(e));
                                }
                            }
                        }
                    }
                }
            }
        }

        ConfigurationSetup.kits.saveConfig();
        p.sendMessage(msg.replaceAll("%kit%", kit));
        return true;
    }
}
