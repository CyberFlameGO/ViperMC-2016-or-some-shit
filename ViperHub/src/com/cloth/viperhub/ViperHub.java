package com.cloth.viperhub;

import com.cloth.viperhub.events.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class ViperHub extends JavaPlugin 
{
	public String bow_name = getConfig().getString("bow_name").replaceAll("&", "§");
	public int bow_slot = getConfig().getInt("bow_slot");
	
	public String enderpearl_name = getConfig().getString("enderpearl_name").replaceAll("&", "§");
	public int enderpearl_slot = getConfig().getInt("enderpearl_slot");

	public ArrayList<UUID> flying = new ArrayList<UUID>();

	public void onEnable()
	{		
		getConfig().options().copyDefaults(true);
		saveConfig();
		registerEvents();
	}
	
	public void registerEvents()
	{
		//getServer().getPluginManager().registerEvents(new Move(this), this);
		//getServer().getPluginManager().registerEvents(new Dismount(this), this);
		//getServer().getPluginManager().registerEvents(new Teleport(this), this);
		getServer().getPluginManager().registerEvents(new Damage(this), this);
		//getServer().getPluginManager().registerEvents(new Interact(this), this);
		getServer().getPluginManager().registerEvents(new DoubleJump(this), this);
		getServer().getPluginManager().registerEvents(new ItemPickup(this), this);
		getServer().getPluginManager().registerEvents(new ItemDamage(this), this);
		getServer().getPluginManager().registerEvents(new Click(this), this);
		//getServer().getPluginManager().registerEvents(new Connect(this), this);
		getServer().getPluginManager().registerEvents(new ItemDrop(this), this);
		//getServer().getPluginManager().registerEvents(new Shoot(this), this);
	}
	
	public ItemStack getBow()
	{
		String bow_name = this.bow_name;
		ItemStack bow = new ItemStack(Material.BOW);
		ItemMeta bowmeta = bow.getItemMeta();
		bowmeta.setDisplayName(bow_name);
		bow.setItemMeta(bowmeta);	
		return bow;
	}
	
	public ItemStack getPearl()
	{
		String enderpearl_name = this.enderpearl_name;
		ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL);
		ItemMeta enderpearlmeta = enderpearl.getItemMeta();
		enderpearlmeta.setDisplayName(enderpearl_name);
		enderpearl.setItemMeta(enderpearlmeta);	
		return enderpearl;
	}
}
