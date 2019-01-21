package com.cloth.vipersecurity;

import com.cloth.vipersecurity.commands.AddressRemoveCommand;
import com.cloth.vipersecurity.commands.AddressSaveCommand;
import com.cloth.vipersecurity.commands.ServerJoinEvent;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

/**
 * Created by Brennan on 6/20/2017.
 */
public class ViperSecurity extends Plugin
{

    public Configuration configuration;
    public void onEnable()
    {
        saveDefaultConfig();
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        getProxy().getPluginManager().registerCommand(this, new AddressSaveCommand(this));
        getProxy().getPluginManager().registerCommand(this, new AddressRemoveCommand(this));
        getProxy().getPluginManager().registerListener(this, new ServerJoinEvent(this));
    }

    public void onDisable()
    {

    }

    public void saveConfig()
    {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDefaultConfig()
    {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = getResourceAsStream("config.yml");
                     OutputStream os = new FileOutputStream(configFile)) {
                     ByteStreams.copy(is, os);
                }
            } catch (IOException error1) {
                throw new RuntimeException("Unable to create configuration file", error1);
            } catch (NullPointerException error2){

            }
        }
    }
}
