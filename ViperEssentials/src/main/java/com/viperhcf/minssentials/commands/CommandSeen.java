package com.viperhcf.minssentials.commands;

import java.time.Duration;
import java.util.Map;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Maps;
import com.viperhcf.minssentials.Minssentials;


public class CommandSeen implements CommandExecutor, Listener{

	private Map<Player , Long> loggedonTime = Maps.newHashMap();
	private String currentlyOnline;
	private String previousOnline;
	private String neverOnline;
	public CommandSeen(Minssentials plugin){
		plugin.getServer().getPluginManager().registerEvents( this, plugin);
		for(Player player : Bukkit.getOnlinePlayers()){
			loggedonTime.put(player, System.currentTimeMillis());
		}
		currentlyOnline = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("seen-current-online"));
		previousOnline = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("seen-previous-online"));
		neverOnline = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("seen-never-online"));

	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		loggedonTime.put(event.getPlayer(), System.currentTimeMillis());
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		loggedonTime.remove(event.getPlayer());
	}
	@Override
	public boolean onCommand(final CommandSender sender, Command arg1, String arg2, final String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Must Be Player");
			return true;
		}
		if(args.length <= 0){
			sender.sendMessage("You must specify the player.");
			return true;
		}
		Player target = Bukkit.getPlayer(args[0]);
		if(target != null){
			long value = loggedonTime.get(target);
			sender.sendMessage(currentlyOnline.replace("%name%", target.getName()).replace("%duration%", DurationFormatUtils.formatDurationWords(System.currentTimeMillis() - value, true, true)));
			return true;
		}
		new BukkitRunnable() {
			
			@Override
			public void run() {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if(!offlinePlayer.hasPlayedBefore()){
					sender.sendMessage(neverOnline);
					return;
				}
				sender.sendMessage(previousOnline.replace("%name%", offlinePlayer.getName()) .replace("%duration%",  DurationFormatUtils.formatDurationWords(System.currentTimeMillis() - offlinePlayer.getLastPlayed(), true, true)));
			}
		}.runTaskAsynchronously(JavaPlugin.getProvidingPlugin(CommandSeen.class));
		return true;
	}


}
