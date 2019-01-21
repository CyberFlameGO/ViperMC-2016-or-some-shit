package com.cloth.viperkits;

import com.cloth.viperkits.commands.GiveKitCommand;
import com.cloth.viperkits.commands.KitCommand;
import com.cloth.viperkits.commands.SaveKitCommand;
import com.cloth.viperkits.config.ConfigurationSetup;
import com.cloth.viperkits.events.ClickEvent;
import com.cloth.viperkits.util.CooldownManager;
import com.cloth.viperkits.util.Util;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * Created by Brennan on 6/20/2017.
 */
public class ViperKits extends JavaPlugin
{
    public static ViperKits INSTANCE;

    public void onEnable() {
        INSTANCE = this;
        setupConfigurationFiles();
        setupAliases();
        registerCommands();
        registerEvents();
        registerCooldowns();
    }

    public static ViperKits getInstance() {
        return INSTANCE;
    }

    private void registerCooldowns(){
        CooldownManager.startSubtracting();
    }

    private void setupAliases()
    {
        Util.setupAliases();
    }

    private void registerCommands() {
        getCommand("givekit").setExecutor(new GiveKitCommand());
        getCommand("savekit").setExecutor(new SaveKitCommand());
        getCommand("kit").setExecutor(new KitCommand());
        getCommand("kit").setAliases(Util.kitAliases);
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new ConfigurationSetup(), this);
        getServer().getPluginManager().registerEvents(new ClickEvent(), this);
    }

    private void setupConfigurationFiles(){
        ConfigurationSetup.setupGUI();
        ConfigurationSetup.setupKits();
        ConfigurationSetup.setupMessages();
    }
}
