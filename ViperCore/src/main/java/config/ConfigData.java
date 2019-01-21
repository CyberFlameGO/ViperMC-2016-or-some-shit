package config;

import net.libhalt.bukkit.kaede.HCFactionPlugin;

/**
 * Created by Brennan on 6/15/2017.
 */
public class ConfigData
{

    HCFactionPlugin plugin;
    public ConfigData(HCFactionPlugin instance){
        this.plugin = instance;
    }

    public void setupDefaultMessages()
    {
        if(!isSetup())
        {
            plugin.messages.set("nether-portal-in-spawn", "&7(&aViper&7) You cannot enter the portal from spawn.");
            plugin.messages.set("all-players-in-spawn", "&7(&aViper&7) All players are currently in spawn.");
            plugin.messages.set("only-one-online", "&7(&aViper&7) You are the only person online.");
            plugin.messages.set("xp-bottle-created", "&7(&aViper&7) You have bottled the xp.");
            plugin.messages.set("xp-bottle-received", "&7(&aViper&7) You have un-bottled the xp. ");
            plugin.messages.set("xp-bottle-inventory", "&7(&aViper&7) You must have one available inventory space.");
            plugin.messages.set("xp-bottle-insufficient", "&7(&aViper&7) You must have a minimum of 30 XP to bottle.");
            plugin.messages.set("xp-bottle-name", "&a&lLevel 30 XP Bottle");
            plugin.messages.set("pearl-stuck-message", "&7(&aViper&7) Plugin prevented you from becoming stuck.");
            plugin.messages.set("pearl-suffocate-message", "&7(&aViper&7) Plugin prevented you from suffocating.");
            plugin.messages.set("sotw-started", "&7(&aViper&7) The SOTW has been started.");
            plugin.messages.set("sotw-stopped", "&7(&aViper&7) The SOTW has been stopped.");
            plugin.messages.set("sotw-active", "&7(&aViper&7) The SOTW is already active.");
            plugin.messages.set("sotw-inactive", "&7(&aViper&7) The SOTW is not active.");
            plugin.messages.saveConfig();
        }
    }

    public boolean isSetup()
    {
        if(!plugin.messages.contains("nether-portal-in-spawn"))
        {
            return false;
        }
        return true;
    }

    public String getConfigMessage(String message) {
        return plugin.messages.getString(message);
    }
}

