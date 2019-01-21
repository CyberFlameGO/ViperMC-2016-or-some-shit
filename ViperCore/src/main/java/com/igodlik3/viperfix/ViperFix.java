package com.igodlik3.viperfix;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.Manager;
import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.claim.Position2D;
import net.syuu.popura.faction.bean.ClaimedRegion;
import net.syuu.popura.faction.bean.Faction;
import net.syuu.popura.faction.bean.FactionPlayer;
import org.bukkit.plugin.java.*;
import org.bukkit.plugin.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.*;

public class ViperFix extends Manager implements Listener
{
    public ViperFix(HCFactionPlugin plugin) {
        super(plugin);
    }

    public void init() {
        this.getPlugin().getServer().getPluginManager().registerEvents((Listener) this, getPlugin());
    }
    
    @EventHandler
    public void onEntityDamageEvent(final EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION && entity instanceof Player) {
            final Player player = (Player)entity;
            final Location loc = player.getLocation();
            player.teleport(loc);
        }
    }
    
    @EventHandler
    public void onVehiclePlace(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && player.getItemInHand() != null && player.getItemInHand().getType() == Material.MINECART) {
            final Location loc = event.getClickedBlock().getLocation();
            final ClaimedRegion flo = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(loc.getWorld().getName() , loc.getBlockX() , loc.getBlockZ()));
            if (flo != null) {
                final Faction faction = flo.getOwner();
                if (factionPlayer.getFaction() == null || factionPlayer.getFaction() != faction) {
                    player.sendMessage(ChatColor.RED + "You can only place minecart on your territory !");
                }
            }
            else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can only place minecart on your territory !");
            }
        }
    }
}
