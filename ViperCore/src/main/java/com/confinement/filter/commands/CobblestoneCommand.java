package com.confinement.filter.commands;

import java.util.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class CobblestoneCommand implements CommandExecutor
{
    public static final ArrayList<UUID> cantPickup;
    
    static {
        cantPickup = new ArrayList<UUID>();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final Player player = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("cobble") && sender instanceof Player) {
            if (args.length == 0 && !CobblestoneCommand.cantPickup.contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You are now &cunable &7to pickup cobblestone."));
                CobblestoneCommand.cantPickup.add(player.getUniqueId());
            }
            else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You are now &aable &7to pickup cobblestone."));
                CobblestoneCommand.cantPickup.remove(player.getUniqueId());
            }
        }
        return true;
    }
}
