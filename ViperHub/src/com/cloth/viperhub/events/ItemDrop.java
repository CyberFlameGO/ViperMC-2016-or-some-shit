package com.cloth.viperhub.events;

import com.cloth.viperhub.ViperHub;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDrop implements Listener 
{

	ViperHub plugin;
	public ItemDrop(ViperHub instance){
		this.plugin = instance;
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e)
	{
		if(e.getItemDrop().getItemStack() != null)
		{
			if(e.getItemDrop().getItemStack().hasItemMeta())
			{
				if(e.getItemDrop().getItemStack().getItemMeta().hasDisplayName())
				{
					if(e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(plugin.bow_name))
					{
						e.setCancelled(true);
						return;
					}
					if(e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(plugin.enderpearl_name))
					{
						e.setCancelled(true);
						return;
					}
				}
			}
		}
		return;		
	}
}
