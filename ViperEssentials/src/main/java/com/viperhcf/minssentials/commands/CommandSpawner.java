package com.viperhcf.minssentials.commands;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.viperhcf.minssentials.utils.LocationUtil;
import com.viperhcf.minssentials.utils.Mob;

public class CommandSpawner implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Must Be Player");
			return true;
		}
		Player player = (Player) sender;
		final Location target = LocationUtil.getTarget(player);
		if (target == null || target.getBlock().getType() != Material.MOB_SPAWNER)
		{
			player.sendMessage("You must be looking at mob spawner");
			return true;
		}
		String name = args[0];
		int delay = 0;

		Mob mob = Mob.fromName(name);
		if(mob == null){
			player.sendMessage(ChatColor.RED + "Invalid Mob Type");
			return true;
		}
		if (args.length > 1)
		{
			if (NumberUtils.isDigits(args[1]))
			{
				delay = Integer.parseInt(args[1]);
			}
		}

		CreatureSpawner spawner = (CreatureSpawner)target.getBlock().getState();
		spawner.setSpawnedType(mob.getType());
		spawner.setDelay(delay);
		spawner.update();
		return true;
	}

	
}
