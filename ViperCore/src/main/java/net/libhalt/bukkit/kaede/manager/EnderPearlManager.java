package net.libhalt.bukkit.kaede.manager;

import java.util.HashMap;
import java.util.Map;

import net.syuu.common.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class EnderPearlManager extends Manager implements Listener {

	public static Map<Player , Long> expire = new HashMap<Player , Long>();
	private ConfigurationWrapper config;
	private long cooldownDuration = 10000L;

	public EnderPearlManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.config = new ConfigurationWrapper("cooldowns.yml", this.getPlugin());
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
		reload();
	}

	@Override
	public void reload() {
		this.cooldownDuration = this.config.getConfig().getInt("enderpearl-cooldown") * 1000L;
	}

	public long getMillisecondLeft(Player player) {
		return this.expire.containsKey(player) ? Math.max(this.expire.get(player).longValue() - System.currentTimeMillis(), 0L) : 0L;
	}

	public static boolean isCooldownActive(Player player) {
		return EnderPearlManager.expire.containsKey(player) ? System.currentTimeMillis() < EnderPearlManager.expire.get(player).longValue() : false;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		this.expire.remove(event.getPlayer());
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		this.expire.remove(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR )
	public void onPlayerTeleport(PlayerTeleportEvent event){
		if(event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
			if (event.isCancelled() || event.getTo().equals(event.getFrom())) {
				this.expire.remove(event.getPlayer());
			}
		}
	}
	@EventHandler(priority = EventPriority.MONITOR  )
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasItem() && event.getItem().getType() == Material.ENDER_PEARL) {
			final Block block = event.getClickedBlock();
			if (block.getType().isSolid() && !(block.getState() instanceof InventoryHolder)) {
				return;
			}
		}
		Action action = event.getAction();
		if (event.hasItem() && event.getItem().getType() == Material.ENDER_PEARL && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			PlayerUtils.updateInventory(player);

			if (this.isCooldownActive(player)) {
				event.setUseItemInHand(Result.DENY);
				int time = (int) (this.getMillisecondLeft(player) / 1000.0D);
				getPlugin().sendLocalized(player, "ENDERPEARL_COOLDOWN", String.valueOf(time));
			} else {
				this.expire.put(player, Long.valueOf(System.currentTimeMillis() + this.cooldownDuration));
			}
		}
	}
}
