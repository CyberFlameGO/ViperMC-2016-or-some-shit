package net.libhalt.bukkit.kaede.manager.crowbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.syuu.popura.event.ShopParseEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.ItemStackUtils;
import net.libhalt.bukkit.kaede.utils.Manager;

public class CrowbarManager extends Manager implements Listener, CommandExecutor {

	private final String CROWBAR_TITLE = ChatColor.RED + "Crowbar";
	private ConfigurationWrapper config;
	private Material crowbarItem = Material.DIAMOND_HOE;
	private final Pattern crowbarUsageRegex;
	private Map<CrowbarType  , Salvager> salavageHandlers = new HashMap<>();
	private static final Map<CrowbarType , Integer> DEFAULT_USAGE = new HashMap<>();
	static{
		DEFAULT_USAGE.put(CrowbarType.SPAWNER, 1);
		DEFAULT_USAGE.put(CrowbarType.END_PORTAL, 6);
	}

	public String toReadable(EntityType entityType){
		return  ChatColor.GREEN + StringUtils.capitalize(entityType.name().replace("_", " ").toLowerCase()) + " Spawner";
	}
	public CrowbarManager(HCFactionPlugin plugin) {
		super(plugin);
		this.crowbarUsageRegex = Pattern.compile("\\{([^)]+)\\}");
		salavageHandlers.put(CrowbarType.END_PORTAL, new Salvager() {
			@Override
			public void onSalavage(Player player , ItemStack item, Block clicked) {
				int endPortalUsage = getCount(item, CrowbarType.END_PORTAL);
				if (endPortalUsage > 0) {
					Location location = clicked.getLocation();
					World world = clicked.getWorld();
					clicked.setType(Material.AIR);
					clicked.getWorld().playEffect(location, Effect.STEP_SOUND, Material.ENDER_PORTAL_FRAME.getId());
					clearPortalWithEffect(location);
					world.dropItemNaturally(location, new ItemStack(Material.ENDER_PORTAL_FRAME));
					--endPortalUsage;
					if (endPortalUsage < 1) {
						player.setItemInHand(null);
					} else {
						short maxDura = item.getType().getMaxDurability();
						item.setDurability((short) ((int) (maxDura - (maxDura / 6.0D * endPortalUsage))));
						Map<CrowbarType , Integer> usage = new HashMap<>();
						usage.put(CrowbarType.END_PORTAL, endPortalUsage);
						updateCrowbarMeta(item, usage);
					}
					player.updateInventory();
				} else {
					getPlugin().sendLocalized(player , "CROWBAR_END_FRAME_LEFT" , "0");
				}
			}
			private void clearPortalWithEffect(Location begin){
				for (int x = -4; x < 5; x++) {
					for (int z = -4; z < 5; z++) {
						Location found = begin.clone();
						found.add(x, 0.0D, z);
						if (found.getBlock().getType() == Material.ENDER_PORTAL) {
							begin.getWorld().playEffect(found, Effect.STEP_SOUND, Material.ENDER_PORTAL.getId());
							found.getBlock().setType(Material.AIR);
						}
					}
				}
			}
		});
		salavageHandlers.put(CrowbarType.SPAWNER, new Salvager() {
			
			@Override
			public void onSalavage(Player player, ItemStack item, Block block) {
				int spawnerUsage = getCount(item, CrowbarType.SPAWNER);
				Location location = block.getLocation();
				if (spawnerUsage > 0) {
					location.getWorld().playEffect(location, Effect.STEP_SOUND, Material.MOB_SPAWNER.getId());
					CreatureSpawner var13 = (CreatureSpawner) block.getState();
					String readableSpawnerType =toReadable(var13.getSpawnedType());
					ItemStack spawner = ItemStackUtils.setItemTitle(new ItemStack(Material.MOB_SPAWNER), readableSpawnerType);
					block.setType(Material.AIR);
					block.getWorld().dropItemNaturally(location, spawner);
					--spawnerUsage;
					if (spawnerUsage < 1) {
						player.setItemInHand(null);
					} else {
						short max = crowbarItem.getMaxDurability();
						item.setDurability((short) ((int) (max - max / 6.0D * spawnerUsage)));
						Map<CrowbarType , Integer> usage = new HashMap<>();
						usage.put(CrowbarType.SPAWNER, spawnerUsage);
						updateCrowbarMeta(item, usage);
					}
					player.updateInventory();
				} else {
					getPlugin().sendLocalized(player , "CROWBAR_SPAWNER_LEFT" , "0");
				}				
			}
		});
	}

