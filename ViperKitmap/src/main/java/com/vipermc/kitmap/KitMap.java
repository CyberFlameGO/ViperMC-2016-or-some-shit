package com.vipermc.kitmap;

import org.bukkit.plugin.java.*;
import org.bukkit.scheduler.BukkitRunnable;

import net.syuu.common.command.CommandRegistry;
import net.syuu.common.command.annotation.SimpleCommand;
import net.syuu.common.type.Armor;
import net.syuu.common.utils.ItemStackUtils;
import net.syuu.common.utils.PlayerUtils;
import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.claim.Position2D;
import net.syuu.popura.faction.FactionType;
import net.syuu.popura.faction.bean.ClaimedRegion;
import net.syuu.popura.faction.bean.FactionPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitMap extends JavaPlugin implements Listener {
	
	private KitMapSign kitmapSign = new KitMapSign(this);
	
	public void onEnable() {

		setupConfig();
		addFixes();

		kitmapSign.init();
		CommandRegistry.getInstance().registerSimpleCommand(this);
		getServer().getPluginManager().registerEvents(this, this);
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()){
					FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
					if(factionPlayer.getFaction() != null){
						factionPlayer.getFaction().setDtr(1.01D);
					}
				}
			}
		}.runTaskTimer(this, 20, 20);
	}

	private void addFixes()
	{
		getServer().getPluginManager().registerEvents(new Updates(this),this);
	}

	private void setupConfig()
	{
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		if(event.getPlayer().getInventory().getHelmet() == null || event.getPlayer().getInventory().getHelmet().getType() == Material.AIR){

			event.getPlayer().chat("/spawn");
			PlayerUtils.clear(event.getPlayer());
			event.getPlayer().getInventory().setItem(0, new ItemStack(Material.BOOK));		}
	}

	@EventHandler
	public void onPlayerRespawn(InventoryClickEvent event){
		Location location = ((Player)event.getWhoClicked()).getLocation();
		ClaimedRegion region = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(location.getWorld().getName(), location.getBlockX()  , location.getBlockZ()));
		if(region != null && region.getOwner().getFactionType() == FactionType.SAFEZONE){
			if(event.getSlotType() == SlotType.ARMOR){
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onPlayerRespawn(PlayerDropItemEvent event){
		Location location = ((Player)event.getPlayer()).getLocation();
		ClaimedRegion region = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(location.getWorld().getName(), location.getBlockX()  , location.getBlockZ()));
		if(region != null && region.getOwner().getFactionType() == FactionType.SAFEZONE){
			event.setCancelled(true);

		}
		
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		PlayerUtils.clear(event.getPlayer());
		event.getPlayer().getInventory().setItem(0, new ItemStack(Material.BOOK));
	}

	@SimpleCommand(requireop = false , name = "suicide")
	public void suicide(Player player , String[] args){
		player.setHealth(0.0D);
	}
	@SimpleCommand(requireop = false , name = "openkitgui")
	public void openGUI(Player player , String[] args){
		Location location = ((Player)player).getLocation();
		ClaimedRegion region = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(location.getWorld().getName(), location.getBlockX()  , location.getBlockZ()));
		if(region != null && region.getOwner().getFactionType() == FactionType.SAFEZONE){
			
		Inventory inventory = Bukkit.createInventory(null, 9 , "Kit Selector");
		inventory.setItem(0	, ItemStackUtils.setItemTitle(new ItemStack(Material.LEATHER_HELMET) , ChatColor.AQUA + "Archer"));
		inventory.setItem(3	, ItemStackUtils.setItemTitle(new ItemStack(Material.DIAMOND_HELMET) , ChatColor.AQUA + "Diamond"));
		inventory.setItem(5	, ItemStackUtils.setItemTitle(new ItemStack(Material.IRON_HELMET) , ChatColor.AQUA + "Builder"));
		inventory.setItem(8	, ItemStackUtils.setItemTitle(new ItemStack(Material.GOLD_HELMET) , ChatColor.AQUA + "Bard"));
		
		player.openInventory(inventory);
		}
	}
	@EventHandler
	public void onPlayerClick(InventoryClickEvent event){
		if(event.getInventory() != null && event.getInventory().getName().equals("Kit Selector")){
			event.setCancelled(true);
			ItemStack look = event.getCursor();
			if(look == null || look.getType() == Material.AIR){
				look = event.getCurrentItem();
			}
			if(look != null){
				if(look.getType() == Material.LEATHER_HELMET){
					PlayerUtils.clearInventory(((Player)event.getWhoClicked()));
					((Player)event.getWhoClicked()).chat("/" + Armor.LEATHER.name().toLowerCase());
				}
				if(look.getType() == Material.DIAMOND_HELMET){
					PlayerUtils.clearInventory(((Player)event.getWhoClicked()));
					((Player)event.getWhoClicked()).chat("/" + Armor.DIAMOND.name().toLowerCase());
				}
				if(look.getType() == Material.GOLD_HELMET){
					PlayerUtils.clearInventory(((Player)event.getWhoClicked()));
					((Player)event.getWhoClicked()).chat("/" + Armor.GOLD.name().toLowerCase());
				}
				if(look.getType() == Material.IRON_HELMET){
					PlayerUtils.clearInventory(((Player)event.getWhoClicked()));
					((Player)event.getWhoClicked()).chat("/" + Armor.IRON.name().toLowerCase());
				}
			}
		}
	}
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.hasItem() && event.getItem().getType() == Material.BOOK && event.getPlayer().getInventory().getHeldItemSlot() != 8){
			event.getPlayer().chat("/openkitgui");
		}
	}
}
