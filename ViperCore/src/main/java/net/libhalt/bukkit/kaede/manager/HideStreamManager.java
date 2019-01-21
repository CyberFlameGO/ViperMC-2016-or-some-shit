package net.libhalt.bukkit.kaede.manager;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HideStreamManager extends Manager implements Listener{

	public HideStreamManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
		
	}

	
	@EventHandler
	public void onCraft(PlayerJoinEvent event){
		event.setJoinMessage(null);
	}
	@EventHandler
	public void onCraft(PlayerQuitEvent event){
		event.setQuitMessage(null);
	}



}
