package com.confinement.filter;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.Manager;
import org.bukkit.plugin.java.*;
import org.bukkit.command.*;
import com.confinement.filter.commands.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import com.confinement.filter.listeners.*;

public class Filter extends Manager
{
    public Filter(HCFactionPlugin plugin) {
        super(plugin);
    }

    public void init() {
        getPlugin().getCommand("cobble").setExecutor((CommandExecutor)new CobblestoneCommand());
        getPlugin().getCommand("stone").setExecutor((CommandExecutor)new StoneCommand());
        getPlugin().getCommand("mobdrops").setExecutor((CommandExecutor)new MobDropsCommand());
        Bukkit.getServer().getPluginManager().registerEvents((Listener)new CobblestoneListener(), (Plugin)getPlugin());
        Bukkit.getServer().getPluginManager().registerEvents((Listener)new StoneListener(), (Plugin)getPlugin());
        Bukkit.getServer().getPluginManager().registerEvents((Listener)new MobDropsListener(), (Plugin)getPlugin());
    }
    
    public void onDisable() {
    }
}
