package com.cloth.viperhub.events;

import com.cloth.viperhub.ViperHub;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.ArrayList;
import java.util.UUID;

public class Dismount implements Listener 
{

	ViperHub plugin;
	public Dismount(ViperHub instance)
	{
		this.plugin = instance;
	}

	@EventHandler
	public void onDismount(EntityDismountEvent e)
	{
		Location location = e.getDismounted().getLocation();
		location.setY(location.getY() + 2);

		if(e.getDismounted().getType() == EntityType.ENDER_PEARL)
		{
			e.getDismounted().remove();
			e.getDismounted().teleport(location);
		}
	}
}
