package com.igodlik3.stats;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.Manager;
import net.milkbowl.vault.permission.Permission;

public class Stats extends Manager
{
    private Permission perms;
    private static Stats instance;

    public Stats(HCFactionPlugin plugin) {
        super(plugin);
    }

    public void init() {
        (Stats.instance = this).setUpPermissions();
        this.getPlugin().getCommand("stats").setExecutor((CommandExecutor)new StatsCmd());
        Bukkit.getPluginManager().registerEvents((Listener)new GUIListener(), this.getPlugin());
    }
    
    private boolean setUpPermissions() {
        final RegisteredServiceProvider<Permission> rsp = this.getPlugin().getServer().getServicesManager().getRegistration(Permission.class);
        this.perms = (Permission)rsp.getProvider();
        return this.perms != null;
    }

    public Permission getPerms() {
        return this.perms;
    }
    
    public static Stats getInstance() {
        return Stats.instance;
    }
}
