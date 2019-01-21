package net.libhalt.bukkit.kaede.utils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Preconditions;

public class ItemStackUtils {
	public static ItemStack setItemLore(ItemStack item, List<String> lore){
		Preconditions.checkNotNull(item);
		Preconditions.checkNotNull(lore);
		Preconditions.checkState(item.getType() != Material.AIR);
		org.bukkit.inventory.meta.ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		return item;
	}
	public static ItemStack setItemTitle(ItemStack item, String title){
		Preconditions.checkNotNull(item);
		Preconditions.checkNotNull(title);
		Preconditions.checkState(item.getType() != Material.AIR);
		org.bukkit.inventory.meta.ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(title);
		item.setItemMeta(itemMeta);
		return item;
	}


	public static boolean isEmpty(ItemStack item){
		return item == null || item.getType() == Material.AIR;
	}

	public static boolean isWearingFull(Player player , Armor armor){
		for(int i = 0 ; i < 4 ; i++){
			ItemStack item = player.getInventory().getArmorContents()[i];
			if(item == null){
				return false;
			}
			switch(armor){
			case CHAIN_MAIL:
				if(!isChainArmor(item.getType())){
					return false;
				}
				break;
			case GOLD:
				if(!isGoldenArmor(item.getType())){
					return false;
				}
				break;
			case DIAMOND:
				if(!isDiamondArmor(item.getType())){
					return false;
				}
				break;
			case IRON:
				if(!isIronArmor(item.getType())){
					return false;
				}
				break;
			case LEATHER:
				if(!isLeatherArmor(item.getType())){
					return false;
				}
				break;
			}
		}
		return true;
	}
	public static int getIndexOfArmor(Material material){
		Preconditions.checkState(isArmor(material));
		if(isHelmet(material)){
			return 3;
		}else if(isChestPlate(material)){
			return 2;
		}else if(isLegging(material)){
			return 1;
		}else{
			return 0;
		}
	}


	public static Armor getArmor(Material material){
		Preconditions.checkState(isArmor(material));
		if(isLeatherArmor(material)){
			return Armor.LEATHER;
		}else if(isChainArmor(material)){
			return Armor.CHAIN_MAIL;
		}else if(isGoldenArmor(material)){
			return Armor.GOLD;
		}else if(isIronArmor(material)){
			return Armor.IRON;
		}else{
			return Armor.DIAMOND;
		}
	}

	public static boolean isArmorOfType(Material material , Armor armor){
		Preconditions.checkState(isArmor(material));
		switch(armor){
		case CHAIN_MAIL:
			return isChainArmor(material);
		case DIAMOND:
			return isDiamondArmor(material);
		case GOLD : 
			return isGoldenArmor(material);
		case IRON :
			return isIronArmor(material);
		case LEATHER:
			return isLeatherArmor(material);
		}
		throw new AssertionError();
	}
	public static boolean isArmor(Material material){
		return isHelmet(material) || isChestPlate(material) || isLegging(material) || isBoot(material);
	}
	public static boolean isHelmet(Material material){
		switch(material){
		case GOLD_HELMET:
		case LEATHER_HELMET:
		case CHAINMAIL_HELMET:
		case DIAMOND_HELMET:
		case IRON_HELMET:
			return true;
		default:
			return false;
		}
	}

	public static boolean isChestPlate(Material material){
		switch(material){
		case GOLD_CHESTPLATE:
		case LEATHER_CHESTPLATE:
		case CHAINMAIL_CHESTPLATE:
		case DIAMOND_CHESTPLATE:
		case IRON_CHESTPLATE:
			return true;
		default:
			return false;
		}
	}

	public static boolean isLegging(Material material){
		switch(material){
		case GOLD_LEGGINGS:
		case LEATHER_LEGGINGS:
		case CHAINMAIL_LEGGINGS:
		case DIAMOND_LEGGINGS:
		case IRON_LEGGINGS:
			return true;
		default:
			return false;
		}
	}

	public static boolean isBoot(Material material){
		switch(material){
		case GOLD_BOOTS:
		case LEATHER_BOOTS:
		case CHAINMAIL_BOOTS:
		case DIAMOND_BOOTS:
		case IRON_BOOTS:
			return true;
		default:
			return false;
		}
	}

	public static boolean isGoldenArmor(Material material){
		switch(material){
		case GOLD_HELMET:
		case GOLD_CHESTPLATE:
		case GOLD_LEGGINGS:
		case GOLD_BOOTS:
			return true;
		default:
			return false;
		}
	}

	public static boolean isChainArmor(Material material){
		switch(material){
		case CHAINMAIL_HELMET:
		case CHAINMAIL_CHESTPLATE:
		case CHAINMAIL_LEGGINGS:
		case CHAINMAIL_BOOTS:
			return true;
		default:
			return false;
		}
	}

	public static boolean isIronArmor(Material material){
		switch(material){
		case IRON_HELMET:
		case IRON_CHESTPLATE:
		case IRON_LEGGINGS:
		case IRON_BOOTS:
			return true;
		default:
			return false;
		}
	}
	public static boolean isDiamondArmor(Material material){
		switch(material){
		case DIAMOND_HELMET:
		case DIAMOND_CHESTPLATE:
		case DIAMOND_LEGGINGS:
		case DIAMOND_BOOTS:
			return true;
		default:
			return false;
		}
	}

	public static boolean isLeatherArmor(Material material){
		switch(material){
		case LEATHER_HELMET:
		case LEATHER_CHESTPLATE:
		case LEATHER_LEGGINGS:
		case LEATHER_BOOTS:
			return true;
		default:
			return false;
		}
	}


	public static boolean canInteract(Material material) {
		if (material == null || !material.isBlock()) {
			return false;
		}
		switch (material) {
		case DISPENSER:
		case NOTE_BLOCK:
		case BED_BLOCK:
		case CHEST:
		case WORKBENCH:
		case FURNACE:
		case BURNING_FURNACE:
		case WOODEN_DOOR:
		case LEVER:
		case REDSTONE_ORE:
		case STONE_BUTTON:
		case JUKEBOX:
		case CAKE_BLOCK:
		case DIODE_BLOCK_ON:
		case DIODE_BLOCK_OFF:
		case TRAP_DOOR:
		case FENCE_GATE:
		case ENCHANTMENT_TABLE:
		case BREWING_STAND:
		case DRAGON_EGG:
		case ENDER_CHEST:
		case COMMAND:
		case BEACON:
		case WOOD_BUTTON:
		case ANVIL:
		case TRAPPED_CHEST:
		case REDSTONE_COMPARATOR_ON:
		case REDSTONE_COMPARATOR_OFF:
		case HOPPER:
		case DROPPER:
			return true;
		default:
			return false;
		}
	}

}
