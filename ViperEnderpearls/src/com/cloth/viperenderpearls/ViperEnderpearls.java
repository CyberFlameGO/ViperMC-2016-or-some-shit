package com.cloth.viperenderpearls;

import com.cloth.viperenderpearls.manager.FilesCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

/**
 * Created by Brennan on 6/20/2017.
 */
public class ViperEnderpearls extends JavaPlugin
{
    File f;
    FileConfiguration fc;
    FilesCreator FilesCreatorInstance = FilesCreator.getInstance();

    public void onEnable()
    {
        this.FilesCreatorInstance.setup(this);
        PluginDescriptionFile pdfFile = getDescription();
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerTeleport(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        FileConfiguration config = getConfig();
        config.addDefault("anti-glitch", Boolean.valueOf(true));
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void onDisable()
    {
        reloadConfig();
    }

    public static String toColor(String text)
    {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
