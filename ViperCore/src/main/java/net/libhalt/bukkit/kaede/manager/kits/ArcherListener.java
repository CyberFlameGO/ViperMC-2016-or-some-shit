package net.libhalt.bukkit.kaede.manager.kits;

import java.util.WeakHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
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
import net.libhalt.bukkit.kaede.utils.Eval;
import net.libhalt.bukkit.kaede.utils.ItemStackUtils;
import net.libhalt.bukkit.kaede.utils.MilliToSecondFormatter;
import net.libhalt.bukkit.kaede.utils.PotionUtils;

public class ArcherListener implements Listener {
	private WeakHashMap<Player , Long> sugarCoolDown = new WeakHashMap<Player , Long>();
	private KitManager utils;

	public ArcherListener(KitManager utils) {
		this.utils = utils;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && event.hasItem() && event.getItem().getType() == Material.SUGAR) {
			Player player = event.getPlayer();
			if (ItemStackUtils.isWearingFull(player, Armor.LEATHER) && this.utils.getActiveKit(event.getPlayer()) == Armor.LEATHER) {
				if (this.sugarCoolDown.containsKey(event.getPlayer())) {
					long value = this.sugarCoolDown.get(event.getPlayer());
					if (value > System.currentTimeMillis()) {
						HCFactionPlugin.getInstance().sendLocalized(player, "ARCHER_INSTANT_EFFECT_COOLDOWN", MilliToSecondFormatter.format(value - System.currentTimeMillis()) );
						return;
					}
				}
				ItemStack item = event.getItem();
					
				if(item.getAmount() > 1){
					item.setAmount(item.getAmount() - 1);
				}else{
					event.getPlayer().setItemInHand(null);
				}
				this.sugarCoolDown.put(event.getPlayer(), System.currentTimeMillis() + 30000L);
				utils.addEffectWithReturn(player, PotionUtils.ROUGE_SPEED_5);
				HCFactionPlugin.getInstance().sendLocalized(player, "ARCHER_INSTANT_EFFECT", "Speed");
			}
		}

	}

	@EventHandler(priority = EventPriority.HIGH , ignoreCancelled = true)
	public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			if (arrow.getShooter() instanceof Player) {
				Player player = (Player) arrow.getShooter();
				if (ItemStackUtils.isWearingFull(player, Armor.LEATHER) && this.utils.getActiveKit(player) == Armor.LEATHER) {
					double distance = player.getLocation().distance(event.getEntity().getLocation());
					double damage = Eval.eval(utils.getArcherFormula().replace("damage", String.valueOf(event.getDamage())).replace("distance", String.valueOf(distance)));
					event.setDamage(damage);
					HCFactionPlugin.getInstance().sendLocalized(player, "ARCHER_WITH_RANGE", (int) distance , 1.5D ,  Math.floor(event.getDamage()) );
				}
			}
		}

	}

	public static double flatDistance(Location one, Location other) {
		return Math.sqrt((one.getX() - other.getX()) * (one.getX() - other.getX()) + (one.getZ() - other.getZ()) * (one.getZ() - other.getZ()));
	}
}
