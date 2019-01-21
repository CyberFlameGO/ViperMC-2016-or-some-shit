package com.cloth.viperhub.events;

import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.*;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftEntityEquipment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.cloth.viperhub.ViperHub;

public class Interact implements Listener 
{
	
	ViperHub plugin;
	public Interact(ViperHub instance)
	{
		this.plugin = instance;
	}
	
	
	@SuppressWarnings({"unused", "deprecation"})
	@EventHandler
	void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) 
		{
			if (e.getPlayer().getItemInHand() != null)
			{
				if (e.getPlayer().getItemInHand().getType() == Material.ENDER_PEARL) 
				{
					e.setCancelled(true);
					e.getPlayer().updateInventory();
					EnderPearl pearl = e.getPlayer().launchProjectile(EnderPearl.class);
					e.getPlayer().spigot().setCollidesWithEntities(false);
					pearl.setVelocity(e.getPlayer().getLocation().getDirection());
					pearl.setPassenger(e.getPlayer());
					if(!plugin.flying.contains(e.getPlayer().getUniqueId()))
					{
						plugin.flying.add(e.getPlayer().getUniqueId());
					}
				}

				if (e.getPlayer().getItemInHand().getType() == Material.BOW)
				{
					e.setCancelled(true);
					Arrow arrow = e.getPlayer().shootArrow();
					for(Player on : Bukkit.getServer().getOnlinePlayers())
					{
						PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(arrow.getEntityId());
						((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
					}
				}
			}
		}
	}
}
