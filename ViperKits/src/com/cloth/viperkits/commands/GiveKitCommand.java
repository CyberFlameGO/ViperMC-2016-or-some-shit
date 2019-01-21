package com.cloth.viperkits.commands;

import com.cloth.viperkits.config.ConfigurationSetup;
import com.cloth.viperkits.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

/**
 * Created by Brennan on 6/22/2017.
 */
public class GiveKitCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        if(!sender.hasPermission("viperkits.give")){
            String msg = ConfigurationSetup.messages.getString("no_permission").replaceAll("&", "§");
            sender.sendMessage(msg);
            return true;
        }

        if(args.length != 3){
            String msg = ConfigurationSetup.messages.getString("invalid_usage").replaceAll("&", "§");
            sender.sendMessage(msg.replace("%usage%", "§a/givekit <player> <kit> <duration>"));
            return true;
        }

        String target = args[0];
        if(!ConfigurationSetup.playerdata.contains(target.toLowerCase() + ".basic_cooldown")){
            String msg = ConfigurationSetup.messages.getString("invalid_player").replaceAll("&", "§");
            sender.sendMessage(msg);
            return true;
        }

        if(!Util.kitExists(args[1])){
            String msg = ConfigurationSetup.messages.getString("invalid_kit").replaceAll("&", "§");
            sender.sendMessage(msg);
            return true;
        }

        if(Util.convertStringToSeconds(args[2]) == -1){
            String msg = ConfigurationSetup.messages.getString("invalid_duration").replaceAll("&", "§");
            sender.sendMessage(msg);
            return true;
        }

        String msg1 = ConfigurationSetup.messages.getString("kit_given").replaceAll("&", "§");
        String msg2 = msg1.replace("%player%", target);
        String msg3 = msg2.replace("%kit%", Util.getKit(args[1]));
        String msg4 = msg3.replace("%duration%", args[2]);

        sender.sendMessage(msg4);
        ConfigurationSetup.playerdata.set(target.toLowerCase() + ".available_kits." + Util.getKit(args[1]), Util.convertStringToSeconds(args[2]));
        ConfigurationSetup.playerdata.saveConfig();
        return true;
    }
}
