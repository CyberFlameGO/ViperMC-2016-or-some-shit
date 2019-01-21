package com.igodlik3.modmode.event;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.event.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.*;

public class VanishEvent extends Event
{
    private static final HandlerList handlers;
    public static Set<Player> vanished;
    private Player p;

    static {
        handlers = new HandlerList();
        VanishEvent.vanished = new HashSet<Player>();
    }

    public VanishEvent(final Player player, final boolean on) {
        this.p = player;
        if (on) {
            this.setVanish();
        }
        else {
            this.unVanish();
        }
    }

    public void setVanish() {
        VanishEvent.vanished.add(this.p);
        /*
        Player[] onlinePlayers;
        for (int length = (onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0])).length, i = 0; i < length; ++i) {
            final Player player = onlinePlayers[i];
            if (!player.hasPermission("ModMode.Vanish")) {
                player.hidePlayer(this.p);
            }
        }*/
        ((CraftPlayer)p).setHidden(true);
    }

    public void unVanish() {
        VanishEvent.vanished.remove(this.p);
        /*
        Player[] onlinePlayers;
        for (int length = (onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0])).length, i = 0; i < length; ++i) {
            final Player player = onlinePlayers[i];
            player.showPlayer(this.p);
        }
        */
        ((CraftPlayer)p).setHidden(false);
    }

    public boolean isVanish() {
        return VanishEvent.vanished.contains(this.p);
    }

    public String getVanishedPlayers() {
        return VanishEvent.vanished.toString();
    }

    public Player getPlayer() {
        return this.p;
    }

    public HandlerList getHandlers() {
        return VanishEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return VanishEvent.handlers;
    }
}
