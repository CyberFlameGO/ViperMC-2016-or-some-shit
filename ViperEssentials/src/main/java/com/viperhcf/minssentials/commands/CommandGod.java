package com.viperhcf.minssentials.commands;

import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import com.google.common.collect.Sets;
import com.viperhcf.minssentials.Minssentials;


public class CommandGod implements CommandExecutor , Listener{

	private Set<Player> godModes = Sets.newHashSet();

	public CommandGod(Minssentials plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event){
		if(godModes.contains(event.getEntity())){
			event.setCancelled(true);
		}

		if(godModes.contains(event.getDamager())){
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		if(godModes.contains(event.getEntity())){
			event.setCancelled(true);
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Must Be Player");
			return true;
		}
		Player player = (Player) sender;

		boolean toggledOn = false;
		if(godModes.contains(player)){
			godModes.remove(player);
		}else{
			godModes.add(player);
			toggledOn = true;
		}
		
		Command.broadcastCommandMessage(sender, "Toggled God Mode " + (toggledOn ? "On" : "Off"));

		return true;
	}


}
