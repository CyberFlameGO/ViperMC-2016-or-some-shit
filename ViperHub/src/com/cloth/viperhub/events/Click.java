package com.cloth.viperhub.events;

import com.cloth.viperhub.ViperHub;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click implements Listener 
{

	ViperHub plugin;
	public Click(ViperHub instance)
	{
		this.plugin = instance;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e)
	{		
		if(e.getWhoClicked() instanceof Player)
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			p.updateInventory();
			return;
		}
	}
}
