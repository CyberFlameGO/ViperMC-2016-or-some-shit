package com.viperhcf.minssentials;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Maps;

public class PlayerStorage implements Listener{

	public static PlayerStorage getInstance(){
		
		return INSTANCE;
	}
	
	private static PlayerStorage INSTANCE = new PlayerStorage();
	
	private Map<UUID , StoredData> storedData = Maps.newHashMap();
	
	private JavaPlugin plugin;
	private File workingDir;
	private String message;
	public void prepare(JavaPlugin plugin){
		message = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("first-join-message"));
		
		this.plugin = plugin;
		workingDir = new File(plugin.getDataFolder() , "player");
		workingDir.mkdirs();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public StoredData getData(Player player){
		return storedData.get(player.getUniqueId());
	}
	@EventHandler
	public void onPlayerJoin(PlayerQuitEvent event){
		final Player player = event.getPlayer();
		final StoredData data = storedData.remove(player.getUniqueId());
		if(data != null){
			new BukkitRunnable() {
				@Override
				public void run() {
					data.save(new File(workingDir , player.getUniqueId().toString() + ".yml"));
				}
			}.runTaskAsynchronously(plugin);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		final Player player = event.getPlayer();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				File file = new File(workingDir , player.getUniqueId().toString() + ".yml");
				boolean broadcast = !file.exists();
				
				final StoredData data = file.exists() ?  new StoredData(YamlConfiguration.loadConfiguration(file)) : new StoredData(null);
				data.save(new File(workingDir , player.getUniqueId().toString() + ".yml"));
				if(broadcast){
					//Bukkit.broadcastMessage(message.replace("%who%", player.getName()).replace("%count%", String.valueOf(workingDir.listFiles().length)));
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						if(player.isOnline()){
							storedData.put(player.getUniqueId(),data );
						}
					}
				}.runTask(plugin);
			}
		}.runTaskAsynchronously(plugin);
	}
}
