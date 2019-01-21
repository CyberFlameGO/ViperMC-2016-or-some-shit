package com.igodlik3.conquest.listener;

import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.faction.bean.Faction;
import net.syuu.popura.faction.bean.FactionPlayer;
import org.bukkit.configuration.*;
import com.igodlik3.conquest.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import com.igodlik3.conquest.utils.*;
import com.igodlik3.conquest.event.*;
import java.util.*;
import org.bukkit.event.*;

public class PlayerListener implements Listener
{
    private Configuration config;
    private ConquestManager cm;
    
    public PlayerListener() {
        this.config = (Configuration)Conquest.getInstance().getConfig();
        this.cm = Conquest.getInstance().getConquestManager();
    }
    
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        if (this.config.getBoolean("CONQUEST.Penality.enabled") && this.cm.getRunningGame() != null) {
            final ConquestData ca = this.cm.getRunningGameData();
            final FactionPlayer fp = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);

            if (fp.getFaction() != null) {
                final Faction faction = fp.getFaction();
                final int penality = this.config.getInt("CONQUEST.Penality.points");
                if (ca.getFactionPoints(faction) >= penality) {
                    ca.setFactionsPoints(faction, ca.getFactionPoints(faction) - penality, false);
                    for (final Player pls : faction.getOnlineMembers()) {
                        pls.sendMessage(Utils.color(this.config.getString("Messages.PENALITY").replaceAll("%POINTS%", String.valueOf(penality))));
                    }
                }
            }
        }
    }
}
