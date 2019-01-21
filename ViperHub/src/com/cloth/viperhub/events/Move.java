package com.cloth.viperhub.events;

import com.cloth.viperhub.ViperHub;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Brennan on 6/16/2017.
 */
public class Move implements Listener
{

    ViperHub plugin;
    public Move(ViperHub instance){
        this.plugin = instance;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if(plugin.flying.contains(e.getPlayer().getUniqueId()))
        {
            if(e.getPlayer().getLocation().subtract(0, .5, 0).getBlock().getType() != Material.AIR)
            {
                plugin.flying.remove(e.getPlayer().getUniqueId());
                e.getPlayer().teleport(e.getPlayer().getLocation().add(0, 1.5, 0));
                return;
            }
        }
    }
}
