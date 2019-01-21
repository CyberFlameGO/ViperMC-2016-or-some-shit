package net.libhalt.bukkit.kaede.manager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.Manager;

public class FreezeManager extends Manager implements Listener, CommandExecutor {
	private List<Player> frozen = new ArrayList<Player>();

	public FreezeManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		(new BukkitRunnable() {
			@Override
			public void run() {
				for(Player player : frozen){
					player.sendMessage(ChatColor.WHITE + "\u2588\u2588\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588\u2588\u2588\u2588");
					player.sendMessage(ChatColor.WHITE + "\u2588\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.GOLD + ChatColor.BLACK + "\u2588" + ChatColor.GOLD + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588\u2588\u2588");
					player.sendMessage(ChatColor.WHITE + "\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.BLACK + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588\u2588");
					player.sendMessage(ChatColor.WHITE + "\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.BLACK + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588\u2588 " + getPlugin().getLocalized(player, "FREEZE_MESSAGE"));
					player.sendMessage(ChatColor.WHITE + "\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.BLACK + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588\u2588" + getPlugin().getLocalized(player, "FREEZE_MESSAGE_LINE2"));
					player.sendMessage(ChatColor.WHITE + "\u2588" + ChatColor.RED + "\u2588" + ChatColor.GOLD + "\u2588\u2588\u2588" + ChatColor.BLACK + ChatColor.GOLD + "\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588");
					player.sendMessage(new StringBuilder().append(ChatColor.WHITE).append(ChatColor.RED).append("\u2588").append(ChatColor.GOLD).append("\u2588\u2588\u2588").append(ChatColor.BLACK).append("\u2588").append(ChatColor.GOLD).append("\u2588\u2588\u2588").append(ChatColor.RED).append("\u2588").append(ChatColor.WHITE).toString());
					player.sendMessage(ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
				}

			}
		}).runTaskTimer(this.getPlugin(), 100L, 100L);
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
		this.getPlugin().getCommand("ss").setExecutor(this);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Location from = event.getFrom();
		Location to = event.getTo();
		if ((from.getX() != to.getX() || from.getZ() != to.getZ()) && this.frozen.contains(event.getPlayer())) {
			event.setTo(event.getFrom());
		}

	}
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event){
		if(frozen.contains(event.getEntity().getShooter())){
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		if(frozen.contains(event.getEntity())){
			event.setCancelled(true);
		}
		if(frozen.contains(event.getDamager())){
			event.setCancelled(true);
		}
		if (event.getDamager() instanceof Projectile) {
			Projectile projectile1 = (Projectile) event.getDamager();
			if (projectile1.getShooter() instanceof Player) {
				Player player = (Player) projectile1.getShooter();
				if(frozen.contains(player)){
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler
	public void onPlayerTelport(PlayerTeleportEvent event){
		if(event.getCause() == TeleportCause.ENDER_PEARL && this.frozen.contains(event.getPlayer())){
			event.setTo(event.getFrom());
		}
	}

	@EventHandler
	public void onSplash(PotionSplashEvent event) {
		if(!frozen.isEmpty()){
			try {
				Field eventField = PotionSplashEvent.class.getDeclaredField("affectedEntities");
				eventField.setAccessible(true);
				Map<?, ?> affectedEntities = (Map<?, ?>)eventField.get(event);
				Iterator<?> iterator = affectedEntities.entrySet().iterator();
				while(iterator.hasNext()) {
					Entry<?, ?> entry = (Entry<?, ?>)iterator.next();
					if(frozen.contains(entry.getKey())) {
						iterator.remove();
					}
				}
				eventField.set(event, affectedEntities);
			}catch(Exception exception) {
			}
		}

	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		this.frozen.remove(event.getPlayer());
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		this.frozen.remove(event.getPlayer());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender.hasPermission("command.ss.use")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "/ss <name>");
			} else {
				Player player = this.getPlugin().getServer().getPlayer(args[0]);
				if (player == null) {
					sender.sendMessage(ChatColor.RED + args[0] + " is not online");
					return true;
				}

				if (!this.frozen.contains(player)) {
					sender.sendMessage(ChatColor.RED + "You have frozen " + player.getName());
					this.frozen.add(player);
				} else {
					sender.sendMessage(ChatColor.RED + "You have un-frozen " + player.getName());
					this.frozen.remove(player);
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "No Permission");
		}

		return true;
	}
}
