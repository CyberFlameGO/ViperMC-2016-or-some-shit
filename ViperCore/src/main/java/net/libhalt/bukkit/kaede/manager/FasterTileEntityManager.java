package net.libhalt.bukkit.kaede.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;

public class FasterTileEntityManager /*extends Manager implements Listener */{
	/*
	private ConfigurationWrapper config;
	private Map<Location , BrewingStand> activeStands = new HashMap<Location , BrewingStand>();
	private int brewingStandMultipliar;
	private int furnaceMultipliar;

	public FasterTileEntityManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.config = new ConfigurationWrapper("tile-entity-multiplier.yml", this.getPlugin());
		this.reload();
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
		(new FasterTileEntityManager.BrewingUpdateTask()).runTaskTimer(this.getPlugin(), 1L, 1L);
	}

	@Override
	public void reload() {
		this.brewingStandMultipliar = this.config.getConfig().getInt("potionbrewer-speed-multiplier") - 1;
		this.furnaceMultipliar = this.config.getConfig().getInt("furnace-speed-multiplier") - 1;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (this.brewingStandMultipliar > 1 && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			BlockState state = event.getClickedBlock().getState();
			if (state instanceof BrewingStand) {
				this.activeStands.put(state.getLocation(), (BrewingStand) state);
			}
		}

	}

	@EventHandler
	public void onFurnaceBurn(FurnaceBurnEvent event) {
		BlockState state = event.getBlock().getState();
		if (this.furnaceMultipliar > 1 && state instanceof Furnace) {
			(new FasterTileEntityManager.FurnaceUpdateTask((Furnace) state)).runTaskTimer(this.getPlugin(), 1L, 1L);
		}

	}

	public class BrewingUpdateTask extends BukkitRunnable {
		@Override
		public void run() {
			Iterator<Entry<Location , BrewingStand>> iter = FasterTileEntityManager.this.activeStands.entrySet().iterator();

			while (iter.hasNext()) {
				Entry<Location , BrewingStand> entry = iter.next();
				if ((!entry.getValue().getChunk().isLoaded() ) || entry.getKey().getBlock().getType() != Material.BREWING_STAND) {
					iter.remove();
				} else {
					BrewingStand stand = entry.getValue();
					if (stand.getBrewingTime() > 1) {
						stand.setBrewingTime(Math.max(1, stand.getBrewingTime() - FasterTileEntityManager.this.brewingStandMultipliar));
					}
				}
			}

		}
	}

	public class FurnaceUpdateTask extends BukkitRunnable {
		private Furnace furnace;

		public FurnaceUpdateTask(Furnace furnace) {
			this.furnace = furnace;
		}

		@Override
		public void run() {
			if(furnace.getLocation().getBlock().getType() != Material.BURNING_FURNACE && furnace.getLocation().getBlock().getType() != Material.FURNACE ){
				this.cancel();
				return;
			}
			if (this.furnace.getBurnTime() <= 1) {
				this.cancel();
				return;
			}
			this.furnace.setCookTime((short) (this.furnace.getCookTime() + FasterTileEntityManager.this.furnaceMultipliar));
			this.furnace.setBurnTime((short) Math.max(1, this.furnace.getBurnTime() - FasterTileEntityManager.this.furnaceMultipliar));
			this.furnace.update();
			if (this.furnace.getBurnTime() <= 1) {
				this.cancel();
			}

		}
	}
	*/
}
