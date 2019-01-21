package com.igodlik3.vipermisc;

import com.igodlik3.vipermisc.listeners.ScoreboardListener;
import com.igodlik3.vipermisc.sotw.SOTWCmd;
import com.igodlik3.vipermisc.sotw.SOTWListener;
import com.igodlik3.vipermisc.sotw.SOTWManager;
import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * Created by libhalt on 2016/12/06.
 */
public class Misc extends Manager{
    public static Misc INSTANCE;
    private HCFactionPlugin hcfStuff;

    public static Misc getInstance(){
        return INSTANCE;
    }
    public Misc(HCFactionPlugin plugin) {
        super(plugin);
        INSTANCE = this;
    }

    private ConfigurationWrapper config;
    private SOTWManager sotwManager;
    public Configuration getConfig(){
        return config.getConfig();
    }
    public void init() {
        hcfStuff = HCFactionPlugin.getInstance();
        config = new ConfigurationWrapper("viper-misc.yml", this.getPlugin());
        config.saveDefault();
        sotwManager = new SOTWManager();
        getPlugin().getCommand("sotw").setExecutor(new SOTWCmd());


        getPlugin().getServer().getPluginManager().registerEvents((Listener) new ScoreboardListener(), getPlugin());
        getPlugin().getServer().getPluginManager().registerEvents((Listener)new SOTWListener(), getPlugin());
    }

    public SOTWManager getSotwManager() {
        return sotwManager;
    }

    public HCFactionPlugin getHcfStuff() {
        return hcfStuff;
    }
}
