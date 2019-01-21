package com.viperhcf.minssentials.commands;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandFly implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Must Be Player");
			return true;
		}
		Player player = (Player) sender;
		if(args.length <= 0){
			player.setAllowFlight(!player.getAllowFlight());
			Command.broadcastCommandMessage(sender, "Toggled Flight " + (player.getAllowFlight() ? "on" : "off"));
		}else{
			float speed = 0.0F;
			if(NumberUtils.isDigits(args[0])){
				speed = Integer.valueOf(args[0]) / 10.0F;
			}
			speed = Math.max(-1, Math.min(1, speed));
			Command.broadcastCommandMessage(sender, "Toggled Flight Speed To " + ((int)(speed * 10)));
			player.setFlySpeed(speed);
		}


		return true;
	}


}
