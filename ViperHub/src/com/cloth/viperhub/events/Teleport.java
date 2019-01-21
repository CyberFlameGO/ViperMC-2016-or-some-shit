package com.cloth.viperhub.events;

import com.cloth.viperhub.ViperHub;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class Teleport implements Listener 
{

	ViperHub plugin;
	public Teleport(ViperHub instance)
	{
		this.plugin = instance;
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e)
	{
		if(e.getCause() == TeleportCause.ENDER_PEARL)
		{
			e.setCancelled(true);
			return;
		}
	}
}
