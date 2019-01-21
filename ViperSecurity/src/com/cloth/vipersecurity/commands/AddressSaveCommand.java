package com.cloth.vipersecurity.commands;
import com.cloth.vipersecurity.ViperSecurity;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;

/**
 * Created by Brennan on 6/20/2017.
 */

public class AddressSaveCommand extends Command
{
    ViperSecurity plugin;
    public AddressSaveCommand(ViperSecurity instance) {
        super("ipsave");
        this.plugin = instance;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if(!(commandSender instanceof ProxiedPlayer))
        {
            if(args.length == 2)
            {
                if(ProxyServer.getInstance().getPlayer(args[0]) != null)
                {
                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[0]);

                    String ip = args[1];
                    commandSender.sendMessage(TextComponent.fromLegacyText("Successfully set " + player.getName().toUpperCase() + "'s security ip."));

                    if(!plugin.configuration.contains("Addresses." + player.getName().toUpperCase() + ".IP"))
                    {
                        ArrayList<String> ips = new ArrayList<String>();
                        ips.add(ip);
                        plugin.configuration.set("Addresses." + player.getName().toUpperCase() + ".IP", ips);
                        plugin.saveConfig();
                    } else {
                        ArrayList<String> ips = (ArrayList<String>) plugin.configuration.getList("Addresses." + player.getName().toUpperCase() + ".IP");
                        ips.add(ip);

                        plugin.configuration.set("Addresses." + player.getName().toUpperCase() + ".IP", ips);
                        plugin.saveConfig();
                    }
                } else {
                    commandSender.sendMessage(TextComponent.fromLegacyText("Cannot find the specified player."));
                    return;
                }
            } else {
                commandSender.sendMessage(TextComponent.fromLegacyText("Did you mean: /ipsave <player> <ip>"));
                return;
            }
        } else {
            commandSender.sendMessage(TextComponent.fromLegacyText("You cannot use that command."));
            return;
        }
    }
}
