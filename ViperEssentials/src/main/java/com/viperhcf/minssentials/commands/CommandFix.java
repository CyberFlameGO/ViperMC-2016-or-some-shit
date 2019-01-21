package com.viperhcf.minssentials.commands;

import com.viperhcf.minssentials.Minssentials;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Brennan on 6/29/2017.
 */
public class CommandFix implements CommandExecutor
{

    public static HashMap<UUID, Long> hand_cooldown = new HashMap<>();
    public static HashMap<UUID, Long> all_cooldown = new HashMap<>();


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {

        if(!(sender instanceof Player))
        {
            sender.sendMessage("§7(§aViper§7) Only players can use that command.");
            return true;
        }

        Player p = (Player) sender;
        if(!p.hasPermission("fix.hand") && !p.hasPermission("fix.all"))
        {
            p.sendMessage("§7(§aViper§7)§f You don't have permission.");
            return true;
        }

        if(args.length != 1)
        {
            p.sendMessage("§7(§aViper§7)§f Correct usage: §a/fix <hand:all>");
            return true;
        }

        String choice = args[0];

        if(!choice.equalsIgnoreCase("hand") && !choice.equalsIgnoreCase("all"))
        {
            p.sendMessage("§7(§aViper§7)§f Correct usage: §a/fix <hand:all>");
            return true;
        }

        if(choice.equalsIgnoreCase("hand"))
        {
            if(!p.hasPermission("fix.hand"))
            {
                p.sendMessage("§7(§aViper§7)§f You don't have permission.");
                return true;
            }

            if(p.getItemInHand() == null)
            {
                p.sendMessage("§7(§aViper§7)§f You don't have an item in your hand.");
                return true;
            }

            if(!canBeRepaired(p.getItemInHand()))
            {
                p.sendMessage("§7(§aViper§7)§f This item cannot be repaired.");
                return true;
            }


            if(hand_cooldown.containsKey(p.getUniqueId()))
            {
                if ((hand_cooldown.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000 <= 0)
                {
                    hand_cooldown.remove(p.getUniqueId());
                } else {
                    long remaining = (hand_cooldown.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000;
                    long toMinutes = remaining/60;

                    if(toMinutes < 0)
                    {
                        toMinutes = 1;
                    }

                    if(toMinutes == 1)
                    {
                        p.sendMessage("§7(§aViper§7)§f You are still on cooldown for §a" + toMinutes + "§f minute.");
                        return true;
                    }

                    if(toMinutes >= 60)
                    {
                        long toHours = toMinutes / 60;

                        if(toMinutes == 60){
                            p.sendMessage("§7(§aViper§7)§f You are still on cooldown for §a" + toHours + "§f hour.");
                            return true;
                        } else {
                            p.sendMessage("§7(§aViper§7)§f You are still on cooldown for §a" + toHours + "§f hours.");
                            return true;
                        }
                    }

                    p.sendMessage("§7(§aViper§7)§f You are still on cooldown for §a" + toMinutes + "§f minutes.");
                    return true;
                }
            }

            if(!hand_cooldown.containsKey(p.getUniqueId()))
            {
                int cooldown = Minssentials.getInstance().getConfig().getInt("fix-hand-cooldown");
                hand_cooldown.put(p.getUniqueId(), System.currentTimeMillis() + (1000 * (60*cooldown)));
            }

            p.getItemInHand().setDurability((short)0);
            p.sendMessage("§7(§aViper§7)§f Successfully repaired the item in your hand.");
            return true;
        }

        if(choice.equalsIgnoreCase("all"))
        {
            if(!p.hasPermission("fix.all"))
            {
                p.sendMessage("§7(§aViper§7)§f You don't have permission.");
                return true;
            }

            if(all_cooldown.containsKey(p.getUniqueId()))
            {
                if ((all_cooldown.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000 <= 0)
                {
                    all_cooldown.remove(p.getUniqueId());
                } else {
                    long remaining = (all_cooldown.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000;
                    long toMinutes = remaining/60;

                    if(toMinutes < 0)
                    {
                        toMinutes = 1;
                    }

                    if(toMinutes == 1)
                    {
                        p.sendMessage("§7(§aViper§7)§f You are still on cooldown for §a" + toMinutes + "§f minute.");
                        return true;
                    }

                    if(toMinutes >= 60)
                    {
                        long toHours = toMinutes / 60;

                        if(toMinutes == 60){
                            p.sendMessage("§7(§aViper§7)§f You are still on cooldown for §a" + toHours + "§f hour.");
                            return true;
                        } else {
                            p.sendMessage("§7(§aViper§7)§f You are still on cooldown for §a" + toHours + "§f hours.");
                            return true;
                        }
                    }

                    p.sendMessage("§7(§aViper§7)§f You are still on cooldown for §a" + toMinutes + "§f minutes.");
                    return true;
                }
            }

            if(!all_cooldown.containsKey(p.getUniqueId()))
            {
                int cooldown = Minssentials.getInstance().getConfig().getInt("fix-all-cooldown");
                all_cooldown.put(p.getUniqueId(), System.currentTimeMillis() + (1000 * (60*cooldown)));
            }

            for(ItemStack items : p.getInventory().getContents())
            {
                if(items != null){
                    if(canBeRepaired(items))
                    {
                        items.setDurability((short)0);
                    }
                }
            }

            for(ItemStack armor : p.getInventory().getArmorContents())
            {
                if(armor != null)
                {
                    if(canBeRepaired(armor))
                    {
                        armor.setDurability((short)0);
                    }
                }
            }

            p.sendMessage("§7(§aViper§7)§f Successfully repaired all items.");
            return true;
        }

        return true;
    }

    public boolean canBeRepaired(ItemStack item)
    {
        if(isArmor(item) || isTool(item) || isWeapon(item))
        {
            return true;
        }
        return false;
    }

    public boolean isArmor(ItemStack item)
    {
        if(item.getType() == Material.LEATHER_BOOTS
                || item.getType() == Material.LEATHER_LEGGINGS
                || item.getType() == Material.LEATHER_CHESTPLATE
                || item.getType() == Material.LEATHER_HELMET
                || item.getType() == Material.GOLD_BOOTS
                || item.getType() == Material.GOLD_LEGGINGS
                || item.getType() == Material.GOLD_CHESTPLATE
                || item.getType() == Material.GOLD_HELMET
                || item.getType() == Material.CHAINMAIL_BOOTS
                || item.getType() == Material.CHAINMAIL_LEGGINGS
                || item.getType() == Material.CHAINMAIL_CHESTPLATE
                || item.getType() == Material.CHAINMAIL_HELMET
                || item.getType() == Material.IRON_BOOTS
                || item.getType() == Material.IRON_LEGGINGS
                || item.getType() == Material.IRON_CHESTPLATE
                || item.getType() == Material.IRON_HELMET
                || item.getType() == Material.DIAMOND_BOOTS
                || item.getType() == Material.DIAMOND_LEGGINGS
                || item.getType() == Material.DIAMOND_CHESTPLATE
                || item.getType() == Material.DIAMOND_HELMET)
        {
            return true;
        }
        return false;
    }

    public boolean isTool(ItemStack item)
    {
        if(item.getType() == Material.FISHING_ROD
                || item.getType() == Material.WOOD_PICKAXE
                || item.getType() == Material.WOOD_SPADE
                || item.getType() == Material.WOOD_AXE
                || item.getType() == Material.WOOD_HOE
                || item.getType() == Material.IRON_PICKAXE
                || item.getType() == Material.IRON_SPADE
                || item.getType() == Material.IRON_AXE
                || item.getType() == Material.IRON_HOE
                || item.getType() == Material.GOLD_PICKAXE
                || item.getType() == Material.GOLD_SPADE
                || item.getType() == Material.GOLD_AXE
                || item.getType() == Material.GOLD_HOE
                || item.getType() == Material.STONE_PICKAXE
                || item.getType() == Material.STONE_SPADE
                || item.getType() == Material.STONE_AXE
                || item.getType() == Material.STONE_HOE
                || item.getType() == Material.DIAMOND_PICKAXE
                || item.getType() == Material.DIAMOND_SPADE
                || item.getType() == Material.DIAMOND_AXE
                || item.getType() == Material.DIAMOND_HOE)
        {
            return true;
        }
        return false;
    }

    public boolean isWeapon(ItemStack item)
    {
        if(item.getType() == Material.BOW
                || item.getType() == Material.WOOD_SWORD
                || item.getType() == Material.STONE_SWORD
                || item.getType() == Material.GOLD_SWORD
                || item.getType() == Material.IRON_SWORD
                || item.getType() == Material.DIAMOND_SWORD)
        {
            return true;
        }
        return false;
    }
}
