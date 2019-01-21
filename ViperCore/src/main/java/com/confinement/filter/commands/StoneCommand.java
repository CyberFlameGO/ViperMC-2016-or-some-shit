package com.confinement.filter.commands;

import java.util.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class StoneCommand implements CommandExecutor
{
    public static final ArrayList<UUID> cantPickupStone;
    
    static {
        cantPickupStone = new ArrayList<UUID>();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final Player player = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("stone") && sender instanceof Player) {
            if (args.length == 0 && !StoneCommand.cantPickupStone.contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You are now &cunable &7to pickup stone!!"));
                StoneCommand.cantPickupStone.add(player.getUniqueId());
            }
            else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You are now &aable &7to pickup stone."));
                StoneCommand.cantPickupStone.remove(player.getUniqueId());
            }
        }
        return true;
    }
}
