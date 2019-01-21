package com.cloth.viperenderpearls.manager;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class FilesCreator
{
    static FilesCreator instance = new FilesCreator();
    Plugin p;
    FileConfiguration config;
    File cfile;

    public static FilesCreator getInstance()
    {
        return instance;
    }

    public void setup(Plugin p)
    {
        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }
        this.cfile = new File(p.getDataFolder(), "config.yml");
        if (!this.cfile.exists()) {
            try
            {
                this.cfile.createNewFile();
            }
            catch (IOException e)
            {
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create config.yml!");
            }
        }
        this.config = p.getConfig();
    }

    public FileConfiguration getConfig()
    {
        return this.config;
    }

    public void saveConfig()
    {
        try
        {
            this.config.save(this.cfile);
        }
        catch (IOException e)
        {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save config.yml!");
        }
    }
}
