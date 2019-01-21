package net.libhalt.bukkit.kaede.manager;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;

public class PotionLimitManager extends Manager implements Listener {
	private ConfigurationWrapper config;
	private List<Short> disabledPotions;

	public PotionLimitManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.config = new ConfigurationWrapper("potion-limiter.yml", this.getPlugin());
		this.reload();
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
	}

	@Override
	public void reload() {
		this.disabledPotions = this.config.getConfig().getShortList("disabled-potions");
	}

	public boolean isPotionDisabled(ItemStack item) {
		return item.getType() == Material.POTION ? this.disabledPotions.contains(Short.valueOf(item.getDurability())) : false;
	}

	@EventHandler
	public void onSplash(PotionSplashEvent event) {
		if (this.isPotionDisabled(event.getPotion().getItem())) {
			event.setCancelled(true);
			ProjectileSource shooter = event.getEntity().getShooter();
			if (shooter instanceof Player) {
				getPlugin().sendLocalized((Player)shooter, "POTION_DISABLED");
			}
		}

	}

	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
		if (this.isPotionDisabled(event.getItem())) {
			event.setCancelled(true);
			getPlugin().sendLocalized(event.getPlayer(), "POTION_DISABLED");
		}

	}
}
