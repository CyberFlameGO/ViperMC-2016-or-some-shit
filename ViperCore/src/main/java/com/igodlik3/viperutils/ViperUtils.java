package com.igodlik3.viperutils;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import net.syuu.popura.command.faction.FactionCommand;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import org.bukkit.command.*;

public class ViperUtils extends Manager
{
    private static ViperUtils instance;
    private ConfigurationWrapper config;

    public ViperUtils(HCFactionPlugin plugin) {
        super(plugin);
    }

    public Configuration getConfig(){
        return config.getConfig();
    }
    public void init() {
        config = new ConfigurationWrapper("viper-utils.yml", this.getPlugin());
       config.saveDefault();
        ViperUtils.instance = this;
        FactionCommand.INSTANCE.addSubCommand(new CmdMuteFaction());
        Bukkit.getPluginManager().registerEvents((Listener)new BoostXPListener(), getPlugin());
        this.getPlugin().getCommand("utils").setExecutor((CommandExecutor)new CmdCredits());
    }
    
    public static ViperUtils getInstance() {
        return ViperUtils.instance;
    }
}
