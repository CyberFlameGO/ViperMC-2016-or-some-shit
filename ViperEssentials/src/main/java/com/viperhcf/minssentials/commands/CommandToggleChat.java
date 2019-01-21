package com.viperhcf.minssentials.commands;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.Sets;
import com.viperhcf.minssentials.Minssentials;


public class CommandToggleChat implements CommandExecutor , Listener{

	private Set<UUID> disabled = Sets.newHashSet();
	private String globalChatDisabled;
	private String globalChatEnabled;
	public CommandToggleChat(Minssentials plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		globalChatDisabled = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("global-chat-disabled"));
		globalChatEnabled = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("global-chat-enabled"));
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		disabled.remove(event.getPlayer().getUniqueId());
	}
	
	@EventHandler(ignoreCancelled = true , priority = EventPriority.MONITOR)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event){
		if(event.getPlayer().hasPermission("minssentials.staffview")){
			return;
		}
		
		Iterator<Player> recipents = event.getRecipients().iterator();
		while(recipents.hasNext()){
			if(disabled.contains(recipents.next().getUniqueId())){
				recipents.remove();
			}
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(disabled.contains(((Player)sender).getUniqueId())){
			disabled.remove(((Player)sender).getUniqueId());
			sender.sendMessage(globalChatEnabled);
		}else{
			disabled.add(((Player)sender).getUniqueId());
			sender.sendMessage(globalChatDisabled);
		}

		return true;
	}


}
