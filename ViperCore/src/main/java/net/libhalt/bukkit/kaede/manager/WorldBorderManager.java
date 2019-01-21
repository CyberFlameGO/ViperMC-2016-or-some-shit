package net.libhalt.bukkit.kaede.manager;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;

public class WorldBorderManager extends Manager implements Listener {
	private ConfigurationWrapper config;
	private int bordernether;
	private int borderworld;
	private int borderend;

	public WorldBorderManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.config = new ConfigurationWrapper("world-border.yml", this.getPlugin());
		this.reload();
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
	}

	@Override
	public void reload() {
		this.borderend = this.config.getConfig().getInt("border-end");
		this.bordernether = this.config.getConfig().getInt("border-nether");
		this.borderworld = this.config.getConfig().getInt("border-world");
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Location to = event.getTo();
		int border = this.borderworld;
		if (to.getWorld().getEnvironment() == Environment.NETHER) {
			border = this.bordernether;
		}

		if (to.getWorld().getEnvironment() == Environment.THE_END) {
			border = this.borderend;
		}

		if (Math.abs(to.getX()) > border || Math.abs(to.getZ()) > border) {
			event.setTo(event.getFrom());
			getPlugin().sendLocalized(event.getPlayer(), "WORLD_BOREDER_REACHED");
		}
	}
}
