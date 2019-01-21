package com.igodlik3.modmode.commands;

import org.bukkit.configuration.*;
import com.igodlik3.modmode.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.igodlik3.modmode.utils.*;
import org.bukkit.*;

public class CmdStaffChat implements CommandExecutor
{
    private Configuration config;
    
    public CmdStaffChat() {
        this.config = (Configuration)ModMode.getInstance().getConfig();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        final Player player = (Player)sender;
        if (player.hasPermission("ModMode.StaffChat")) {
            if (args.length == 0) {
                player.sendMessage(Utils.color("&c/sc <message>"));
            }
            else {
                final StringBuilder message = new StringBuilder();
                for (int i = 0; i < args.length; ++i) {
                    message.append(String.valueOf(args[i]) + " ");
                }
                for (Player pls : Bukkit.getOnlinePlayers()){
                    if (pls.hasPermission("ModMode.StaffChat")) {
                        pls.sendMessage(Utils.color(this.config.getString("Mod-Mode.Staff-Chat.format").replaceAll("%PLAYER%", player.getName()).replaceAll("%MESSAGE%", message.toString())));
                    }
                }
            }
        }
        return true;
    }
}
