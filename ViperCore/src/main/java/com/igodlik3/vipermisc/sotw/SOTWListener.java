package com.igodlik3.vipermisc.sotw;

import com.igodlik3.vipermisc.*;
import org.bukkit.entity.*;
import net.libhalt.bukkit.kaede.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

public class SOTWListener implements Listener
{
    private Misc misc;
    private SOTWManager sotwManager;
    
    public SOTWListener() {
        this.misc = Misc.getInstance();
        this.sotwManager = this.misc.getSotwManager();
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (!this.sotwManager.isSOTWActive()) {
            return;
        }
        final Player player = event.getPlayer();
        final PlayerData data = misc.getHcfStuff().getPlayerDataManager().getPlayerData(player);
        if (data != null && data.getPvpTime() > 0) {
            data.setPvpTime(0);
        }

    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        if (!this.sotwManager.isSOTWActive()) {
            return;
        }
        final Player player = event.getPlayer();
        final PlayerData data = this.misc.getHcfStuff().getPlayerDataManager().getPlayerData(player);
        if (data.getPvpTime() > 0) {
            data.setPvpTime(0);
        }
    }
    
    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof Player) || !this.sotwManager.isSOTWActive()) {
            return;
        }
        if (event.getCause() != EntityDamageEvent.DamageCause.VOID) {
            event.setCancelled(true);
        }
    }
}
