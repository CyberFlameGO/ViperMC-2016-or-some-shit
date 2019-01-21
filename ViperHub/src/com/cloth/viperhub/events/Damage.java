package com.cloth.viperhub.events;

import com.cloth.viperhub.ViperHub;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Damage implements Listener 
{

	ViperHub plugin;
	public Damage(ViperHub instance)
	{
		this.plugin = instance;
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e)
	{
		e.setCancelled(true);
		return;
	}
}
