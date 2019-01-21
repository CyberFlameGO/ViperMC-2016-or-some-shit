package com.viperhcf.minssentials.commands;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.viperhcf.minssentials.Minssentials;


public class CommandFeed implements CommandExecutor{

	private String feeded;
	public CommandFeed(Minssentials plugin) {
		feeded = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("feeded"));
	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(args.length <= 0){
			sender.sendMessage("/feed <player>");
			return true;
		}
		if(args[0].equalsIgnoreCase("*")){
			for(Player player : Bukkit.getOnlinePlayers()){
				player.setFoodLevel(20);
				player.sendMessage(feeded);
			}
		}else{
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null){
				sender.sendMessage(args[0] + " is offline");
				return true;
			}
			target.setFoodLevel(20);
			target.sendMessage(feeded);
			sender.sendMessage("§7(§aViper§7) You have fed §f" + target.getName() + "§7.");
		}
		return true;
	}


}
