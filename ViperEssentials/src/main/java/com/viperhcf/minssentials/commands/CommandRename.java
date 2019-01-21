package com.viperhcf.minssentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Joiner;
import com.viperhcf.minssentials.utils.ItemStackUtils;


public class CommandRename implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Must Be Player");
			return true;
		}
		Player player = (Player) sender;
		ItemStack itemStack = player.getItemInHand();
        if(itemStack != null && itemStack.getType() != Material.AIR){
            ItemStackUtils.setItemTitle(itemStack, ChatColor.translateAlternateColorCodes('&', Joiner.on(" ").join(args)));
            ItemStackUtils.updateInventory(player);
        }else{
        	sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7(&aViper&7) &7You must be holding an item if youd like to rename it."));
        }
		return true;
	}


}
