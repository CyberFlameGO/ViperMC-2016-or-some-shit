package net.libhalt.bukkit.kaede.manager;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.Manager;

public class LootingManager extends Manager implements Listener {
	public LootingManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		double multiplier = 1.5D;
		if (event.getEntity().getKiller() != null) {
			Player player = event.getEntity().getKiller();
			if (player.getItemInHand() != null && player.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) {
				multiplier = player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) + 1;
			}
		}

		event.setDroppedExp((int) (event.getDroppedExp() * multiplier));
	}
}
