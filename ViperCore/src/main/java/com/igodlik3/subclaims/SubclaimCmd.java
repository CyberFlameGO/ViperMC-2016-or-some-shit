package com.igodlik3.subclaims;

import org.bukkit.configuration.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.*;

public class SubclaimCmd implements CommandExecutor
{
    private Configuration lang;
    
    public SubclaimCmd() {
        this.lang = Subclaims.getInstance().getConfig();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        final Player player = (Player)sender;
        if (args.length == 0) {
            for (final String stg : this.lang.getStringList("Messages.Subclaim.INFO")) {
                player.sendMessage(this.color(stg));
            }
        }
        else if (args[0].equalsIgnoreCase("credits")) {
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
