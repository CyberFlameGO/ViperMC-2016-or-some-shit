package me.dreamzy.report;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import net.syuu.popura.command.faction.FactionCommand;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import me.dreamzy.report.stats.*;
import me.dreamzy.report.reports.*;
import org.bukkit.command.*;

public class ViperReport extends Manager
{
    private static ViperReport instance;
    private String prefix;
    private String prefixReportGui;
    private String prefixReportChatGui;
    private String prefixReportCheatGui;
    private StatsManager statsManager;
    private ReportManager reportManager;
    
    {
        this.prefix = "&8[&9Delta&aReport&8]";
        this.prefixReportGui = "&e\u2022 &9Report &6";
        this.prefixReportChatGui = "&2Cheat &e";
        this.prefixReportCheatGui = "&6Chat &e";
    }

    public ViperReport(HCFactionPlugin plugin) {
        super(plugin);
    }
    private ConfigurationWrapper config;
    public Configuration getConfig(){
        return config.getConfig();
    }
    public void init() {
        config = new ConfigurationWrapper("report.yml", this.getPlugin());
        config.saveDefault();
        (ViperReport.instance = this).loadManagers();
        this.loadListeners();
        this.loadCommands();
    }
    
    public void onDisable() {
        this.statsManager.saveData();
    }

    private void loadManagers() {
        (this.statsManager = new StatsManager()).reloadData();
        this.reportManager = new ReportManager();
    }
    
    public void loadListeners() {
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents((Listener)new StatsListener(), this.getPlugin());
        //pm.registerEvents((Listener)new ReportListener(), this.getPlugin());
    }
    
    public void loadCommands() {
        FactionCommand.INSTANCE.addSubCommand(new CmdStats());
        //this.getPlugin().getCommand("report").setExecutor((CommandExecutor)new CmdReport());
    }
    
    public static ViperReport getInstance() {
        return ViperReport.instance;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public String getPrefixReportGui() {
        return this.prefixReportGui;
    }
    
    public String getPrefixReportChatGui() {
        return this.prefixReportChatGui;
    }
    
    public String getPrefixReportCheatGui() {
        return this.prefixReportCheatGui;
    }
    
    public StatsManager getStatsManager() {
        return this.statsManager;
    }
    
    public ReportManager getReportManager() {
        return this.reportManager;
    }
}
