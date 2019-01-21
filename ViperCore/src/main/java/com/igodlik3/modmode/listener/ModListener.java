package com.igodlik3.modmode.listener;

import org.bukkit.configuration.*;
import com.igodlik3.modmode.manager.*;
import com.igodlik3.modmode.*;
import com.igodlik3.modmode.utils.*;
import org.bukkit.command.*;
import java.util.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.projectiles.*;
import org.bukkit.scheduler.BukkitTask;

public class ModListener implements Listener
{
    private Configuration config;
    private FreezeManager manager;
    
    public ModListener() {
        this.config = (Configuration)ModMode.getInstance().getConfig();
        this.manager = ModMode.getInstance().getFreezeManager();
    }
    
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (this.config.getBoolean("Mod-Mode.Staff-LeaveJoin-Message") && player.hasPermission("ModMode")) {
            for(Player pls : Bukkit.getOnlinePlayers()){
                if (pls.hasPermission("ModMode")) {
                    pls.sendMessage(Utils.color(this.config.getString("Messages.STAFF-JOIN").replaceAll("%PLAYER%", player.getName())));
                }
            }
        }
        if (this.config.getBoolean("Mod-Mode.Toggle-ModMode-OnJoin")) {
            player.performCommand("mod");
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (this.config.getBoolean("Mod-Mode.Staff-LeaveJoin-Message") && player.hasPermission("Xeor.ModMode")) {
    for(Player pls : Bukkit.getOnlinePlayers()){
                if (pls.hasPermission("Xeor.ModMode")) {
                    pls.sendMessage(Utils.color(this.config.getString("Messages.STAFF-LEAVE").replaceAll("%PLAYER%", player.getName())));
                }
            }
        }
        if (this.manager.isFrozen(player) && this.config.getBoolean("Mod-Mode.Freeze.execute-cmd-on-disconnect")) {
            for (final String stg : this.config.getStringList("Mod-Mode.Freeze.cmds")) {
                Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), stg.replaceAll("%PLAYER%", player.getName()));
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player)event.getWhoClicked();
        if (this.isMod(player) && event.getClickedInventory() != null) {
            if (event.getClickedInventory().equals(player.getOpenInventory().getBottomInventory())) {
                event.setCancelled(true);
            }
            else if (this.config.getBoolean("Mod-Mode.Block-InventoryMove.Others-Inventory")) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onBlockPlace(final FoodLevelChangeEvent event) {
        final Player player = (Player) event.getEntity();
        if (this.isMod(player)) {
            event.setFoodLevel(20);
        }
    }
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if (this.isMod(player) && !event.isCancelled() && this.config.getBoolean("Mod-Mode.Block-Place")) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (this.isMod(player) && !event.isCancelled() && this.config.getBoolean("Mod-Mode.Block-Break")) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onItemDrop(final PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        if (this.isMod(player) && this.config.getBoolean("Mod-Mode.Block-ItemDrop")) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onItemDrop(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        if (this.isMod(player) && this.config.getBoolean("Mod-Mode.Block-PickUp")) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Location to = event.getTo();
        final Location from = event.getFrom();
        if (this.manager.isFrozen(player) && (to.getBlockX() != from.getBlockX() || to.getBlockZ() != from.getBlockZ())) {
            event.setTo(from);
        }
    }
    
    @EventHandler
    public void onEntityDamage(final EntityDamageByEntityEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof Player)) {
            return;
        }
        final Player damaged = (Player)event.getEntity();
        Entity eDamager = event.getDamager();
        if (eDamager instanceof Projectile) {
            final ProjectileSource projectileSource = (ProjectileSource)((Projectile)eDamager).getShooter();
            if (projectileSource instanceof Player) {
                eDamager = (Entity)projectileSource;
            }
        }
        if (!(eDamager instanceof Player)) {
            return;
        }
        final Player damager = (Player)eDamager;
        if (damager == damaged) {
            return;
        }
        if (this.manager.isFrozen(damager)) {
            event.setCancelled(true);
            damager.sendMessage(Utils.color("&cYou can't pvp whilst you're frozen !"));
            return;
        }
        if (this.manager.isFrozen(damaged)) {
            event.setCancelled(true);
            damager.sendMessage(Utils.color("&cThis player is frozen !"));
        }
    }
    
    private boolean isMod(final Player player) {
        return player.hasPermission("ModMode") && ModMode.getInstance().getModToggled().contains(player.getUniqueId());
    }
}
