package com.cloth.viperhub.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

import com.cloth.viperhub.ViperHub;

public class ItemDamage implements Listener 
{

	ViperHub plugin;
	public ItemDamage(ViperHub instance)
	{
		this.plugin = instance;
	}
	
	@EventHandler
	public void onDamage(PlayerItemDamageEvent e)
	{
		e.setCancelled(true);
		e.getPlayer().updateInventory();
		return;
	}
}
