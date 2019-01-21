package com.viperhcf.minssentials.events;

import com.viperhcf.minssentials.Minssentials;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Brennan on 6/27/2017.
 */
public class PlayerConnectEvent implements Listener
{

    Minssentials plugin;
    public PlayerConnectEvent(Minssentials instance){
        this.plugin = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        if(!plugin.playerdata.contains(p.getName() + ".color"))
        {
            plugin.playerdata.set(p.getName() + ".color", "&f");
            plugin.playerdata.set(p.getName() + ".tag", "None");
            plugin.playerdata.set(p.getName() + ".fix-hand-cooldown", 0);
            plugin.playerdata.set(p.getName() + ".fix-all-cooldown", 0);
            plugin.playerdata.saveConfig();
        }
    }
}
