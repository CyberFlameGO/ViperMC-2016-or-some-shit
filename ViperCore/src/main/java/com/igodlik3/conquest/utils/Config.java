package com.igodlik3.conquest.utils;

import org.bukkit.configuration.file.*;
import com.google.common.base.*;
import com.google.common.io.*;
import java.util.logging.*;
import java.nio.charset.*;
import org.bukkit.configuration.*;
import java.io.*;
import org.bukkit.plugin.*;

public class Config
{
    File f;
    FileConfiguration cfg;
    String path_;
    String fileName_;
    private Plugin main;
    
    public Config(final String path, final String fileName, final Plugin JavaPluginExtender) {
        this.main = JavaPluginExtender;
        this.path_ = path;
        this.fileName_ = fileName;
    }
    
    public void create() {
        this.f = new File(this.path_, this.fileName_);
        this.cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(this.f);
    }
    
    public void setDefault(final String filename) {
        final InputStream defConfigStream = this.main.getResource(filename);
        if (defConfigStream == null) {
            return;
        }
        YamlConfiguration defConfig;
        if (this.isStrictlyUTF8()) {
            defConfig = YamlConfiguration.loadConfiguration((Reader)new InputStreamReader(defConfigStream, Charsets.UTF_8));
        }
        else {
            defConfig = new YamlConfiguration();
            byte[] contents;
            try {
                contents = ByteStreams.toByteArray(defConfigStream);
            }
            catch (IOException e) {
                this.main.getLogger().log(Level.SEVERE, "Unexpected failure reading " + filename, e);
                return;
            }
            final String text = new String(contents, Charset.defaultCharset());
            if (!text.equals(new String(contents, Charsets.UTF_8))) {
                this.main.getLogger().warning("Default system encoding may have misread " + filename + " from plugin jar");
            }
            try {
                defConfig.loadFromString(text);
            }
            catch (InvalidConfigurationException e2) {
                this.main.getLogger().log(Level.SEVERE, "Cannot load configuration from jar", (Throwable)e2);
            }
        }
        this.cfg.setDefaults((Configuration)defConfig);
    }
    
    private boolean isStrictlyUTF8() {
        return this.main.getDescription().getAwareness().contains(PluginAwareness.Flags.UTF8);
    }
    
    public void saveConfig() {
        try {
            this.cfg.save(this.f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public FileConfiguration getConfig() {
        return this.cfg;
    }
    
    public File toFile() {
        return this.f;
    }
    
    public boolean exists() {
        return this.f.exists();
    }
}
