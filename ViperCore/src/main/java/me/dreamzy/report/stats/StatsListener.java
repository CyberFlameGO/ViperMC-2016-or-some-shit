package me.dreamzy.report.stats;

import me.dreamzy.report.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class StatsListener implements Listener
{
    private StatsManager manager;
    
    public StatsListener() {
        this.manager = ViperReport.getInstance().getStatsManager();
    }
    
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        this.manager.getUser(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.manager.getUser(event.getPlayer()).saveStats(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerKick(final PlayerKickEvent event) {
        this.manager.getUser(event.getPlayer()).saveStats(event.getPlayer());
    }
}
