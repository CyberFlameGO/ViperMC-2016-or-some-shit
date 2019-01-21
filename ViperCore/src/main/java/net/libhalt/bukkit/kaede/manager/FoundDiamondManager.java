package net.libhalt.bukkit.kaede.manager;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.Manager;

import java.util.Set;
import java.util.UUID;

public class FoundDiamondManager extends Manager implements Listener {
	private FixedMetadataValue tag;
	private Set<UUID> noNotifications = Sets.newHashSet();

	public FoundDiamondManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
		this.tag = new FixedMetadataValue(this.getPlugin(), Boolean.TRUE);
		getPlugin().getCommand("td").setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
				UUID uuid = ((Player)sender).getUniqueId();
				if(noNotifications.contains(uuid)){
					noNotifications.remove(uuid);
					getPlugin().sendLocalized((Player)sender , "FOUND_DIAMOND_NOTIFICATION_ON");
				}else{
					noNotifications.add(uuid);
					getPlugin().sendLocalized((Player)sender , "FOUND_DIAMOND_NOTIFICATION_OFF");
				}
				return false;
			}
		});
	}

	@EventHandler
	public void onPlayerQUit(PlayerQuitEvent event){
		noNotifications.remove(event.getPlayer().getUniqueId());
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		if (block.getType() == Material.DIAMOND_ORE) {
			block.setMetadata("PlacedBlock", this.tag);
		}

	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block base = event.getBlock();
		if (base.getType() == Material.DIAMOND_ORE && !base.hasMetadata("PlacedBlock")) {
			int total = 0;

			for (int x = -3; x < 4; ++x) {
				for (int y = -3; y < 4; ++y) {
					for (int z = -3; z < 4; ++z) {
						Block block = event.getBlock().getRelative(x, y, z);
						if (block.getType() == Material.DIAMOND_ORE && !block.hasMetadata("PlacedBlock")) {
							++total;
							block.setMetadata("PlacedBlock", this.tag);
						}
					}
				}
			}
			for(Player player : Bukkit.getOnlinePlayers()){
				if(!noNotifications.contains(player.getUniqueId())){
					if(total <= 1){
						player.sendMessage("[FD] " + ChatColor.AQUA + event.getPlayer().getName() + " found " + total + " diamond.");
					}else {
						player.sendMessage("[FD] " + ChatColor.AQUA + event.getPlayer().getName() + " found " + total + " diamonds.");
					}
				}
			}
		}

	}
}
