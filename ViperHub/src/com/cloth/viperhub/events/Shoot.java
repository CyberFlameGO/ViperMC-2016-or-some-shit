package com.cloth.viperhub.events;

import com.cloth.viperhub.ViperHub;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Shoot implements Listener 
{

	ViperHub plugin;
	public Shoot(ViperHub instance)
	{
		this.plugin = instance;
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e)
	{
		if(e.getEntity() instanceof Arrow)
		{
			Arrow arrow = (Arrow) e.getEntity();
			if(arrow.getShooter() instanceof Player)
			{
				Player shooter = (Player) arrow.getShooter();
				float originalyaw = shooter.getLocation().getYaw();
				float originalpitch = shooter.getLocation().getPitch();
				Location arrowblock = e.getEntity().getLocation();
				Location teleport = new Location(arrowblock.getWorld(), arrowblock.getX(), arrowblock.getY(), arrowblock.getZ(), originalyaw, originalpitch);
				shooter.teleport(teleport);
			}
		}
	}
}
