package com.igodlik3.modmode.manager;

import org.bukkit.scheduler.*;
import org.bukkit.configuration.*;
import com.igodlik3.modmode.*;
import org.bukkit.entity.*;
import com.igodlik3.modmode.utils.*;
import org.bukkit.*;
import java.util.*;

public class FreezeManager extends BukkitRunnable
{
    private Set<UUID> frozenPlayers;
    private Configuration config;
    
    public FreezeManager() {
        this.frozenPlayers = new HashSet<UUID>();
        this.config = (Configuration)ModMode.getInstance().getConfig();
    }
    
    public boolean isFrozen(final Player player) {
        return this.frozenPlayers.contains(player.getUniqueId());
    }
    
    public String freezePlayer(final Player player) {
        if (this.isFrozen(player)) {
            this.getFrozenPlayers().remove(player.getUniqueId());
            return Utils.color(this.config.getString("Messages.UN-FROZEN").replaceAll("%PLAYER%", player.getName()));
        }
        this.getFrozenPlayers().add(player.getUniqueId());
        return Utils.color(this.config.getString("Messages.FROZEN").replaceAll("%PLAYER%", player.getName()));
    }
    
    public void run() {
        for (final UUID uuid : this.frozenPlayers) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                final String Line1 = Utils.color(this.config.getString("Messages.MOD-ITEMS.FREEZE.MESSAGE.1"));
                final String Line2 = Utils.color(this.config.getString("Messages.MOD-ITEMS.FREEZE.MESSAGE.2"));
                final String Line3 = Utils.color(this.config.getString("Messages.MOD-ITEMS.FREEZE.MESSAGE.3"));
                final String Line4 = Utils.color(this.config.getString("Messages.MOD-ITEMS.FREEZE.MESSAGE.4"));
                final String Line5 = Utils.color(this.config.getString("Messages.MOD-ITEMS.FREEZE.MESSAGE.5"));
                player.sendMessage(ChatColor.WHITE + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588 " + Line1);
                player.sendMessage(ChatColor.WHITE + "\u2588\u2588\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588\u2588\u2588\u2588");
                player.sendMessage(ChatColor.WHITE + "\u2588\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.GOLD + ChatColor.BLACK + "\u2588" + ChatColor.GOLD + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588\u2588\u2588 " + Line2);
                player.sendMessage(ChatColor.WHITE + "\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.BLACK + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588\u2588 " + Line3);
                player.sendMessage(ChatColor.WHITE + "\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.BLACK + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588\u2588 " + Line4);
                player.sendMessage(ChatColor.WHITE + "\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.BLACK + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588\u2588 " + Line5);
                player.sendMessage(ChatColor.WHITE + "\u2588" + ChatColor.RED + "\u2588" + ChatColor.GOLD + "\u2588\u2588\u2588" + ChatColor.BLACK + ChatColor.GOLD + "\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588 ");
                player.sendMessage(ChatColor.RED + "\u2588" + ChatColor.GOLD + "\u2588\u2588\u2588" + ChatColor.BLACK + "\u2588" + ChatColor.GOLD + "\u2588\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE);
                player.sendMessage(ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
                player.sendMessage(ChatColor.WHITE + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
            }
        }
    }
    
    public Set<UUID> getFrozenPlayers() {
        return this.frozenPlayers;
    }
}
