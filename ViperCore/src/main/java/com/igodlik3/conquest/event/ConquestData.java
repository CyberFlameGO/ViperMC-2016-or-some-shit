package com.igodlik3.conquest.event;

import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.faction.bean.Faction;
import net.syuu.popura.faction.bean.FactionPlayer;
import org.bukkit.entity.*;
import java.util.*;
import com.igodlik3.conquest.*;

public class ConquestData
{
    private Map<String, Integer> factionsPoints;
    private Map<ConquestArea.Type, Integer> capTime;
    private Set<Faction> containedFactions;
    private Map<ConquestArea.Type, Player> firstCapper;
    
    public ConquestData() {
        this.factionsPoints = new HashMap<String, Integer>();
        this.capTime = new HashMap<ConquestArea.Type, Integer>();
        this.containedFactions = new HashSet<Faction>();
        this.firstCapper = new HashMap<ConquestArea.Type, Player>();
    }
    
    public Map<String, Integer> getFactionsPoints() {
        return this.factionsPoints;
    }
    
    public int getFactionPoints(final Player player) {
        final FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);

        return factionPlayer.getFaction() != null ? this.getFactionPoints(factionPlayer.getFaction()) : -1;
    }
    
    public int getFactionPoints(final Faction faction) {
        return this.factionsPoints.containsKey(faction.getName()) ? this.factionsPoints.get(faction.getName()) : -1;
    }
    
    public void setFactionsPoints(final Faction faction, final int points, final boolean contained) {
        if (contained) {
            this.containedFactions.add(faction);
        }
        this.factionsPoints.put(faction.getName(), points);
    }
    
    public void setFactionsPoints(final Player player, final int points) {
        final FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);

        if (factionPlayer.getFaction() != null) {
            this.setFactionsPoints(factionPlayer.getFaction(), points, true);
        }
    }
    
    public boolean capPoint(final Faction faction, final ConquestArea.Type type) {
        this.containedFactions.add(faction);
        if (!this.capTime.containsKey(type)) {
            this.capTime.put(type, 0);
            return false;
        }
        final int time = this.capTime.get(type) + 1;
        this.capTime.put(type, time);
        final boolean capped = time >= Conquest.getInstance().getConfig().getInt("CONQUEST.Cap-Time");
        if (capped) {
            this.capTime.put(type, 0);
        }
        return capped;
    }
    
    public int getCapTime(final ConquestArea.Type type) {
        final Integer time = this.capTime.get(type);
        return (time == null) ? 0 : time;
    }
    
    public void resetTime(final ConquestArea.Type type) {
        this.capTime.put(type, 0);
    }
    
    public Set<Faction> getContainedFactions() {
        return this.containedFactions;
    }
    
    public Player getFirstCapper(final ConquestArea.Type type) {
        return this.firstCapper.get(type);
    }
    
    public void setFirstCapper(final ConquestArea.Type type, final Player capper) {
        if (capper == null) {
            this.firstCapper.remove(type);
        }
        else {
            this.firstCapper.put(type, capper);
        }
    }
}
