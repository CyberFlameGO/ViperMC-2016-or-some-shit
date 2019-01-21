package com.cloth.viperhub.events;

import com.cloth.viperhub.ViperHub;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ItemPickup implements Listener 
{

	ViperHub plugin;
	public ItemPickup(ViperHub instance)
	{
		this.plugin = instance;
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e)
	{
		e.setCancelled(true);
		return;
	}
}
