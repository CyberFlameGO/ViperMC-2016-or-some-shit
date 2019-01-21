package com.viperhcf.minssentials.commands;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.viperhcf.minssentials.utils.Enchantments;


public class CommandGive implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(args.length < 0){
			sender.sendMessage(ChatColor.RED + "/give <name> <item>");
			return true;
		}
		Player target = Bukkit.getPlayerExact(args[0]);
		if (target == null) {
			sender.sendMessage("Can't find player " + args[0] + ". No give.");
			return true;
		}
		String type = args[1];
		short damage = 0;
		if(type.contains(":")){
			String[] split = type.split(":");
			type = split[0];
			damage = Short.valueOf(split[1]);
			if(!NumberUtils.isDigits(split[1])){
				sender.sendMessage("Invalid Damage: " + split[1]);
			}
		}
		ItemStack itemstack;
		if(NumberUtils.isDigits(type)){
			itemstack = new ItemStack(Material.getMaterial(Integer.valueOf(type)) , 1 , damage);
		}else{
			itemstack = approximateMatch(type , damage);
		}
		if(itemstack == null){
			sender.sendMessage("No Such Material. " + type);
			return true;
		}
		ItemMeta meta = itemstack.getItemMeta();
		if(args.length > 2){
			if(!NumberUtils.isDigits(args[2])){
				sender.sendMessage("Please specify amount");
				return true;
			}
			itemstack.setAmount(Integer.valueOf(args[2]));
			if(args.length > 2){
				for(int i = 3 ; i <  args.length ; i ++){
					if(!args[i].contains(":")){
						sender.sendMessage("Unknown Data " + args[i]);
						return true;
					}
					String leader = args[i].split(":")[0];
					String data = args[i].split(":")[1];
					if(leader.equalsIgnoreCase("name")){
						meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', data).replace("_",  " "));
					}else if (Enchantments.getByName(leader) != null && NumberUtils.isDigits(data)){
						if(meta instanceof EnchantmentStorageMeta){
							EnchantmentStorageMeta enchantmentStorage = (EnchantmentStorageMeta) meta;
							enchantmentStorage.addStoredEnchant(Enchantments.getByName(leader), Integer.valueOf(data), true);
						}else{
							meta.addEnchant(Enchantments.getByName(leader), Integer.valueOf(data), true);
						}
					}else{
						sender.sendMessage("Unknown Data " + args[i]);
						return true;
					}
				}
			}
		}
		itemstack.setItemMeta(meta);
		target.getInventory().addItem(itemstack);
		return true;
	}

	public ItemStack approximateMatch(String input ,short damage){
		input = input.replace("_", "").trim();
		for(Material type : Material.values()){
			if(type.name().replace("_", "").trim().equalsIgnoreCase(input)){
				return new ItemStack(type , 1 , damage);
			}
		}
		if(input.equalsIgnoreCase("endportalframe")){
			return new ItemStack(Material.ENDER_PORTAL_FRAME , 1 , damage);
		}
		if(input.equalsIgnoreCase("cobweb")){
			return new ItemStack(Material.WEB , 1 , damage);
		}
		if(input.equalsIgnoreCase("witherskull")){
			return new ItemStack(Material.SKULL_ITEM , 1 , (short) 1);
		}
		if(input.equalsIgnoreCase("gunpowder")){
			return new ItemStack(Material.SULPHUR , 1 , damage);
		}
		if(input.equalsIgnoreCase("steak")){
			return new ItemStack(Material.COOKED_BEEF , 1 , damage);
		}
		return null;
	}

}
