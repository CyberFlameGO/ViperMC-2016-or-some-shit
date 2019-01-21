package com.viperhcf.minssentials.commands;

import com.viperhcf.minssentials.Minssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by Brennan on 6/27/2017.
 */
public class CommandNick implements CommandExecutor
{

    Minssentials plugin;
    public CommandNick(Minssentials instance){
        this.plugin = instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "Only players can use that command.");
            return true;
        }

        Player p = (Player) sender;
        if(!p.hasPermission("minssentials.nick"))
        {
            p.sendMessage("§7(§aViper§7)§f You don't have permission.");
            return true;
        }

        p.openInventory(plugin.NicknameGUI);
        return true;
    }
}
