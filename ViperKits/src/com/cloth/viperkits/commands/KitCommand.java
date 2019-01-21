package com.cloth.viperkits.commands;

import com.cloth.viperkits.config.ConfigurationSetup;
import com.cloth.viperkits.util.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Brennan on 6/21/2017.
 */
public class KitCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player)){
            return true;
        }

        Player p = (Player) sender;
        InventoryManager.setupInventory(p);
        return true;
    }
}
