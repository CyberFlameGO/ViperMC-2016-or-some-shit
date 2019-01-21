package com.viperhcf.minssentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.viperhcf.minssentials.Minssentials;

import java.util.HashMap;
import java.util.UUID;


public class CommandSpawn implements CommandExecutor{
	
	private Minssentials plugin;
	
	public CommandSpawn(Minssentials plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Must Be Player");
			return true;
		}

		Player player = (Player) sender;
		Location location = Bukkit.getWorlds().get(0).getSpawnLocation();
		location.setYaw((float) plugin.getConfig().getDouble("spawn-yaw-" + location.getWorld().getName()));
		location.setPitch((float) plugin.getConfig().getDouble("spawn-pitch-" + location.getWorld().getName()));
		player.teleport(location);
		return true;
	}
}
