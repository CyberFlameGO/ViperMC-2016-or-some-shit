package com.viperhcf.minssentials.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.viperhcf.minssentials.Minssentials;


public class CommandClearChat implements CommandExecutor{

	private String message;
	public CommandClearChat(Minssentials plugin){
		message = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chat-cleared"));
	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		for(Player player : Bukkit.getOnlinePlayers()){
			for(int i = 0 ; i < 125 ; i++){
				player.sendMessage("");
			}
		}
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(message.replace("%who%", sender.getName()));
		return true;
	}


}
