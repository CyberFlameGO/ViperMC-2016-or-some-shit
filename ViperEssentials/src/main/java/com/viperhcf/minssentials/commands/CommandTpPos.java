package com.viperhcf.minssentials.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class CommandTpPos implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Must Be Player");
			return true;
		}
		if(args.length < 3){
			sender.sendMessage("You must specify x y z coords.");
			return true;
		}
		Player player = (Player) sender;
		final double x = args[0].startsWith("~") ? player.getLocation().getX() + Integer.parseInt(args[0].substring(1)) : Integer.parseInt(args[0]);
		final double y = args[1].startsWith("~") ? player.getLocation().getY() + Integer.parseInt(args[1].substring(1)) : Integer.parseInt(args[1]);
		final double z = args[2].startsWith("~") ? player.getLocation().getZ() + Integer.parseInt(args[2].substring(1)) : Integer.parseInt(args[2]);
		final Location loc = new Location(player.getWorld(), x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
		if (args.length > 3)
		{
			loc.setYaw((Float.parseFloat(args[3]) + 180 + 360) % 360);
		}
		if (args.length > 4)
		{
			loc.setPitch(Float.parseFloat(args[4]));
		}
		if (x > 30000000 || y > 30000000 || z > 30000000 || x < -30000000 || y < -30000000 || z < -30000000)
		{
			sender.sendMessage("Invalid Position. No tp.");
			return true;
		}
		player.teleport(loc, TeleportCause.COMMAND);
		Command.broadcastCommandMessage(sender, "Teleported " + player.getDisplayName() + " to " + x + ", " + y + ", " + z);
		return true;
	}


}
