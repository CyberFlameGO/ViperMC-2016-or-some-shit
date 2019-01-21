package net.libhalt.bukkit.kaede.manager;

import net.libhalt.bukkit.kaede.PlayerData;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;

import java.io.IOException;

public class EndWorldManager extends Manager implements Listener {
	private Vector leave;
	private float leaveYaw;
	private float leavePitch;
	private Vector enter;

	private float enterYaw;
	private float enterPitch;
	private ConfigurationWrapper config;

	public EndWorldManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.config = new ConfigurationWrapper("world-end.yml", this.getPlugin());
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
		this.getPlugin().getCommand("setendexit").setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
				Player player = ((Player)sender);
				leave = player.getLocation().toVector();
				leaveYaw = player.getLocation().getYaw();
				leavePitch = player.getLocation().getPitch();
				config.getConfig().set("end-exit" , leave.getBlockX() + "," + leave.getBlockY() + "," + leave.getBlockZ() + "," + leaveYaw + "," + leavePitch);
				try {
					config.getConfig().save(config.getFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
				player.sendMessage("End exit set");
				return true;
			}
		});
		this.getPlugin().getCommand("setendspawn").setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
				Player player = ((Player)sender);
				enter = player.getLocation().toVector();
				enterYaw = player.getLocation().getYaw();
				enterPitch = player.getLocation().getPitch();
				config.getConfig().set("end-spawn" , enter.getBlockX() + "," + enter.getBlockY() + "," + enter.getBlockZ() + "," + enterYaw + "," + enterPitch);
				try {
					config.getConfig().save(config.getFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
				player.sendMessage("End spawn set");

				return true;
			}
		});
		this.reload();
	}


	@Override
	public void reload() {

		String string = this.config.getConfig().getString("end-exit");
		String[] result = string.split(",");
		leave = new Vector(Integer.valueOf(result[0]).intValue() , Integer.valueOf(result[1]).intValue() ,  Integer.valueOf(result[2]).intValue());
		leaveYaw = Float.valueOf(result[3]);
		leavePitch = Float.valueOf(result[4]);

		YamlConfiguration actualConfig = YamlConfiguration.loadConfiguration(this.config.getFile());
		if(actualConfig.contains("end-spawn")){
			String enter = actualConfig.getString("end-spawn");
			String[] resultenter = enter.split(",");
			this.enter = new Vector(Integer.valueOf(resultenter[0]).intValue() , Integer.valueOf(resultenter[1]).intValue() ,  Integer.valueOf(resultenter[2]).intValue());
			enterYaw = Float.valueOf(result[3]);

			enterPitch = Float.valueOf(result[4]);
		}
	}
	@EventHandler
	public void onWorldTeleport(final PlayerChangedWorldEvent event){

		new BukkitRunnable(){
			@Override
			public void run() {
				PlayerData data = getPlugin().getPlayerDataManager().getPlayerData(event.getPlayer());
				if (data.getPvpTime() > 0) {
					return;
				}
				final World world = getWorld(World.Environment.THE_END);
				if(event.getPlayer().getWorld() == world){
					event.getPlayer().teleport(enter.toLocation(world, enterYaw, enterPitch));
				}
			}
		}.runTaskLater(getPlugin() , 1);
	}
	@EventHandler
	public void onWorldChange(final PlayerTeleportEvent event){
			if(event.getTo().getWorld() != event.getFrom().getWorld()){
				PlayerData data = this.getPlugin().getPlayerDataManager().getPlayerData(event.getPlayer());
				if (data.getPvpTime() > 0) {
					event.getPlayer().sendMessage(ChatColor.RED + "You may not change world while pvp timer is enabled.");
					event.setCancelled(true);
					return;
				}
			}
	}
	@EventHandler
	public void onPlayerMove(final PlayerMoveEvent event) {
		final Location from = event.getFrom();
		final Location to = event.getTo();
		final Player player = event.getPlayer();
		if ((from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ() || from.getBlockY() != to.getBlockY())) {
			World.Environment environment = player.getWorld().getEnvironment();
			if (environment == World.Environment.NORMAL) {
					if (player.getLocation().getBlock().getType() == Material.ENDER_PORTAL) {


						PlayerData data = this.getPlugin().getPlayerDataManager().getPlayerData(event.getPlayer());
						if (data.getPvpTime() > 0) {
							return;
						}
						final World world = this.getWorld(World.Environment.THE_END);
						player.teleport(enter.toLocation(world, enterYaw, enterPitch));
					}
			}else if(environment == World.Environment.THE_END) {
				if( (player.getLocation().add(0.0, -1.0, 0.0).getBlock().getType() == Material.STATIONARY_WATER || player.getLocation().getBlock().getType() == Material.STATIONARY_WATER)) {

					final World world = this.getWorld(World.Environment.NORMAL);
					player.teleport(leave.toLocation(world ,leaveYaw , leavePitch));

				}
			}
		}
	}

	public World getWorld(final World.Environment environment) {
		for (final World world : Bukkit.getWorlds()) {
			if (world.getEnvironment() == environment) {
				return world;
			}
		}
		return null;
	}

}
