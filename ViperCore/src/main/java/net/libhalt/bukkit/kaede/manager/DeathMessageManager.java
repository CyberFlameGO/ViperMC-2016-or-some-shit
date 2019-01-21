package net.libhalt.bukkit.kaede.manager;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.PlayerData;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;

public class DeathMessageManager extends Manager implements Listener{
	
	private ConfigurationWrapper config;
	public DeathMessageManager(HCFactionPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void init() {
		config = new ConfigurationWrapper("death-messages.yml", getPlugin());
		getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
	}
	
	public String tranlasteAndGet(String message){
		return ChatColor.translateAlternateColorCodes('&', config.getConfig().getString(message));
	}
	

	public String toReadable(ItemStack item){
		if(item.hasItemMeta()){
			ItemMeta meta = item.getItemMeta();
			if(meta.hasDisplayName()){
				return meta.getDisplayName();
			}
		}
		return toReadable(item.getType());
	}
	public String toReadable(Enum enu){
		return WordUtils.capitalize(enu.name().replace("_", " ").toLowerCase());
	}

	@EventHandler
	public void onEntityDeath(PlayerDeathEvent event){
		Player player = event.getEntity();
		DamageCause cause = player.getLastDamageCause() == null ? DamageCause.CUSTOM : player.getLastDamageCause().getCause();
		if(!config.getConfig().contains(cause.name())){
			cause = DamageCause.CUSTOM;
		}
		String message = null;
		if(cause == DamageCause.ENTITY_ATTACK){
			if(player.getLastDamageCause() instanceof EntityDamageByEntityEvent){
				EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) player.getLastDamageCause();
				if(damageEvent.getDamager() instanceof Player){
					Player damager = (Player) damageEvent.getDamager();
					if(damager.getItemInHand() != null && damager.getItemInHand().getType() != Material.AIR){
						message = tranlasteAndGet("ENTITY_ATTACK.PLAYER");
						message = message.replace("{killer.hand}", toReadable(damager.getItemInHand()));

					}else{
						message = tranlasteAndGet("ENTITY_ATTACK.PLAYER_NOITEM");
					}
					PlayerData data = getPlugin().getPlayerDataManager().getPlayerData(damager);
					if (data != null) {
						message = message.replace("{killer}", damager.getName());
						message = message.replace("{killer.kills}", String.valueOf(data.getKills()));
					}
				}else{
					message = tranlasteAndGet("ENTITY_ATTACK.ENTITY");
					message = message.replace("{killer}", toReadable(damageEvent.getDamager().getType()));
				}
			}else{
				message = tranlasteAndGet("ENTITY_ATTACK.ENTITY");
				message = message.replace("{killer}", "Unknown");
			}
		}else if(cause == DamageCause.PROJECTILE){
			if(player.getLastDamageCause() instanceof EntityDamageByEntityEvent){
				EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) player.getLastDamageCause();
				if(damageEvent.getDamager() instanceof Projectile){
					Projectile projectile = (Projectile) damageEvent.getDamager();
					if(projectile.getShooter() instanceof Player){

						Player damager = (Player) projectile.getShooter();
						if(damager.getItemInHand() != null && damager.getItemInHand().getType() != Material.AIR){
							message = tranlasteAndGet("PROJECTILE.PLAYER");
							message = message.replace("{killer.hand}", toReadable(damager.getItemInHand()));

						}else{
							message = tranlasteAndGet("PROJECTILE.PLAYER_NOITEM");
						}
						PlayerData data = getPlugin().getPlayerDataManager().getPlayerData(damager);
						message = message.replace("{killer}", damager.getName());
						message = message.replace("{killer.kills}", String.valueOf(data.getKills()));
					}else if(projectile.getShooter() instanceof Entity){
						message = tranlasteAndGet("PROJECTILE.ENTITY");
						message = message.replace("{killer}", toReadable(((Entity)projectile.getShooter()).getType()));
					}else{

						message = tranlasteAndGet("PROJECTILE.ENTITY");
						message = message.replace("{killer}", "Unknown");
					}
				}else{
					message = tranlasteAndGet("PROJECTILE.ENTITY");
					message = message.replace("{killer}", "Unknown");

				}
			}else{
				message = tranlasteAndGet("ENTITY_ATTACK.ENTITY");
				message = message.replace("{killer}", "Unknown");
			}
		}else if(cause == DamageCause.CUSTOM){
			message = tranlasteAndGet("UNKNOWN");

		}else if(cause != null){
			message = tranlasteAndGet(cause.name());
		}
		if(message != null){
			message = message.replace("{player}", player.getName());
			PlayerData data = getPlugin().getPlayerDataManager().getPlayerData(player);
			message = message.replace("{player.kills}", String.valueOf(data.getKills()));
			event.setDeathMessage(message);
			Bukkit.getLogger().info(message);
		}
	}
}
