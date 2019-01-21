package com.viperhcf.minssentials.commands;

import java.util.List;

import net.libhalt.bukkit.kaede.utils.FactionsUtils;
import net.syuu.popura.faction.FactionRole;
import net.syuu.popura.faction.FactionType;
import net.syuu.popura.faction.bean.Faction;
import net.syuu.popura.faction.database.FactionDatabase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.viperhcf.minssentials.Minssentials;


public class CommandNear implements CommandExecutor{

	private String nearbyPrefix;
	private String noplayernearby;

	public CommandNear(Minssentials plugin){
		nearbyPrefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("near-prefix"));
		noplayernearby = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("near-nobody"));
		if(plugin.getServer().getPluginManager().getPermission("minssentials.nearbypass") == null){
			plugin.getServer().getPluginManager().addPermission(new Permission("minssentials.nearbypass" , PermissionDefault.OP));
		}

	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Must Be Player");
			return true;
		}

		Player player = (Player) sender;
		if(FactionsUtils.getFactionAt(player.getLocation()) != null)
		{
			if(FactionsUtils.getFactionAt(player.getLocation()).getName().equals("Spawn"))
			{
				player.sendMessage(ChatColor.GRAY + "You cannot execute that command in spawn.");
				return true;
			}
		}

		List<String> nearByPlayers = Lists.newArrayList();
		for(Entity entity : player.getNearbyEntities(5.0D, 5.0D, 5.0D)){
			if(entity instanceof Player){
				Player near = (Player) entity;
				if(near.hasPermission("minssentials.nearbypass")){
					continue;
				}
				nearByPlayers.add(near.getName());
			}
		}
		if(nearByPlayers.isEmpty()){
			player.sendMessage(noplayernearby);
		}else{
			player.sendMessage(nearbyPrefix  + Joiner.on(", ").join(nearByPlayers));
		}
		return true;
	}


}
