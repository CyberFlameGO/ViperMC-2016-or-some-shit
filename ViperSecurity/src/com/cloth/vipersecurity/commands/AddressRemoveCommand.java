package com.cloth.vipersecurity.commands;

import com.cloth.vipersecurity.ViperSecurity;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Brennan on 6/20/2017.
 */
public class AddressRemoveCommand extends Command {

    ViperSecurity plugin;

    public AddressRemoveCommand(ViperSecurity instance) {
        super("ipremove");
        this.plugin = instance;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if(!(commandSender instanceof  ProxiedPlayer))
        {
            if(args.length == 2)
            {
                if(plugin.configuration.contains("Addresses." + args[0].toUpperCase() + ".IP"))
                {
                    String ip = args[1];

                    commandSender.sendMessage(TextComponent.fromLegacyText("Successfully removed " + args[0].toUpperCase() + "'s saved IP."));

                    if(plugin.configuration.getList("Addresses." + args[0].toUpperCase() + ".IP").size() > 0)
                    {
                        plugin.configuration.getList("Addresses." + args[0].toUpperCase() + ".IP").remove(ip);
                        plugin.configuration.set("Addresses." + args[0].toUpperCase() + ".IP", plugin.configuration.getList("Addresses." + args[0].toUpperCase() + ".IP"));
                        plugin.saveConfig();
                    } else {
                        plugin.configuration.set("Addresses." + args[0].toUpperCase() + ".IP", null);
                        plugin.saveConfig();
                    }
                } else {
                    commandSender.sendMessage(TextComponent.fromLegacyText("That player is not in the config."));
                    return;
                }
            } else {
                commandSender.sendMessage(TextComponent.fromLegacyText("Did you mean: /ipremove <player> <ip>"));
                return;
            }
        } else {
            commandSender.sendMessage(TextComponent.fromLegacyText("You cannot use that command."));
            return;
        }
    }

    public boolean hasIP(String player, String ip)
    {
        for(String messages : (ArrayList<String>) plugin.configuration.getList("Addresses." + player.toUpperCase() + ".IP"))
        {
            ProxyServer.getInstance().broadcast(messages);
            if(messages.equalsIgnoreCase(ip)){
                return true;
            }
        }
        return false;
    }
}