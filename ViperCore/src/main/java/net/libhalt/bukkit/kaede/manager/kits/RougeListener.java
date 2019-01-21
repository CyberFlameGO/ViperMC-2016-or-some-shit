package net.libhalt.bukkit.kaede.manager.kits;

import java.util.WeakHashMap;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.Armor;
import net.libhalt.bukkit.kaede.utils.ItemStackUtils;
import net.libhalt.bukkit.kaede.utils.MilliToSecondFormatter;
import net.libhalt.bukkit.kaede.utils.PotionUtils;

public class RougeListener implements Listener {
	private WeakHashMap<Player , Long> stabCoolDown = new WeakHashMap<Player , Long>();
	private WeakHashMap<Player , Long> sugarCoolDown = new WeakHashMap<Player , Long>();
	private KitManager utils;

	public RougeListener(KitManager utils) {
		this.utils = utils;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && event.hasItem() && event.getItem().getType() == Material.SUGAR) {
			Player player = event.getPlayer();
			if (ItemStackUtils.isWearingFull(player, Armor.CHAIN_MAIL) && this.utils.getActiveKit(player) == Armor.CHAIN_MAIL) {
				if (this.sugarCoolDown.containsKey(event.getPlayer())) {
					long value = this.sugarCoolDown.get(event.getPlayer()).longValue();
					if (value > System.currentTimeMillis()) {
						HCFactionPlugin.getInstance().sendLocalized(event.getPlayer(), "ROUGE_INSTANT_EFFECT_COOLDOWN", MilliToSecondFormatter.format(value - System.currentTimeMillis()));
						return;
					}
				}

				this.sugarCoolDown.put(event.getPlayer(), Long.valueOf(System.currentTimeMillis() + 30000L));
				utils.addEffectWithReturn(player, PotionUtils.ROUGE_SPEED_5);
				HCFactionPlugin.getInstance().sendLocalized(event.getPlayer(), "ROUGE_INSTANT_EFFECT");
			}
		}

	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
			Player damager = (Player) event.getDamager();
			LivingEntity damagee = (LivingEntity) event.getEntity();
			ItemStack item = damager.getItemInHand();
			if (item != null && item.getType() == Material.GOLD_SWORD && ItemStackUtils.isWearingFull(damager, Armor.CHAIN_MAIL) && this.utils.getActiveKit(damager) == Armor.CHAIN_MAIL) {
				Location playerLoc = damager.getLocation();
				Location targetLoc = damagee.getLocation();
				double pvecy = -Math.sin(Math.toRadians(playerLoc.getPitch()));
				double pvecx = -Math.cos(Math.toRadians(playerLoc.getPitch())) * Math.sin(Math.toRadians(playerLoc.getYaw()));
				double pvecz = Math.cos(Math.toRadians(playerLoc.getPitch())) * Math.cos(Math.toRadians(playerLoc.getYaw()));
				double tvecy = -Math.sin(Math.toRadians(targetLoc.getPitch()));
				double tvecx = -Math.cos(Math.toRadians(targetLoc.getPitch())) * Math.sin(Math.toRadians(targetLoc.getYaw()));
				double tvecz = Math.cos(Math.toRadians(targetLoc.getPitch())) * Math.cos(Math.toRadians(targetLoc.getYaw()));
				double dot = tvecx * pvecx + tvecy * pvecy + tvecz * pvecz;
				if (dot > 0.6D) {
					if (this.stabCoolDown.containsKey(damager)) {
						long value = this.stabCoolDown.get(damager).longValue();
						if (value > System.currentTimeMillis()) {
							HCFactionPlugin.getInstance().sendLocalized(damager, "ROUGE_BACKSTAB_COOLDOWN", MilliToSecondFormatter.format(value - System.currentTimeMillis()));
							return;
						}
					}

					Damageable damageevar = (Damageable) damagee;
					this.stabCoolDown.put(damager, Long.valueOf(System.currentTimeMillis() + utils.getRougeCooldownMillisecond()));
					damager.setItemInHand((ItemStack) null);
					damagee.setHealth(Math.max(0.0D, damageevar.getHealth() - 6.0D));
					event.setDamage(0.0D);
					damager.getWorld().playEffect(damagee.getLocation(), Effect.STEP_SOUND, 152);
					damager.getWorld().playSound(damagee.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
				}
			}
		}

	}
}
