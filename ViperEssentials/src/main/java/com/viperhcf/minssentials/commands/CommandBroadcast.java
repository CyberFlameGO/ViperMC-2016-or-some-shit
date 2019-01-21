package com.viperhcf.minssentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Joiner;


public class CommandBroadcast implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&' , "&d[SERVER] " + Joiner.on(" ").join(args)));
		return true;
	}
}
