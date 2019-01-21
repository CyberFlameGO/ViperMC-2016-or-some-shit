package com.viperhcf.minssentials.commands;

import com.viperhcf.minssentials.Minssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Brennan on 6/29/2017.
 */
public class CommandCreateTag implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!sender.isOp()){
            sender.sendMessage("§7(§aViper§7)§f You don't have permission.");
            return true;
        }

        if(args.length != 3){
            sender.sendMessage("§7(§aViper§7)§f Correct usage: §a/createtag <name> <tag> <permission>");
            return true;
        }

        String name = args[0];
        String tag = args[1];
        String permission = args[2];

        Minssentials.getInstance().tags.set(name + ".name", tag);
        Minssentials.getInstance().tags.set(name + ".permission", permission);
        Minssentials.getInstance().tags.saveConfig();
        sender.sendMessage("§7(§aViper§7)§f Successfully created the tag: " + tag.replaceAll("&", "§"));
        return true;
    }
}
