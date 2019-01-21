package com.cloth.viperores;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Brennan on 6/29/2017.
 */
public class PlayerConnection implements Listener
{
    @EventHandler
    public void onConnect(PlayerJoinEvent e)
    {
        if(!ViperOres.getInstance().playerdata.contains(e.getPlayer().getName().toLowerCase() + ".multiplier")){
            ViperOres.getInstance().playerdata.set(e.getPlayer().getName().toLowerCase() + ".multiplier", 0);
            ViperOres.getInstance().playerdata.set(e.getPlayer().getName().toLowerCase() + ".duration", 0);
            ViperOres.getInstance().playerdata.saveConfig();
        }
    }
}
