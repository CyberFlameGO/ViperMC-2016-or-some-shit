package com.cloth.viperhub.events;

import com.cloth.viperhub.ViperHub;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;


public class Connect implements Listener 
{

	ViperHub plugin;
	public Connect(ViperHub instance)
	{
		this.plugin = instance;
	}
	

	@EventHandler
	public void onConnect(PlayerJoinEvent e)
	{
		final Player p = e.getPlayer();

		new BukkitRunnable()
		{
			public void run()
			{
				    p.setAllowFlight(true);
			        p.setFlying(false);
				    
					int bow_slot = plugin.bow_slot;
					String bow_name = plugin.bow_name;
					ItemStack bow = new ItemStack(Material.BOW);
					ItemMeta bowmeta = bow.getItemMeta();
					bowmeta.setDisplayName(bow_name);
					bow.setItemMeta(bowmeta);	
					
					int enderpearl_slot = plugin.enderpearl_slot;
					String enderpearl_name = plugin.enderpearl_name;
					ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL);
					ItemMeta enderpearlmeta = enderpearl.getItemMeta();
					enderpearlmeta.setDisplayName(enderpearl_name);
					enderpearl.setItemMeta(enderpearlmeta);	
					
					p.getInventory().setItem(bow_slot, bow);
					p.getInventory().setItem(enderpearl_slot, enderpearl);
			}
		}.runTaskLater(plugin, 10);
	}
}
