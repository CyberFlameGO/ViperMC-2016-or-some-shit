package com.confinement.filter.commands;

import java.util.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class MobDropsCommand implements CommandExecutor
{
    public static final ArrayList<String> cantPickupMobs;
    
    static {
        cantPickupMobs = new ArrayList<String>();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final Player player = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("mobdrops") && sender instanceof Player) {
            if (args.length == 0 && !MobDropsCommand.cantPickupMobs.contains(player.getUniqueId().toString())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You are now &cunable &7to pickup mob drops."));
                MobDropsCommand.cantPickupMobs.add(player.getUniqueId().toString());
            }
            else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You are now &aable &7to pickup mob drops."));
                MobDropsCommand.cantPickupMobs.remove(player.getUniqueId().toString());
            }
        }
        return true;
    }
}
