package com.cloth.viperhub.events;

import com.cloth.viperhub.ViperHub;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class DoubleJump implements Listener
{

	ViperHub plugin;
	public DoubleJump(ViperHub instance)
	{
		this.plugin = instance;
	}
	
	@EventHandler
	public void playerMove(PlayerMoveEvent e)
	{
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE
				&& e.getPlayer().getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() != Material.AIR
				&& !e.getPlayer().isFlying())
		{			
			e.getPlayer().setAllowFlight(true);
		}
	}
	
	@EventHandler
	public void toggleFlight(PlayerToggleFlightEvent e)
	{		
		if(e.getPlayer().getGameMode() == GameMode.CREATIVE)
		{
			return;
		}
		
		e.setCancelled(true);
		e.getPlayer().setFlying(false);
		e.getPlayer().setAllowFlight(false);
		e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(2).setY(1));
		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);
	}
}
