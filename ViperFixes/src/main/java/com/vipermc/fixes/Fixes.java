package com.vipermc.fixes;

import org.bukkit.plugin.java.*;

import net.minecraft.server.v1_7_R4.TileEntity;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.util.com.google.common.collect.*;
import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.claim.Position2D;
import net.syuu.popura.combattag.CombatLoggerManager;
import net.syuu.popura.combattag.CombatTagManager;
import net.syuu.popura.event.AsyncTabParaseEvent;
import net.syuu.popura.faction.FactionType;
import net.syuu.popura.faction.bean.ClaimedRegion;

import org.bukkit.scheduler.*;
import com.google.common.collect.*;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.*;
import java.util.*;
import org.bukkit.plugin.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.inventory.*;
import com.google.common.io.*;
import com.google.common.base.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Fixes extends JavaPlugin implements Listener {

	private int target = -1;
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new DupeFix(), (Plugin) this);
		this.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this);

		getCommand("tpnearhere").setExecutor(new CommandExecutor() {

			@Override
			public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
				if(!sender.isOp()){
					return true;
				}
				Player to = ((Player)sender);
				for(Player player : Bukkit.getOnlinePlayers()){
					if(player.getLocation().distance(to.getLocation()) < Integer.valueOf(args[0])){
						player.teleport(to);
					}
				}
				return true;
			}
		});
		getCommand("setconnectioninterval").setExecutor(new CommandExecutor() {

			@Override
			public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
				if(!sender.isOp()){
					return true;
				}
				CraftServer craftserver = ((CraftServer)Bukkit.getServer());
				try {
					Field field = CraftServer.class.getDeclaredField("configuration");
					field.setAccessible(true);
					YamlConfiguration config = (YamlConfiguration) field.get(craftserver);
					config.set("settings.connection-throttle", Integer.valueOf(args[0]));
					sender.sendMessage("Connection Thorthle = " + craftserver.getConnectionThrottle());
				} catch (Exception e) {
					e.printStackTrace();
					sender.sendMessage("Error");
				}
				return true;
			}
		});
		getCommand("clist").setExecutor(new CommandExecutor() {

			@Override
			public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] args) {
				for(World world : Bukkit.getWorlds()){
				arg0.sendMessage("" + world.getLoadedChunks().length);
				}

				return true;
			}
		});
		getCommand("abcsetonlineplayers").setExecutor(new CommandExecutor() {

			@Override
			public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] args) {
				if(args.length <= 0){
					arg0.sendMessage("/<label> <number>");
					return true;
				}
				target = Integer.valueOf(args[0]);
				arg0.sendMessage("target set to " + target);

				return true;
			}
		});
		new BukkitRunnable() {

			@Override
			public void run() {
				try{
					CombatLoggerManager loggerManager;
					CombatTagManager tagManager = PopuraPlugin.getInstance().getPopura().getCombatTagManager();
					Field field = CombatTagManager.class.getDeclaredField("combatLoggerManager");
					field.setAccessible(true);
					loggerManager = (CombatLoggerManager) field.get(tagManager);
					HandlerList.unregisterAll(loggerManager);
					System.out.println("Unregister Success");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.runTaskLater(this, 100);

	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockFromTo(BlockFromToEvent event){
		event.setCancelled(false);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onUpdate(AsyncTabParaseEvent event){
		if(!event.getPlayer().isOnline()){
            return;
        }
        String result = event.getInput();
        int online = Bukkit.getOnlinePlayers().size() + target;
        result = result.replace("{server.online}" , String.valueOf(online));
        event.setInput(result);

	}
	@EventHandler
	public void onServerPing(ServerListPingEvent event){
		try{
			Field field = ServerListPingEvent.class.getDeclaredField("numPlayers");
			field.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(event, event.getNumPlayers() + target);
			System.out.println(event.getNumPlayers() + " online returned as ping");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerSpawn(PlayerCommandPreprocessEvent event){
		if(event.getMessage().toLowerCase().equalsIgnoreCase("stats") || event.getMessage().toLowerCase().equalsIgnoreCase("/stats") ||event.getMessage().toLowerCase().startsWith("stats") || event.getMessage().toLowerCase().startsWith("/stats")){
			event.setMessage(event.getMessage().toLowerCase().replace("stats", "vipercore:stats"));
		}
		if(event.getMessage().toLowerCase().equalsIgnoreCase("who") || event.getMessage().toLowerCase().equalsIgnoreCase("/who") ||event.getMessage().toLowerCase().startsWith("who") || event.getMessage().toLowerCase().startsWith("/who")){
			event.setCancelled(true);
			event.setMessage("/testmmgom");
			event.getPlayer().sendMessage("There are " + Bukkit.getOnlinePlayers().size() + " players online");
		}
		if(event.getMessage().toLowerCase().equalsIgnoreCase("online") || event.getMessage().toLowerCase().equalsIgnoreCase("/online") ||event.getMessage().toLowerCase().startsWith("online") || event.getMessage().toLowerCase().startsWith("/online")){
			event.setCancelled(true);
			event.setMessage("/testmmgom");
			event.getPlayer().sendMessage("There are " + Bukkit.getOnlinePlayers().size() + " players online");
		}

		if(event.getMessage().toLowerCase().equalsIgnoreCase("livesclaim") || event.getMessage().toLowerCase().equalsIgnoreCase("/livesclaim") ||event.getMessage().toLowerCase().startsWith("livesclaim") || event.getMessage().toLowerCase().startsWith("/livesclaim")){
			event.setCancelled(true);
			event.setMessage("/testmmgom");
		}
		if(event.getMessage().toLowerCase().equalsIgnoreCase("vipercore:livesclaim") || event.getMessage().toLowerCase().equalsIgnoreCase("/vipercore:livesclaim") ||event.getMessage().toLowerCase().startsWith("vipercore:livesclaim") || event.getMessage().toLowerCase().startsWith("/vipercore:livesclaim")){
			event.setCancelled(true);
			event.setMessage("/testmmgom");
		}
		if(event.getMessage().toLowerCase().equalsIgnoreCase("list") || event.getMessage().toLowerCase().equalsIgnoreCase("/list") ||event.getMessage().toLowerCase().startsWith("list") || event.getMessage().toLowerCase().startsWith("/list")){
			event.setCancelled(true);
			event.setMessage("/testmmgom");
			Collection<String> venoms = Collections2.transform(Collections2.filter(Lists.newArrayList(Bukkit.getOnlinePlayers()) , new Predicate<Player>() {
				@Override
				public boolean apply(Player arg0) {
					return arg0.hasPermission("list.staff");
				}
			}) , new Function<Player, String>() {
				@Override
				public String apply(Player arg0) {
					return arg0.getName();
				}
			});
			int size = Bukkit.getOnlinePlayers().size() + target + 1;
			event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m---------------------------------"));
			event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Staff: &f" + Joiner.on(", " + ChatColor.WHITE).join(venoms)));
			event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m---------------------------------"));
			event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThere are currently &b" + size +"/" + Bukkit.getMaxPlayers()+" &eonline!"));


		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract2(PlayerInteractEvent event){
		if(event.hasBlock() && event.hasItem()){
			Location location = event.getClickedBlock().getLocation();
			if(location.getWorld().getEnvironment() == Environment.NETHER && (event.getItem().getType() == Material.BED || event.getItem().getType() == Material.BED_BLOCK)){
				ClaimedRegion region = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(location.getWorld().getName(), location.getBlockX()  , location.getBlockZ()));
				if(region == null){
					event.setCancelled(false);
				}	
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract2(BlockPlaceEvent event){
		Location location = event.getBlock().getLocation();
		if(location.getWorld().getEnvironment() == Environment.NETHER && (event.getBlock().getType() == Material.BED || event.getBlock().getType() == Material.BED_BLOCK)){
			ClaimedRegion region = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(location.getWorld().getName(), location.getBlockX()  , location.getBlockZ()));
			if(region == null){
				event.setCancelled(false);
			}	
		}
	}
	@EventHandler
	public void onInventoryClick(final PlayerDropItemEvent event){
		if(isDupeItem(event.getItemDrop().getItemStack())){
			event.getItemDrop().remove();
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.hasItem() && isDupeItem(event.getItem())){
			event.getPlayer().setItemInHand(null);
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onInventoryClick(final PlayerPickupItemEvent event){
		scan(event.getPlayer());;
	}
	@EventHandler
	public void onInventoryClick(final InventoryClickEvent event){
		if(isDupeItem(event.getCurrentItem())){
			event.setCurrentItem(null);
			scan(event.getWhoClicked());;
		}
		if(isDupeItem(event.getCursor())){
			scan(event.getWhoClicked());;
		}
	}
	public void scan(final HumanEntity player){
		new BukkitRunnable() {

			@Override
			public void run() {
				List<ItemStack> remove = Lists.newArrayList();
				for(ItemStack itemstack : player.getInventory().getContents()){
					if(itemstack != null && isDupeItem(itemstack)){
						remove.add(itemstack);
					}
				}
				for(ItemStack item : remove){
					player.getInventory().remove(item);
				}
				//((Player)player).updateInventory();
			}
		}.runTaskLater(this, 20);
	}
	public boolean isDupeItem(ItemStack itemstack){
		if(itemstack != null && itemstack.getType() == Material.POTION){
			if(itemstack.hasItemMeta()){
				ItemMeta meta = itemstack.getItemMeta();
				if(meta.hasDisplayName() && meta.hasLore()){
					if(ChatColor.stripColor(meta.getDisplayName()).toLowerCase().startsWith("crate")){
						return true;
					}
				}
			}
		}
		return false;
	}

	@EventHandler
	public void onPlayerSpawn(PlayerJoinEvent event){
		final Player player = event.getPlayer();
		new BukkitRunnable() {

			@Override
			public void run() {
				ClaimedRegion region = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(player.getWorld().getName(), player.getLocation().getBlockX()  , player.getLocation().getBlockZ()));
				if(region != null && region.getOwner().getFactionType() == FactionType.SAFEZONE){
					player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
				}
			}
		}.runTaskLater(this, 1);
	}
}
