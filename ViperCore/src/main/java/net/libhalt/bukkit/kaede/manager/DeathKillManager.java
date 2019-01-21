package net.libhalt.bukkit.kaede.manager;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.PlayerData;
import net.libhalt.bukkit.kaede.utils.Manager;

public class DeathKillManager extends Manager implements Listener {
	public DeathKillManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onDeath(PlayerDeathEvent event) {
		Player died = event.getEntity();
		PlayerData data = getPlugin().getPlayerDataManager().getPlayerData(died);
		if(System.currentTimeMillis() - data.getCreatedTime() < 10000){
			return;
		}
		Player killer = died.getKiller();
		died.getWorld().strikeLightningEffect(died.getLocation());
		if (killer != null) {
			PlayerData killerData = this.getPlugin().getPlayerDataManager().getPlayerData(killer);
			PlayerData diedData = this.getPlugin().getPlayerDataManager().getPlayerData(died);
			killerData.setKills(killerData.getKills() + 1);
			killer.setStatistic(Statistic.PLAYER_KILLS, killerData.getKills());
			/*
			String message = ChatColor.RED + died.getName() + ChatColor.DARK_RED + "[" + diedData.getKills() + "] " + ChatColor.YELLOW + "was slain by " + ChatColor.RED + killer.getName() + ChatColor.DARK_RED + "[" + killerData.getKills() + "]";
			if (killer.getItemInHand() != null) {
				ItemStack item = killer.getItemInHand();
				String name;
				if (item.getItemMeta().hasDisplayName()) {
					name = item.getItemMeta().getDisplayName();
				} else {
					name = item.getType().name().toLowerCase().replace("_", " ");
				}

				message = message + ChatColor.YELLOW + " using a " + ChatColor.RED + name;
			}

			event.setDeathMessage(message);
			*/
		}
	}
}
