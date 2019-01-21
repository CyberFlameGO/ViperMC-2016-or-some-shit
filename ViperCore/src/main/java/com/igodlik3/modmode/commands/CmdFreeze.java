package com.igodlik3.modmode.commands;

import com.igodlik3.modmode.manager.*;
import com.igodlik3.modmode.*;
import org.bukkit.command.*;
import com.igodlik3.modmode.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class CmdFreeze implements CommandExecutor
{
    private FreezeManager manager;
    
    public CmdFreeze() {
        this.manager = ModMode.getInstance().getFreezeManager();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (sender.hasPermission("ModMode")) {
            if (args.length == 0) {
                sender.sendMessage(Utils.color("&c/ss <player>"));
            }
            else {
                final Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(Utils.color("&cThis player is not online or doesn't exist !"));
                    return true;
                }
                sender.sendMessage(this.manager.freezePlayer(target));
            }
        }
        return false;
    }
}
