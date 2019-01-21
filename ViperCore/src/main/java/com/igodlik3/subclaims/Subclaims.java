package com.igodlik3.subclaims;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import org.bukkit.command.*;

public class Subclaims extends Manager
{
    private static Subclaims instance;

    public Subclaims(HCFactionPlugin plugin) {
        super(plugin);
    }
    private ConfigurationWrapper config;

    public Configuration getConfig(){
        return config.getConfig();
    }
    public void init() {
        config = new ConfigurationWrapper("subclaim.yml", this.getPlugin());
        config.saveDefault();
        Subclaims.instance = this;
        Bukkit.getPluginManager().registerEvents((Listener)new SubclaimListener(), this.getPlugin());
        registerCommand("subclaim" , new SubclaimCmd());
    }
    
    public static Subclaims getInstance() {
        return Subclaims.instance;
    }
}
