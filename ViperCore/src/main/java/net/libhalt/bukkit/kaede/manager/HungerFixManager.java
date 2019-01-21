package net.libhalt.bukkit.kaede.manager;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.List;
import java.util.Random;

public class HungerFixManager extends Manager implements Listener {

	public HungerFixManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
	}

	@EventHandler
	public void FixIt(final FoodLevelChangeEvent e) {
		if (e.getFoodLevel() < ((Player)e.getEntity()).getFoodLevel() && new Random().nextInt(100) > 4) {
			e.setCancelled(true);
		}
	}
}
