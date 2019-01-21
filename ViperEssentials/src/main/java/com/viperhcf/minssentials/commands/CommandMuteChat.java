package com.viperhcf.minssentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import com.viperhcf.minssentials.Minssentials;


public class CommandMuteChat implements CommandExecutor , Listener{

	private boolean muted;

	private String messageMuted;
	private String messageUnMuted;
	private String messageCurrentlyMuted;
	
	public boolean isMuted(){
		return muted;
	}
	public CommandMuteChat(Minssentials plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		messageMuted = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chat-muted"));
		messageUnMuted = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chat-unmuted"));
		messageCurrentlyMuted = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chat-currently-muted"));
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event){
		if(muted && !event.getPlayer().hasPermission("minssentials.mutechat.bypass")) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(messageCurrentlyMuted);
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		muted = !muted;
		if(muted){
			Bukkit.broadcastMessage(messageMuted.replace("%who%" , sender.getName()));
		}else{
			Bukkit.broadcastMessage(messageUnMuted.replace("%who%" , sender.getName()));
		}

		return true;
	}


}
