package com.viperhcf.minssentials.commands;

import java.util.Map;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import com.google.common.collect.Maps;
import com.viperhcf.minssentials.Minssentials;


public class CommandSlowChat implements CommandExecutor , Listener{
	
	private boolean slowed;
	private int interval;
	private Map<Player , Long> mutedUntil = Maps.newHashMap();
	private String messageSlowed;
	private String messageUnSlowed;
	private String messageCurrentlySlowed;
	
	
	public boolean isSlowed(){
		return slowed;
	}
	
	public int getInteerval(){
		return interval;
	}
	public long getMutedFor(Player player){
		if(mutedUntil.containsKey(player)){
			long until =  mutedUntil.get(player);
			if(System.currentTimeMillis() > until){
				return -1L;
			}else{
				return until - System.currentTimeMillis();
			}
		}else{
			return -1L;
		}
	}
	public CommandSlowChat(Minssentials plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		messageSlowed = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chat-slowed"));
		messageUnSlowed = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chat-unslowed"));
		messageCurrentlySlowed = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chat-currently-slowed"));
	}
	@EventHandler(ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event){
		if(slowed){
			Player player = event.getPlayer();
			if(!player.hasPermission("minssentials.slowchat.bypass")){
				if(mutedUntil.containsKey(player)){
					long until =  mutedUntil.get(player);
					if(System.currentTimeMillis() > until){
						mutedUntil.put(player, System.currentTimeMillis() + (interval * 1000L));
					}else{
						event.setCancelled(true);
						event.getPlayer().sendMessage(messageCurrentlySlowed.replace("%time%", DurationFormatUtils.formatDurationWords(until - System.currentTimeMillis(), true	, true)));
					}
				}else{
					mutedUntil.put(player, System.currentTimeMillis() + (interval * 1000L));
				}
			}
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		slowed = !slowed;
		if(slowed){
			int delay = 5;
			if(args.length > 0){
				try{
					delay = Integer.valueOf(args[0]);
				}catch(NumberFormatException e){
					sender.sendMessage("Invalid Number");
					return true;
				}
			}
			interval = delay;
			Bukkit.broadcastMessage(messageSlowed.replace("%who%" , sender.getName()));
		}else{
			Bukkit.broadcastMessage(messageUnSlowed.replace("%who%" , sender.getName()));
		}

		return true;
	}


}
