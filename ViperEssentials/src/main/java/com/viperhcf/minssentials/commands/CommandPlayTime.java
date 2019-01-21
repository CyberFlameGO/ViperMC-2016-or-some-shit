package com.viperhcf.minssentials.commands;

import java.time.Duration;
import java.util.Map;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
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


public class CommandPlayTime implements CommandExecutor, Listener{

	public CommandPlayTime(Minssentials plugin){
		plugin.getServer().getPluginManager().registerEvents( this, plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
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
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lVIPER &8 &e%name% &7has been playing for &e".replace("%name%", target.getName()) + DurationFormatUtils.formatDurationWords(target.getStatistic(Statistic.PLAY_ONE_TICK) * 50, true, true)));
			return true;
		}
		sender.sendMessage("No Such Player online");
		return true;
	}


}
