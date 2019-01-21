package com.igodlik3.modmode;

import com.google.common.collect.Sets;
import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.*;
import com.igodlik3.modmode.manager.*;
import java.util.*;
import org.bukkit.command.*;
import com.igodlik3.modmode.commands.*;
import org.bukkit.event.*;
import com.igodlik3.modmode.listener.*;
import org.bukkit.plugin.*;

public class ModMode extends Manager
{
    public static ModMode instance;
    private Set<UUID> modToggled = Sets.newHashSet();
    private FreezeManager freezeManager;
    private ConfigurationWrapper config;

    public Configuration getConfig(){
        return config.getConfig();
    }

    public ModMode(HCFactionPlugin plugin) {
        super(plugin);
    }


    public void init() {
        config = new ConfigurationWrapper("modmode.yml", this.getPlugin());
        config.saveDefault();
        (ModMode.instance = this).registerManagers();
        this.registerListeners();
        this.registerCommands();
    }
    
    private void registerCommands() {
        this.getPlugin().getCommand("mod").setExecutor((CommandExecutor)new CmdModMode());
        this.getPlugin().getCommand("freeze").setExecutor((CommandExecutor)new CmdFreeze());
        this.getPlugin().getCommand("staffchat").setExecutor((CommandExecutor)new CmdStaffChat());
    }
    
    private void registerListeners() {
        final PluginManager pm = this.getPlugin().getServer().getPluginManager();
        pm.registerEvents((Listener)new ItemListener(this.getPlugin()), this.getPlugin());
        pm.registerEvents((Listener)new ModListener(), this.getPlugin());
        pm.registerEvents((Listener)new VanishListener(), this.getPlugin());
        pm.registerEvents((Listener)new ScoreboardListener(), this.getPlugin());
    }
    
    private void registerManagers() {
        (this.freezeManager = new FreezeManager()).runTaskTimer(this.getPlugin(), 20L, (long)(20 * this.getConfig().getInt("Mod-Mode.Freeze.message-delay")));
    }
    
    public static ModMode getInstance() {
        return ModMode.instance;
    }
    
    public Set<UUID> getModToggled() {
        return this.modToggled;
    }
    
    public FreezeManager getFreezeManager() {
        return this.freezeManager;
    }
}
