package com.cloth.vipersecurity.commands;

import com.cloth.vipersecurity.ViperSecurity;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by Brennan on 6/20/2017.
 */
public class ServerJoinEvent implements Listener
{
    ViperSecurity plugin;
    public ServerJoinEvent(ViperSecurity instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onConnect(ServerConnectEvent e)
    {
        ProxiedPlayer p = e.getPlayer();
        if(!plugin.configuration.contains("Addresses." + p.getName().toUpperCase() + ".IP"))
        {
            return;
        }

        if(!plugin.configuration.getList("Addresses." + p.getName().toUpperCase() + ".IP").contains(getIP(e.getPlayer().getAddress()))){
            p.disconnect(ChatColor.GRAY + "(" + ChatColor.GREEN + "Viper" + ChatColor.GRAY + ") You cannot join from that IP address.");
            return;
        }
    }

    public String getIP(InetSocketAddress address)
    {
        String x = address.toString().replace("/", "");
        x = x.substring(0, x.indexOf(":"));
        return x.trim();
    }
}
