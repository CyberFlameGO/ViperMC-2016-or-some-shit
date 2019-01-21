package com.igodlik3.conquest;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import org.bukkit.configuration.Configuration;
import com.igodlik3.conquest.utils.*;
import com.igodlik3.conquest.event.*;
import org.bukkit.*;
import org.bukkit.event.*;
import com.igodlik3.conquest.loot.*;
import com.igodlik3.conquest.listener.*;
import org.bukkit.plugin.*;
import com.igodlik3.conquest.commands.*;
import org.bukkit.command.*;
import com.igodlik3.conquest.task.*;

public class Conquest extends Manager
{
    private static Conquest instance;
    private ConquestManager conquestManager;
    private LootManager lootManager;


    private ConfigurationWrapper config;

    public Configuration getConfig(){
        return config.getConfig();
    }
    private Config storage;

    public Conquest(HCFactionPlugin plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        this.setUpConfig();
        (Conquest.instance = this).registerManagers();
        this.registerListeners();
        this.registerCommands();
    }

    @Override
    public void tear() {

        if (this.conquestManager.getRunningGame() != null) {
            this.conquestManager.setRunningGame(null);
        }
    }

    private void registerListeners() {
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents((Listener)new ScoreboardListener(), this.getPlugin());
        pm.registerEvents((Listener)new SelectionListener(), this.getPlugin());
        pm.registerEvents((Listener)new ChestListener(), this.getPlugin());
        pm.registerEvents((Listener)new PlayerListener(), this.getPlugin());
    }
    
    private void registerCommands() {
        registerCommand("conquest" , (CommandExecutor)new CmdConquest());
    }


    private void registerManagers() {
        this.conquestManager = new ConquestManager();
        this.lootManager = new LootManager();
        new ConquestTask().runTaskTimer(getPlugin(), 20L, 20L);
    }
    
    private void setUpConfig() {
        config = new ConfigurationWrapper("conquest.yml", this.getPlugin());
        config.saveDefault();
        final Config config = new Config("plugins/" + getPlugin().getName() + "/storage", "conqueset-storage.yml", getPlugin());
        config.create();
        if (!config.exists() || config.toFile().exists()) {
            config.saveConfig();
        }
        this.storage = config;
    }
    
    public static Conquest getInstance() {
        return Conquest.instance;
    }
    
    public ConquestManager getConquestManager() {
        return this.conquestManager;
    }
    
    public LootManager getLootManager() {
        return this.lootManager;
    }
    
    public Config getStorage() {
        return this.storage;
    }
}
