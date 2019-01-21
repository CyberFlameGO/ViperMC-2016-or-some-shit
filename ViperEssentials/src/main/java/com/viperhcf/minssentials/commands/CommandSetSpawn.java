package com.viperhcf.minssentials.commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.viperhcf.minssentials.Minssentials;


public class CommandSetSpawn implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Must Be Player");
			return true;
		}
		Player player = (Player) sender;
		Location location = player.getLocation();
		player.getWorld().setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		Minssentials plugin = Minssentials.getPlugin(Minssentials.class);
		plugin.getConfig().set("spawn-yaw-" + player.getLocation().getWorld().getName(), player.getLocation().getYaw());
		plugin.getConfig().set("spawn-pitch-" + player.getLocation().getWorld().getName(), player.getLocation().getPitch());
		try {
			plugin.getConfig().save(new File(plugin.getDataFolder() , "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Command.broadcastCommandMessage(sender, "Set spawn for " +player.getWorld().getName() +" at " + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ());
		return true;
	}


}
