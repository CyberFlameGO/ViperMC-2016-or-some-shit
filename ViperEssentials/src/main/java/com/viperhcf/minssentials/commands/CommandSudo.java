package com.viperhcf.minssentials.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.google.common.base.Joiner;


public class CommandSudo implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {

		if(!(sender instanceof Player)){
			sender.sendMessage("Must Be Player");
			return true;
		}

		if(args.length == 0)
		{
			sender.sendMessage(ChatColor.GRAY + "Did you mean: " + ChatColor.WHITE + "/sudo <player>");
			return true;
		}

		Player player = (Player) sender;
		Player target = Bukkit.getPlayerExact(args[0]);

		if (target == null || player == target) {
			sender.sendMessage("Can't find player " + args[0] + ". No sudo.");
			return true;
		}
		if(target.hasPermission("minssentials.sudo")){
			sender.sendMessage("Can not do this to this person.");
			return true;
		}

		target.chat(Joiner.on(" ").join(Arrays.copyOfRange(args  , 1, args.length) ));
		Command.broadcastCommandMessage(sender, "Sudo-ed " + target.getDisplayName() + " to " +Joiner.on(" ").join (Arrays.copyOfRange(args  , 1, args.length)) );
		return true;
	}


}
