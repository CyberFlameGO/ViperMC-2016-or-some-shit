package com.viperhcf.minssentials.events;

import com.viperhcf.minssentials.Minssentials;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

/**
 * Created by Brennan on 6/16/2017.
 */
public class CustomCommandEvent implements Listener
{

    Minssentials plugin;
    public CustomCommandEvent(Minssentials instance){
        this.plugin = instance;
    }

    @EventHandler
    public void onCustomCommand(PlayerCommandPreprocessEvent e){
        String[] args = e.getMessage().split(" ");
        for(String input : plugin.getConfig().getConfigurationSection("text-cmd").getKeys(false))
        {
            final List<String> message = plugin.getConfig().getConfigurationSection("text-cmd").getStringList(input);
            if(args[0].equalsIgnoreCase("/" + input))
            {
                e.setCancelled(true);
                for(String x : message)
                {
                    e.getPlayer().sendMessage(x.replaceAll("&", "§"));
                }
                return;
            }
        }
    }
}
