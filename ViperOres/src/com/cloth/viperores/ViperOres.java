package com.cloth.viperores;

import com.cloth.viperores.config.MyConfig;
import com.cloth.viperores.config.MyConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Brennan on 6/29/2017.
 */
public class ViperOres extends JavaPlugin
{

    MyConfigManager manager = new MyConfigManager(this);
    MyConfig playerdata = manager.getNewConfig("playerdata.yml");

    static ViperOres INSTANCE;

    public void onEnable()
    {
        INSTANCE = this;
        saveConfigFiles();
        registerListeners();
        registerCommands();
        OreMonitor.check();
    }

    private void registerListeners()
    {
        getServer().getPluginManager().registerEvents(new PlayerMine(), this);
        getServer().getPluginManager().registerEvents(new PlayerConnection(), this);
    }

    private void registerCommands()
    {
        getCommand("ore").setExecutor(new OreCommand());
    }

    private void saveConfigFiles()
    {
        playerdata.saveConfig();
    }

    public static ViperOres getInstance()
    {
        return INSTANCE;
    }
}
