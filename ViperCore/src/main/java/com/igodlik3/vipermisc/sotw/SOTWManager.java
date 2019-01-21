package com.igodlik3.vipermisc.sotw;

import org.bukkit.*;
import com.igodlik3.vipermisc.*;
import com.igodlik3.vipermisc.utils.*;
import org.bukkit.entity.*;
import net.libhalt.bukkit.kaede.*;

public class SOTWManager
{
    private SOTWTimer timer;
    private HCFactionPlugin core;
    
    public SOTWManager() {
        this.timer = new SOTWTimer();
        this.core = HCFactionPlugin.getInstance();
    }
    
    public boolean isSOTWActive() {
        return this.timer.isActive();
    }

    public void activeSOTW() {
    activeSOTW(Misc.getInstance().getConfig().getInt("sotw-defaultTime"));
    }
    public void activeSOTW(int value) {
        for (Player player : Bukkit.getOnlinePlayers()){
            final PlayerData data = this.core.getPlayerDataManager().getPlayerData(player);
            if (data.getPvpTime() > 0) {
                data.setPvpTime(0);
            }
        }
        this.timer.setTimerEnd(System.currentTimeMillis() + 1000 * value);

        String started = HCFactionPlugin.getInstance().messages.getString("sotw-started").replaceAll("&", "§");
        Bukkit.broadcastMessage(started);
    }
    
    public SOTWTimer getTimer() {
        return this.timer;
    }
}
