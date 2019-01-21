package com.vipermc.kitmap;

import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.faction.FactionDataManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Brennan on 6/16/2017.
 */
public class Updates implements Listener
{

    KitMap plugin;
    Updates(KitMap instance){
        this.plugin = instance;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){

        String command = event.getMessage();
        if(command.equalsIgnoreCase("/iron")
                || command.equalsIgnoreCase("/gold")
                || command.equalsIgnoreCase("/leather")
                || command.equalsIgnoreCase("/diamond"))
        {
            if(FactionsUtils.getFactionAt(event.getPlayer().getLocation()) != null)
            {
                String name = FactionsUtils.getFactionAt(event.getPlayer().getLocation()).getName();
                if(!name.equals("Spawn"))
                {
                    String msg = plugin.getConfig().getString("disabled-outside-spawn").replaceAll("&", "§");
                    event.getPlayer().sendMessage(msg);
                    event.setCancelled(true);
                    return;
                }
            } else {
                String msg = plugin.getConfig().getString("disabled-outside-spawn").replaceAll("&", "§");
                event.getPlayer().sendMessage(msg);
                event.setCancelled(true);
                return;
            }
        }
    }
}
