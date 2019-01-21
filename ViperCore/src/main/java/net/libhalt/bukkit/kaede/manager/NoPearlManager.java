package net.libhalt.bukkit.kaede.manager;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.claim.Position2D;
import net.syuu.popura.faction.bean.ClaimedRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class NoPearlManager extends Manager implements Listener {
	private List<String> regions;
	private ConfigurationWrapper config;

	public YamlConfiguration getConfig(){
		return config.getConfig();
	}
	public NoPearlManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		getPlugin().getServer().getPluginManager().registerEvents(this	 , getPlugin());
		this.config = new ConfigurationWrapper("pearl.yml", this.getPlugin());
		reload();
	}

	@Override
	public void reload() {
		config.reloadConfig();
		regions = config.getConfig().getStringList("PEARL_DISABLE_REGIONS");
	}

	@EventHandler
	public void onPlayerTeleport(final PlayerTeleportEvent event) {
		if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
			final Location to = event.getTo();
			final ClaimedRegion region = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(to.getWorld().getName(), to.getBlockX(), to.getBlockZ()));
			if (region != null && regions.contains(region.getOwner().getName())) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "EnderPearle is disabled in this region.");
			}
		}
	}

}
