package com.vipermc.kitmap;

import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.claim.Position2D;
import net.syuu.popura.faction.bean.ClaimedRegion;
import net.syuu.popura.faction.bean.Faction;
import net.syuu.popura.faction.bean.FactionPlayer;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.event.*;
import java.util.*;

public class FactionsUtils
{
    public static boolean isInOwnTerritory(final Player player) {
        final Location loc = player.getLocation();
        return hasFaction(player) && getFactionAt(loc) != null && getFactionAt(loc) == getFaction(player);
    }
    
    public static boolean hasFaction(final Player player) {
        final FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);

        return factionPlayer.getFaction() != null;
    }

    
    public static Faction getFactionAt(final Location location) {
        ClaimedRegion flocation = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(location.getWorld().getName() , location.getBlockX() , location.getBlockZ()));
        return flocation == null ? null : flocation.getOwner();
    }
    
    public static Faction getFaction(final Player player) {
        final FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
        return factionPlayer.getFaction();
    }

}
