package net.libhalt.bukkit.kaede.manager.kits;

import java.util.WeakHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.Armor;
import net.libhalt.bukkit.kaede.utils.ItemStackUtils;
import net.libhalt.bukkit.kaede.utils.MilliToSecondFormatter;

public class BardListener implements Listener {
	private KitManager kit;
	private WeakHashMap<Player , Long> bardCoolDown = new WeakHashMap<Player , Long>();

	public BardListener(KitManager kit) {
		this.kit = kit;
	}

	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent event) {
		if (ItemStackUtils.isWearingFull(event.getPlayer(), Armor.GOLD) && this.kit.getActiveKit(event.getPlayer()) == Armor.GOLD) {
			this.kit.applyBard(event.getPlayer(), event.getPlayer().getInventory().getItem(event.getNewSlot()));
		}

	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		ItemStack item = event.getPlayer().getItemInHand();
		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && item !=null &&  this.kit.canBardInstantApply(event.getPlayer(),item) && ItemStackUtils.isWearingFull(event.getPlayer(), Armor.GOLD) && this.kit.getActiveKit(event.getPlayer()) == Armor.GOLD) {
			if (this.bardCoolDown.containsKey(event.getPlayer())) {
				long value = this.bardCoolDown.get(event.getPlayer()).longValue();
				if (value > System.currentTimeMillis()) {
					HCFactionPlugin.getInstance().sendLocalized(event.getPlayer(), "BARD_INSTANT_EFFECT_COOLDOWN", MilliToSecondFormatter.format(value - System.currentTimeMillis()));
					return;
				}
			}

			this.bardCoolDown.put(event.getPlayer(), Long.valueOf(System.currentTimeMillis() + 30000L));
			HCFactionPlugin.getInstance().sendLocalized(event.getPlayer(), "BARD_INSTANT_EFFECT", event.getItem().getType().name().toLowerCase());
			if(this.kit.applyInstantBard(event.getPlayer(), item)){
				if(item.getAmount() > 1){
					item.setAmount(item.getAmount() - 1);
				}else{
					event.getPlayer().setItemInHand(null);
				}
			}
		}

	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
		}
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event){
		Player player = event.getPlayer();
		if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
		}
	}
}
