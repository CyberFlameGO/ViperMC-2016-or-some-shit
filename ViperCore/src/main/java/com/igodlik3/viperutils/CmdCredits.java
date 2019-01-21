package com.igodlik3.viperutils;

import org.bukkit.command.*;
import org.bukkit.*;

public class CmdCredits implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("credits")) {
            sender.sendMessage(this.color("&9Developed by &eiGoDLiK3"));
            sender.sendMessage(this.color("&9Founded by &6Salvific"));
            sender.sendMessage(this.color("&aWebsite&7: &cMC-Outlet.com"));
        }
        return true;
    }
    
    private String color(final String stg) {
        return ChatColor.translateAlternateColorCodes('&', stg);
    }
}
