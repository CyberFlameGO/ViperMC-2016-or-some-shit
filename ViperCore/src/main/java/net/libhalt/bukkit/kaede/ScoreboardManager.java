package net.libhalt.bukkit.kaede;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;

import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;

public class ScoreboardManager extends Manager implements Listener {
	private ConfigurationWrapper config;

	public ScoreboardManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.config = new ConfigurationWrapper("scoreboard.yml", this.getPlugin());
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());


	}
	public boolean useDecimalForEnderPearls(){
		return config.getConfig().getBoolean("formatter.ender-pearl.use-decimals");
	}
	
	public boolean useDecimalForSpawnTag(){
		return config.getConfig().getBoolean("formatter.spawn-tag.use-decimals");
	}
	
	public boolean isTabsEnabled(){
		return config.getConfig().getBoolean("drtshock-relation-colors");
	}
	public boolean isAlwaysDisplay(){
		return config.getConfig().getBoolean("always-display" , false);
	}
	public boolean useMinuteSeperatorForPvPTimer(){
		return config.getConfig().getBoolean("formatter.pvp-timer.use-minute-seperator" , false);
	}
	
	public boolean appendZeroForMinuteAsPvPTimer(){
		return config.getConfig().getBoolean("formatter.pvp-timer.append-zero-for-minute" , false);
	}

	public List<String> getConfiguredContext() {
		return this.config.getConfig().getStringList("context");
	}


}
