package net.libhalt.bukkit.kaede.manager;

import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.faction.bean.FactionPlayer;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.Manager;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MiscDisableManager extends Manager implements Listener {
	public MiscDisableManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		event.blockList().clear();
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		if (block.getType() == Material.MOB_SPAWNER && block.getWorld().getEnvironment() == Environment.NETHER) {
			FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(event.getPlayer());
			if(factionPlayer != null && factionPlayer.getFaction() != null && factionPlayer.getFaction().getName().equalsIgnoreCase("GlowstoneMtn")){
				return;
			}
			event.setCancelled(true);
			getPlugin().sendLocalized(event.getPlayer(), "NETHER_SPAWNER_PROTECTED");
		}

	}
	@EventHandler
	public void onBlockBreak(final EntitySpawnEvent event) {
		if(event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof Player)){
			new BukkitRunnable(){
				@Override
				public void run() {

					LivingEntity livingEntity = (LivingEntity) event.getEntity();
					livingEntity.getEquipment().setHelmet(null);
					livingEntity.getEquipment().setChestplate(null);
					livingEntity.getEquipment().setLeggings(null);
					livingEntity.getEquipment().setBoots(null);
					livingEntity.getEquipment().setItemInHand(null);
					livingEntity.setCanPickupItems(false);
				}
			}.runTaskLater(getPlugin() , 3);
		}

	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getType() == Material.MOB_SPAWNER && block.getWorld().getEnvironment() == Environment.NETHER) {
			event.setCancelled(true);
			getPlugin().sendLocalized(event.getPlayer(), "NETHER_SPAWNER_PROTECTED");
		}

	}
}