	@Override
	public void init() {
		this.config = new ConfigurationWrapper("crowbar.yml", this.getPlugin());
		this.getPlugin().getCommand("crowbar").setExecutor(this);
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
		reload();
	}

	@Override
	public void reload() {
		try{
			crowbarItem = Material.valueOf(config.getConfig().getString("crowbar-material" , "DIAMOND_HOE").toUpperCase());
		}catch(IllegalArgumentException e){
			getPlugin().getLogger().severe("Can not parase crowbar-material in corwbar.yml");
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender.hasPermission("command.crowbar.use")) {
			if (args.length == 0 && sender instanceof Player) {
				Player player = (Player) sender;
				player.getInventory().addItem(new ItemStack[] { this.getNewCrowbar() });
			}
			if (args.length > 0) {
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null){
					sender.sendMessage("Not Online");
					return true;
				}
				target.getInventory().addItem(new ItemStack[] { this.getNewCrowbar() });
			}
		} else {
			sender.sendMessage(ChatColor.RED + "No Permision");
		}

		return true;
	}


	@EventHandler
	public void onInteract(ShopParseEvent event) {
		if(event.getInput().equalsIgnoreCase("WRENCH")){
			event.setItemStack(getNewCrowbar());
		}

		if(event.getInput().endsWith("SPAWNER")){
			String left = event.getInput().replace("SPAWNER" , "").replace("_" , "");
			EntityType entityType = EntityType.valueOf(left);
			ItemStack spawner = ItemStackUtils.setItemTitle(new ItemStack(Material.MOB_SPAWNER), toReadable(entityType));
			event.setItemStack(spawner);
		}
	}
	@EventHandler(ignoreCancelled = true , priority = EventPriority.HIGH)
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			ItemStack hand = player.getItemInHand();
			if (hand != null && hand.hasItemMeta() && hand.getItemMeta().hasDisplayName() && hand.getItemMeta().getDisplayName().equals(this.CROWBAR_TITLE)) {
				event.setCancelled(true);
				Block clicked = event.getClickedBlock();
				World world = clicked.getWorld();
				if(world.getEnvironment() != Environment.NORMAL){
					player.sendMessage(ChatColor.RED + "Crowbars can only be used in overworld and is disabled in " + StringUtils.capitalize(world.getEnvironment().name().toLowerCase()));
					return;
				}
				Material type = clicked.getType();
				CrowbarType crowbarType = CrowbarType.fromMaterial(type);
				if(crowbarType != null && salavageHandlers.containsKey(crowbarType)){
					salavageHandlers.get(crowbarType).onSalavage(event.getPlayer(), hand, clicked);
				}else {
					String readable = StringUtils.capitalize(type.name().toLowerCase().replace("_", " "));
					getPlugin().sendLocalized(player , "CROWBAR_DENIED" , readable);
				}
			}
		}

	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		ItemStack item = event.getItemInHand();
		if (item != null && item.getType() == Material.MOB_SPAWNER && item.getItemMeta().hasDisplayName()) {
			String display = item.getItemMeta().getDisplayName();
			if (display.startsWith(ChatColor.GREEN.toString()) && display.endsWith(" Spawner")) {
				String type = ChatColor.stripColor(display).replace(" Spawner", "").replace(" ", "_").toUpperCase();
				EntityType entity = EntityType.valueOf(type);
				CreatureSpawner spawner = (CreatureSpawner) block.getState();
				spawner.setSpawnedType(entity);
				spawner.update();
			}
		}

	}

	public int getCount(ItemStack item, CrowbarType type) {
		ItemMeta meta = item.getItemMeta();
		if (meta.hasLore()) {
			for(String lore : meta.getLore()){
				String stripepd = ChatColor.stripColor(lore);
				if(stripepd.startsWith(type.toReadable())){
					Matcher matcher = this.crowbarUsageRegex.matcher(stripepd);
					if (matcher.find()) {
						return Integer.valueOf(matcher.group(1)).intValue();
					}
				}
			}
		}

		return 0;
	}

	public ItemStack getNewCrowbar() {
		ItemStack item = new ItemStack(crowbarItem);
		this.updateCrowbarMeta(item,DEFAULT_USAGE);
		return item;
	}

	public void updateCrowbarMeta(ItemStack item, Map<CrowbarType , Integer> usage) {
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		for(Entry<CrowbarType , Integer>  entry : usage.entrySet()){
			lore.add(ChatColor.AQUA + entry.getKey().toReadable() + ChatColor.YELLOW + "{" + ChatColor.BLUE + entry.getValue() + ChatColor.YELLOW + "}");
		}
		meta.setDisplayName(this.CROWBAR_TITLE);
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
}
