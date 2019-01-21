package com.viperhcf.minssentials.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;


public class CommandClearEntity implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		int count = 0;
		for(World world : Bukkit.getWorlds()){
			for(Entity entity : world.getEntities()){
				if(entity instanceof Item || entity instanceof Monster || entity instanceof Projectile	){
					if(entity.getType() != EntityType.ENDER_PEARL){
						entity.remove();
						count++;
					}
				}
			}
		}
		sender.sendMessage("Cleared " + count + " entities");
		return true;
	}


}
