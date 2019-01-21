package com.igodlik3.conquest.task;

import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.faction.bean.Faction;
import net.syuu.popura.faction.bean.FactionPlayer;
import org.bukkit.scheduler.*;
import org.bukkit.configuration.*;
import com.igodlik3.conquest.*;
import org.bukkit.entity.*;
import com.igodlik3.conquest.loot.*;
import com.igodlik3.conquest.event.*;
import com.igodlik3.conquest.utils.*;
import java.util.*;
import org.bukkit.*;

public class ConquestTask extends BukkitRunnable
{
    private Configuration config;
    
    public ConquestTask() {
        this.config = (Configuration)Conquest.getInstance().getConfig();
    }
    
    public void run() {
        if (Conquest.getInstance().getConquestManager().getRunningGame() == null) {
            return;
        }
        final ConquestManager manager = Conquest.getInstance().getConquestManager();
        final ConquestData data = manager.getRunningGameData();
        final ConquestGame game = manager.getRunningGame();
        ConquestArea.Type[] values;
        for (int length = (values = ConquestArea.Type.values()).length, j = 0; j < length; ++j) {
            final ConquestArea.Type type = values[j];
            final ConquestArea area = game.getArea(type);
            final Cuboid cubo = manager.getConquestCubo(area);
            final List<Player> containedPlayers = new ArrayList<Player>();
            final List<Faction> containedFactions = new ArrayList<Faction>();
            for(Player player : Bukkit.getOnlinePlayers()){
                final FactionPlayer fp = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
                if (fp.getFaction() != null) {
                    if (cubo.contains(player.getLocation())) {
                        containedPlayers.add(player);
                        if (!containedFactions.contains(fp.getFaction())) {
                            containedFactions.add(fp.getFaction());
                        }
                    }
                }
            }
            if (containedFactions.size() == 0) {
                data.resetTime(type);
                data.setFirstCapper(area.getType(), null);
            }
            else {
                if (data.getFirstCapper(area.getType()) != null && !containedFactions.contains(data.getFirstCapper(area.getType()))) {
                    data.setFirstCapper(area.getType(), null);
                }
                if (data.getFirstCapper(area.getType()) == null) {
                    data.setFirstCapper(area.getType(), containedPlayers.get(0));
                }
                final FactionPlayer fp2 = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(data.getFirstCapper(area.getType()));
                if (fp2.getFaction() != null && data.capPoint(fp2.getFaction(), type)) {
                    int points = data.getFactionPoints(data.getFirstCapper(area.getType()));
                    points = ((points == -1) ? 1 : (points + 1));
                    data.setFactionsPoints(data.getFirstCapper(area.getType()), points);
                    if (String.valueOf(points).endsWith("0")) {
                        Bukkit.broadcastMessage(Utils.color(this.config.getString("Messages.CONQUEST-CAPPING").replaceAll("%POINTS%", String.valueOf(points)).replaceAll("%AREA%", this.getColor(area.getType()) + area.getType().name()).replaceAll("%FACTION%", fp2.getFaction().getName())));
                    }
                    if (data.getFactionPoints(data.getFirstCapper(area.getType())) >= this.config.getInt("CONQUEST.Required-Points")) {
                        manager.setRunningGame(null);
                        manager.resetRunningGameData();
                        Player player2 = null;
                        for (final Player testPlayer : containedPlayers) {
                            final FactionPlayer fp3 = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(testPlayer);
                            if (fp3.getFaction() == data.getFirstCapper(area.getType())) {
                                player2 = testPlayer;
                                break;
                            }
                        }
                        if (player2 == null) {
                            for(Player testPlayer : Bukkit.getOnlinePlayers()){
                                final FactionPlayer fp4 = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(testPlayer);
                                if (fp4.getFaction() == data.getFirstCapper(area.getType())) {
                                    player2 = testPlayer;
                                    break;
                                }
                            }
                        }
                        final FactionPlayer fp5 = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player2);
                        Bukkit.broadcastMessage(Utils.color(this.config.getString("Messages.Conquest.CONQUEST-WIN").replaceAll("%FACTION%", fp5.getFaction().getName()).replaceAll("%POINTS%", String.valueOf(this.config.getInt("CONQUEST.Required-Points")))));
                        final ChestKey key = new ChestKey();
                        for (int i = 0; i < this.config.getInt("Conquest.Loot.Key.amount"); ++i) {
                            key.giveKey(player2);
                        }
                    }
                }
            }
        }
    }
    
    private ChatColor getColor(final ConquestArea.Type type) {
        switch (type) {
            case RED: {
                return ChatColor.RED;
            }
            case YELLOW: {
                return ChatColor.YELLOW;
            }
            case BLUE: {
                return ChatColor.BLUE;
            }
            case GREEN: {
                return ChatColor.GREEN;
            }
            default: {
                return null;
            }
        }
    }
}
