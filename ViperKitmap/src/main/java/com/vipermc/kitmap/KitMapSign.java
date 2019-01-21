package com.vipermc.kitmap;

import net.syuu.common.type.*;
import java.util.*;
import org.bukkit.plugin.*;
import org.bukkit.configuration.file.*;
import org.bukkit.permissions.*;
import net.syuu.common.utils.*;
import net.syuu.common.command.*;
import net.syuu.common.command.annotation.SimpleCommand;

import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.*;
import java.io.*;
import org.bukkit.entity.*;

public class KitMapSign implements Listener
{
	private Map<Armor, StoredPlayerInventory> stored;
	private File folder;
	private KitMap plugin;
	public KitMapSign(final KitMap plugin) {
		this.plugin = plugin;
		this.stored = new HashMap<Armor, StoredPlayerInventory>();
	}

	public void init() {
		this.folder = new File(plugin.getDataFolder(), "kitsign");
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		for (final Armor armor : Armor.values()) {
			final File data = new File(this.folder, armor.name() + ".yml");
			if (data.exists()) {
				final StoredPlayerInventory inventory = new StoredPlayerInventory(YamlConfiguration.loadConfiguration(data));
				this.stored.put(armor, inventory);
			}
		}
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		CommandUtils.registerPermission("kitmap.command", PermissionDefault.OP);
		CommandRegistry.getInstance().registerCommand(new KitMapCommand(this));
		for(Armor armor : Armor.values()){

			final Armor a = armor;
			CommandRegistry.getInstance().registerCommand(new Command(armor.name().toLowerCase()) {
				@Override
				public void proccess(Player player, String[] args) {
					for(ItemStack itemstack : player.getInventory().getArmorContents()){
						if(itemstack != null && itemstack.getType() != Material.AIR){
							return;
						}
					}
					final StoredPlayerInventory inventory = getStoredInventory(a);
					if (inventory != null) {
						inventory.paste(player);
						String msg = plugin.getConfig().getString("kit-enabled").replaceAll("&", "§");
						player.sendMessage(msg);
					}
					else {
						player.sendMessage(ChatColor.RED + "No inventory found for " + a.name().toLowerCase());
					}
				}
			});
		}
	}

	@EventHandler
	public void onSignChange(final SignChangeEvent event) {
		final String text = ChatColor.stripColor(event.getLine(0));
		final Armor found = this.findArmor(text);
		if (found != null && !event.getPlayer().hasPermission("kitmap.command")) {
			event.setLine(0, "NoPermission");
		}
	}

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if (event.hasBlock()) {
			final BlockState state = event.getClickedBlock().getState();
			if (state instanceof Sign) {
				final Sign sign = (Sign)state;
				final String text = sign.getLine(0);
				final Armor found = this.findArmor(ChatColor.stripColor(text));
				if (found != null) {
					final StoredPlayerInventory inventory = this.getStoredInventory(found);
					if (inventory != null) {
						inventory.paste(event.getPlayer());
						String msg = plugin.getConfig().getString("kit-enabled").replaceAll("&", "§");
						event.getPlayer().sendMessage(msg);
					}
					else {
						event.getPlayer().sendMessage(ChatColor.RED + "No inventory found for " + text);
					}
				}
			}
		}
	}

	public Armor findArmor(final String input) {
		Armor armor = null;
		try {
			armor = Armor.valueOf(input.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			if (input.equalsIgnoreCase("archer")) {
				armor = Armor.LEATHER;
			}
			else if (input.equalsIgnoreCase("rogue")) {
				armor = Armor.CHAIN_MAIL;
			}
			else if (input.equalsIgnoreCase("builder")) {
				armor = Armor.IRON;
			}
			else if (input.equalsIgnoreCase("diamond")) {
				armor = Armor.DIAMOND;
			}
			else if (input.equalsIgnoreCase("bard")) {
				armor = Armor.GOLD;
			}
		}
		return armor;
	}

	public StoredPlayerInventory getStoredInventory(final Armor armor) {
		return this.stored.get(armor);
	}

	public void save(final Armor armor, final StoredPlayerInventory inv) {
		final File data = new File(this.folder, armor.name() + ".yml");
		final YamlConfiguration file = new YamlConfiguration();
		inv.paste(file);
		try {
			file.save(data);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		this.stored.put(armor, inv);
	}

	public static class KitMapCommand extends Command
	{
		private final KitMapSign kitMap;

		public KitMapCommand(final KitMapSign kitMap) {
			super("kitmap");
			this.kitMap = kitMap;
		}

		public void proccess(final Player player, final String[] args) {
			if (!player.hasPermission("kitmap.command")) {
				player.sendMessage(ChatColor.RED + "No Permission");
				return;
			}
			if (args.length < 2) {
				player.sendMessage(ChatColor.RED + "Usage: /kitmap save <archer|rogue|bard|miner|diamond>");
				return;
			}
			if (args[0].equals("save")) {
				final Armor armor = this.kitMap.findArmor(args[1]);
				if (armor == null) {
					player.sendMessage(ChatColor.RED + "No Such Kit");
				}
				else {
					this.kitMap.save(armor, new StoredPlayerInventory(player));
					player.sendMessage(ChatColor.GREEN + "Saved!");
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "Usage: /kitmap save <leather|chain_mail|gold|iron>");
			}
		}
	}
}
