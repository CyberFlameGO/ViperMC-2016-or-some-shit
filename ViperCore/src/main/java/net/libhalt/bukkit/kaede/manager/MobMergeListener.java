package net.libhalt.bukkit.kaede.manager;

import net.libhalt.dev.plugin.armor.utils.Color;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.Manager;

import java.util.List;

public class MobMergeListener extends Manager implements Listener {
	public MobMergeListener(HCFactionPlugin plugin) {
		super(plugin);
	}


	@Override
	public void init() {
		new BukkitRunnable() {
			public void run() {
				for (final World world : getPlugin().getServer().getWorlds()) {
					if (world.getEnvironment() != World.Environment.THE_END) {
						for (final Entity entity : world.getEntities()) {
							if (entity instanceof Monster || entity instanceof Cow) {
								final LivingEntity living = (LivingEntity)entity;
								if (entity.isDead()) {
									continue;
								}
								List<Entity> nearBy = entity.getNearbyEntities(3.0, 3.0, 3.0);
								if(entity instanceof Cow){
									int cowCount = 0;
									for(Entity entity1 : nearBy){
										if(entity1 instanceof Cow){
											cowCount ++;
										}
									}
									if(cowCount < 33){
										continue;
									}
								}
								for (final Entity otherEntity : nearBy) {
									if (entity.getType() == otherEntity.getType() && otherEntity != living && !otherEntity.isDead()) {
										final LivingEntity otherMonster = (LivingEntity)otherEntity;
										int increment = 1;
										final String name = getNameForEntity(otherEntity.getType());
										if (otherMonster.getCustomName() != null && otherMonster.getCustomName().endsWith(name)) {
											increment = Integer.valueOf(ChatColor.stripColor(otherMonster.getCustomName().replace(name, "").trim()));
										}
										if (living.getCustomName() != null && living.getCustomName().endsWith(name)) {
											final String customName = living.getCustomName().replace(name, "").trim();
											final int value = Integer.valueOf(ChatColor.stripColor(customName));
											living.setCustomName(Color.translate("&a&l") + (Math.min(300 , value + increment)) + name);
											living.setCustomNameVisible(false);
										}else {
											living.setCustomName(Color.translate("&a&l")+ (Math.min(300 , 1 + increment)) + name);
											living.setCustomNameVisible(false);
										}
										otherMonster.remove();
									}
								}
							}
						}
					}
				}
			}
		}.runTaskTimer((Plugin)this.getPlugin(), 50L, 50L);
		this.getPlugin().getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.getPlugin());
	}

	public String getNameForEntity(final EntityType entityType) {
		return " " + Color.translate("&f&l")+ WordUtils.capitalize(entityType.name().replace("_", " ").toLowerCase());
	}
	@EventHandler
	public void onDeath(final EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Horse){
			event.setDamage(event.getDamage() * 2.0D);
		}
	}
	@EventHandler
	public void onDeath(final EntityPortalEnterEvent event) {
		if (!(event.getEntity() instanceof Player) && !(event.getEntity() instanceof Horse)) {
			event.getEntity().remove();
		}
	}
	@EventHandler
	public void onDeath(final EntityDeathEvent event) {
		if (event.getEntity() instanceof Monster || event.getEntity() instanceof Cow) {
			final LivingEntity monster = event.getEntity();
			if (monster.getCustomName() == null) {
				return;
			}
			final String name = this.getNameForEntity(monster.getType());
			if (monster.getCustomName().endsWith(name)) {
				final String customName = monster.getCustomName().replace(name, "").trim();
				final int value = Integer.valueOf(ChatColor.stripColor(customName).trim());
				if (value <= 1) {
					return;
				}
				final Location location = monster.getLocation();
				final EntityType type = monster.getType();
				new BukkitRunnable() {
					public void run() {
						final Entity entity = monster.getWorld().spawnEntity(location, type);
						((LivingEntity)entity).setCustomName(ChatColor.GREEN.toString() + ChatColor.BOLD + (value - 1) + name);
						((LivingEntity)entity).setCustomNameVisible(true);
						if (entity instanceof Zombie) {
							((Zombie)entity).setBaby(false);
						}
					}
				}.runTask((Plugin)this.getPlugin());
			}
		}
	}
}
