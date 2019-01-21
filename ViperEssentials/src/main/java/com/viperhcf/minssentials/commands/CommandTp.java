package com.viperhcf.minssentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.viperhcf.minssentials.Minssentials;


public class CommandTp implements CommandExecutor{

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
		Player player = (Player) sender;
		Player from = player;
		Player to = Minssentials.getPlayer(args[0]);
		if(args.length > 1){
			to = Minssentials.getPlayer(args[1]);;
			from = Minssentials.getPlayer(args[0]);
		}
		if(from == null || to == null){
			player.sendMessage("That player could not be found");
			return true;
		}
		from.teleport(to, TeleportCause.COMMAND);
		Command.broadcastCommandMessage(sender, "Teleported " + from.getDisplayName() + " to " + to.getDisplayName());
		return true;
	}


}
