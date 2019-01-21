package net.libhalt.bukkit.kaede.manager;

import java.util.Collection;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;

public class PotionFixManager extends Manager implements Listener{
	private boolean[] EnabledFixes = new boolean[3];
	private int[] Amounts = new int[2];
	private ConfigurationWrapper config;

	public YamlConfiguration getConfig(){
		return config.getConfig();
	}
	public PotionFixManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.config = new ConfigurationWrapper("potionfix.yml", this.getPlugin());
		getPlugin().getServer().getPluginManager().registerEvents((Listener)this, getPlugin());

		this.EnabledFixes[0] = this.getConfig().getBoolean("Strength-Fix-Enabled");
		this.EnabledFixes[1] = this.getConfig().getBoolean("Health-Fix-Enabled");
		this.EnabledFixes[2] = this.getConfig().getBoolean("Regeneration-Fix-Enabled");
		this.Amounts[0] = this.getConfig().getInt("Strength-Power-Half-Hearts");
		this.Amounts[1] = this.getConfig().getInt("Health-Power-Half-Hearts");
	}

	@EventHandler
	public void onPlayerDamage(final EntityDamageByEntityEvent event) {
		if (this.EnabledFixes[0] && event.getDamager() instanceof Player) {
			final Player player = (Player)event.getDamager();
			if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
				for (final PotionEffect effect : player.getActivePotionEffects()) {
					if (effect.getType().equals((Object)PotionEffectType.INCREASE_DAMAGE)) {
						final int level = effect.getAmplifier() + 1;
						final double newDamage = event.getDamage(EntityDamageEvent.DamageModifier.BASE) / (level * 1.3 + 1.0) + this.Amounts[0] * level;
						final double damagePercent = newDamage / event.getDamage(EntityDamageEvent.DamageModifier.BASE);
						try {
							event.setDamage(EntityDamageEvent.DamageModifier.ARMOR, event.getDamage(EntityDamageEvent.DamageModifier.ARMOR) * damagePercent);
						}
						catch (Exception ex) {}
						try {
							event.setDamage(EntityDamageEvent.DamageModifier.MAGIC, event.getDamage(EntityDamageEvent.DamageModifier.MAGIC) * damagePercent);
						}
						catch (Exception ex2) {}
						try {
							event.setDamage(EntityDamageEvent.DamageModifier.RESISTANCE, event.getDamage(EntityDamageEvent.DamageModifier.RESISTANCE) * damagePercent);
						}
						catch (Exception ex3) {}
						try {
							event.setDamage(EntityDamageEvent.DamageModifier.BLOCKING, event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) * damagePercent);
						}
						catch (Exception ex4) {}
						event.setDamage(EntityDamageEvent.DamageModifier.BASE, newDamage);
						break;
					}
				}
			}
		}
	}

	@EventHandler
	public void onRegen(final EntityRegainHealthEvent event) {
		if (this.EnabledFixes[1] || this.EnabledFixes[2]) {
			final LivingEntity entity = (LivingEntity) event.getEntity();
			int lvl = 0;
			final Collection<PotionEffect> Effects = (Collection<PotionEffect>)entity.getActivePotionEffects();
			for (final PotionEffect effect : Effects) {
				if (effect.getType().getName() == "REGENERATION" || effect.getType().getName() == "HEAL") {
					lvl = effect.getAmplifier() + 1;
					break;
				}
			}
			if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC_REGEN && event.getAmount() == 1.0 && lvl > 0) {
				if (this.EnabledFixes[2]) {
					getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), (Runnable)new Runnable() {
						@Override
						public void run() {
							Damageable dentity = (Damageable) entity;
							if (dentity.getMaxHealth() >= dentity.getHealth() + 1.0) {
								entity.setHealth(dentity.getHealth() + 1.0);
							}
						}
					}, 50L / (lvl * 2));
				}
			}
			else if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC && event.getAmount() > 1.0 && lvl > 0 && this.EnabledFixes[1]) {
				event.setAmount(event.getAmount() * (this.Amounts[1] * 0.25));
			}
		}
	}

}
