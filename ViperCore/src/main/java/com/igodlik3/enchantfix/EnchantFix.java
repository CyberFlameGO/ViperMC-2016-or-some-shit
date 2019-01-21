package com.igodlik3.enchantfix;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;

public class EnchantFix extends Manager
{
    private static EnchantFix instance;
    private ConfigurationWrapper config;

    public EnchantFix(HCFactionPlugin plugin) {
        super(plugin);
    }

    public Configuration getConfig(){
        return config.getConfig();
    }
    public void init() {
        config = new ConfigurationWrapper("enchant-fix.yml", this.getPlugin());
        EnchantFix.instance = this;
        this.getPlugin().getServer().getPluginManager().registerEvents((Listener)new EnchantListener(),getPlugin());
    }
    
    public static EnchantFix getInstance() {
        return EnchantFix.instance;
    }
}
