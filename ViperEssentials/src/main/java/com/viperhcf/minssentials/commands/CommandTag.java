package com.viperhcf.minssentials.commands;

import com.viperhcf.minssentials.Minssentials;
import com.viperhcf.minssentials.utils.TagUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Brennan on 6/29/2017.
 */
public class CommandTag implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§7(§aViper§7) Only players can use that command.");
            return true;
        }

        Player p = (Player) sender;
        TagUtil.openGUI(p);
        return true;
    }
}
