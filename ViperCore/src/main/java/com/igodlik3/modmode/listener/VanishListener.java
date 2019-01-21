package com.igodlik3.modmode.listener;

import org.bukkit.configuration.*;
import com.igodlik3.modmode.*;
import com.igodlik3.modmode.event.*;
import com.igodlik3.modmode.utils.*;
import org.bukkit.potion.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.*;
import org.bukkit.event.player.*;

public class VanishListener implements Listener
{
    private Configuration config;

    public VanishListener() {
        this.config = (Configuration)ModMode.getInstance().getConfig();
    }

    @EventHandler
    public void onVanishEvent(final VanishEvent event) {
        final Player player = event.getPlayer();
        final String e = this.config.getString("Messages.STATUT.enabled");
        final String d = this.config.getString("Messages.STATUT.disabled");
        if (event.isVanish()) {
            player.sendMessage(Utils.color(this.config.getString("Messages.VANISH").replaceAll("%STATUT%", e)));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 3));
        }
        else {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.sendMessage(Utils.color(this.config.getString("Messages.VANISH").replaceAll("%STATUT%", d)));
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        /*
        final Player player = event.getPlayer();
        Player[] onlinePlayers;
        for (int length = (onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0])).length, i = 0; i < length; ++i) {
            final Player pls = onlinePlayers[i];
            if (VanishEvent.vanished.contains(pls) && !player.hasPermission("ModMode.Vanish")) {
                player.hidePlayer(pls);
            }
        }
        */
    }

    @EventHandler
    public void onPickUp(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        if (VanishEvent.vanished.contains(player)) {
            event.setCancelled(true);
        }
    }
}
