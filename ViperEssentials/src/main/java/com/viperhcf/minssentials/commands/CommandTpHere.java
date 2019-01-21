package com.viperhcf.minssentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class CommandTpHere implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Must Be Player");
			return true;
		}
		Player player = (Player) sender;
		Player target = Bukkit.getPlayerExact(args[args.length - 1]);
		if (target == null) {
			sender.sendMessage("Can't find player " + args[args.length - 1] + ". No tp.");
			return true;
		}
		target.teleport(player, TeleportCause.COMMAND);
		Command.broadcastCommandMessage(sender, "Teleported " + target.getDisplayName() + " to " + player.getDisplayName());
		return true;
	}


}
