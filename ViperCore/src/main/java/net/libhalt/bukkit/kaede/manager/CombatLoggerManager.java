package net.libhalt.bukkit.kaede.manager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.igodlik3.vipermisc.Misc;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.PlayerData;
import net.libhalt.bukkit.kaede.event.CombatLoggerDeathEvent;
import net.libhalt.bukkit.kaede.support.IRegion;
import net.libhalt.bukkit.kaede.support.RegionSupport;
import net.libhalt.bukkit.kaede.support.Support;
import net.libhalt.bukkit.kaede.utils.Manager;
import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.faction.bean.Faction;
import net.syuu.popura.faction.bean.FactionPlayer;
import net.syuu.popura.listener.FactionListener;
import net.syuu.popura.util.ImprovedOfflinePlayer;

public class CombatLoggerManager extends Manager implements Listener , CommandExecutor{
	private Set<Player> loggingOut = new HashSet<Player>();
	public CombatLoggerManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
		getPlugin().getCommand("logout").setExecutor(this);
	}

	@Override
	public void tear() {
		for(World world : Bukkit.getWorlds()){
			for(Entity entity : world.getEntities()){
				if (entity.hasMetadata("CombatTagFra")) {
					entity.removeMetadata("CombatTagFra", this.getPlugin());
					entity.removeMetadata("Inventory", this.getPlugin());
					entity.removeMetadata("Armor", this.getPlugin());
					entity.remove();
				}
			}
		}

	}

	public void onLogout(Player player) {
		Damageable dplayer = (Damageable) player;
		if (!Misc.getInstance().getSotwManager().isSOTWActive() && dplayer.getHealth() >= 0.0D && !player.isDead() && !player.hasMetadata("SafeLogout") && !player.hasPermission("combatlogger.bypass")) {
			for(RegionSupport support : Support.getInstance().getRegionSupporters()){
				for(IRegion region : support.getRegionsAt(player.getLocation())){
					if(region.shouldPvPTimerFreeze()){
						return;
					}
				}
			}
			Set<Player> allies = Support.getInstance().getEveryoneInTeam(player);
			for(Entity entity : player.getNearbyEntities(4.0D , 4.0D , 4.0D)){
				if(entity instanceof Villager){
					final Villager villager1 = (Villager) entity;
					if(villager1.getCustomName() != null && villager1.getCustomName().equals(player.getName())){
						return;
					}
				}
			}
			PlayerData data = getPlugin().getPlayerDataManager().getPlayerData(player);
			if(data == null || data.getPvpTime() > 0){
				return;
			}
			for(Entity entity : player.getNearbyEntities(30.0D, 40.0D, 30.0D)){
				if (entity != player && entity instanceof Player && !allies.contains(entity)) {
					final Villager villager1 = player.getWorld().spawn(player.getLocation(), Villager.class);
					villager1.setCustomName(player.getName());
					villager1.setCustomNameVisible(true);
					villager1.setMetadata("CombatTagFra", new FixedMetadataValue(this.getPlugin(), player.getUniqueId().toString()));
					villager1.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100));
					villager1.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 100));
					Damageable dvillager = (Damageable) villager1;
					dvillager.setMaxHealth(50.0D);
					villager1.setHealth(dvillager.getMaxHealth());
					villager1.setMetadata("Inventory", new FixedMetadataValue(this.getPlugin(), player.getInventory().getContents()));
					villager1.setMetadata("Armor", new FixedMetadataValue(this.getPlugin(), player.getInventory().getArmorContents()));
					(new BukkitRunnable() {
						@Override
						public void run() {
							if (!villager1.isDead()) {
								villager1.removeMetadata("CombatTagFra", CombatLoggerManager.this.getPlugin());
								villager1.removeMetadata("Inventory", CombatLoggerManager.this.getPlugin());
								villager1.removeMetadata("Armor", CombatLoggerManager.this.getPlugin());
								villager1.remove();
							}

						}
					}).runTaskLater(this.getPlugin(), 300L);
					return;

				}
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity.hasMetadata("CombatTagFra")) {
			LivingEntity villager = entity;
			Player killer = event.getEntity().getKiller();
			if (killer != null) {
				PlayerData items = this.getPlugin().getPlayerDataManager().getPlayerData(killer);
				String armor = ChatColor.YELLOW + "(CombatLogger) " + ChatColor.RED + villager.getCustomName() + ChatColor.YELLOW + " was slain by " + ChatColor.RED + killer.getName() + ChatColor.DARK_RED + "[" + items.getKills() + "]";
				Bukkit.broadcastMessage(armor);
			} else {
				Bukkit.broadcastMessage(ChatColor.YELLOW + "(CombatLogger)" + ChatColor.RED + villager.getCustomName() + " died.");
			}

			ItemStack[] var12 = (ItemStack[]) villager.getMetadata("Inventory").get(0).value();
			ItemStack[] var13 = (ItemStack[]) villager.getMetadata("Armor").get(0).value();
			Player died = this.getPlugin().getServer().getPlayer(villager.getCustomName());
			if (died != null) {
				died.getInventory().setContents(new ItemStack[died.getInventory().getContents().length]);
				died.getInventory().setArmorContents(new ItemStack[4]);
				died.setHealth(0.0D);
			}

			ItemStack[] var11 = var12;
			int var10 = var12.length;

			ItemStack diedUUID;
			int offline;
			for (offline = 0; offline < var10; ++offline) {
				diedUUID = var11[offline];
				if (diedUUID != null && diedUUID.getType() != Material.AIR) {
					entity.getWorld().dropItemNaturally(entity.getLocation(), diedUUID);
				}
			}

			var11 = var13;
			var10 = var13.length;

			for (offline = 0; offline < var10; ++offline) {
				diedUUID = var11[offline];
				if (diedUUID != null && diedUUID.getType() != Material.AIR) {
					entity.getWorld().dropItemNaturally(entity.getLocation(), diedUUID);
				}
			}

			String var14 = villager.getMetadata("CombatTagFra").get(0).asString();
			final UUID theUUID = UUID.fromString(var14);
			final FactionPlayer killerplayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(killer);
			killerplayer.setKill(killerplayer.getKill() + 1);
			final FactionPlayer death = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(theUUID);
			if (death != null) {
				death.setDeath(death.getDeath() + 1);
				final Faction faction = death.getFaction();
				if (faction != null && died == null) {
					faction.freezeDTR(PopuraPlugin.getInstance().getPopura().getPopuraConfig().getDtrFreeze());
					final double dtr = faction.getDtr();
					faction.setDtr(dtr - 1.0);
					faction.sendLocalizedText("FACTION_MEMBER_DEATH", death);
					faction.sendLocalizedText("FACTION_DTR_CHANGE", FactionListener.formatter.format(dtr), faction);
				}
			}

			new BukkitRunnable(){
				@Override
				public void run() {
					PopuraPlugin.getInstance().getPopura().getDeathBanManager().ban(theUUID);
					ImprovedOfflinePlayer improvedOfflinePlayer = new ImprovedOfflinePlayer(theUUID);
					improvedOfflinePlayer.setHealthFloat(0.0F);
					org.bukkit.inventory.PlayerInventory playerInventory = improvedOfflinePlayer.getInventory();
					playerInventory.clear();
					playerInventory.setArmorContents(new ItemStack[4]);
					improvedOfflinePlayer.setInventory(playerInventory);
				}
			}.runTaskAsynchronously(getPlugin());
			PlayerData var15 = this.getPlugin().getPlayerDataManager().getOfflinePlayerData(var14);
			var15.setKillOnLogin(false);
			this.getPlugin().getPlayerDataManager().savePlayerData(var14, var15);
			entity.getWorld().strikeLightningEffect(entity.getLocation());
			String killerName = "UNKNOWN";
			if(killer != null){
				killerName = killer.getName();
			}
			if(!getPlugin().isDisabled(DeathSignManager.class)){
				entity.getWorld().dropItemNaturally(entity.getLocation(), getPlugin().getManager(DeathSignManager.class).getDeathSign(killerName, entity.getCustomName()));
			}
			if(!getPlugin().isDisabled(DeathKillManager.class)){
				PlayerData killerData = getPlugin().getPlayerDataManager().getPlayerData(killer);
				killerData.setKills(killerData.getKills() + 1);
				killer.setStatistic(Statistic.PLAYER_KILLS, killerData.getKills());
			}

			CombatLoggerDeathEvent newEevent = new CombatLoggerDeathEvent(entity, UUID.fromString(var14));
			
			getPlugin().getServer().getPluginManager().callEvent(newEevent);
		}

	}

	public void onBlockRedstoneChange(BlockRedstoneEvent event)
	{
		if(event.getBlock().getType() == Material.STONE_PLATE )
		{
			boolean pressedByPlayer=false;
			for(Entity e : event.getBlock().getChunk().getEntities())
			{

				if(
						Location.locToBlock(e.getLocation().getX()) - event.getBlock().getX() <= 1 &&
								Location.locToBlock(e.getLocation().getZ()) - event.getBlock().getZ() <= 1 &&
								e instanceof Player
						)
				{
					pressedByPlayer = true;
					break;
				}

			}
			if(!pressedByPlayer)
			{
				event.setNewCurrent(0);
			}
		}
	}
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().hasMetadata("CombatTagFra")) {
			event.setCancelled(true);
		}

	}

	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event) {
		for(Entity entity : event.getChunk().getEntities()){
			if (entity.hasMetadata("CombatTagFra") && !entity.isDead()) {
				event.setCancelled(true);
			}
		}

	}

	@EventHandler
	public void onEntityDamageByEntity(final EntityDamageByEntityEvent event){
		if(event.getEntity().hasMetadata("CombatTagFra")){
			String var14 = event.getEntity().getMetadata("CombatTagFra").get(0).asString();
			if(event.getDamager() instanceof Player){
				Player player = (Player) event.getDamager();
				FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
				if(factionPlayer != null && factionPlayer.getFaction() != null){
					for(FactionPlayer others : factionPlayer.getFaction().getPlayers()){
						if(others.getUuid().toString().equals(var14)){
							event.setCancelled(true);
							break;
						}
					}
				}
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					event.getEntity().setVelocity(new Vector());
				}
			}.runTask(getPlugin());
		}
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		for(Entity entity : event.getPlayer().getWorld().getEntitiesByClass(Villager.class)){
			Villager villager = (Villager) entity;
			if (villager.isCustomNameVisible() && villager.getCustomName().equals(event.getPlayer().getName())) {
				villager.removeMetadata("CombatTagFra", this.getPlugin());
				villager.removeMetadata("Inventory", this.getPlugin());
				villager.removeMetadata("Armor", this.getPlugin());
				villager.remove();
			}
		}

	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		this.onLogout(event.getPlayer());

		if(event.getPlayer().hasMetadata("SafeLogout")){
			event.getPlayer().removeMetadata("SafeLogout", getPlugin());
		}
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		this.onLogout(event.getPlayer());
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		Location to = event.getTo();
		Location from = event.getFrom();
		if(to.getBlockX() != from.getBlockX() || to.getBlockZ() != from.getBlockZ()){
			if(loggingOut.contains(event.getPlayer())){
				loggingOut.remove(event.getPlayer());
				event.getPlayer().sendMessage(ChatColor.RED + "Your safe logout was cancelled due to block movement.");
			}
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "Only players");
			return true;
		}
		final Player player = (Player)sender;
		if(loggingOut.contains(player)){
			player.sendMessage(ChatColor.GREEN + "Already Logging Out!");
			return true;
		}
		loggingOut.add(player);
		player.sendMessage(ChatColor.GREEN + "You are logging out in 30 second!");
		new BukkitRunnable() {
			int timer = 30;
			@Override
			public void run() {
				if(!player.isOnline() || !loggingOut.contains(player)){
					cancel();
					return;
				}
				Damageable dplayer = (Damageable) player;
				if(dplayer.getHealth() < dplayer.getMaxHealth()){
					player.sendMessage(ChatColor.RED + "Safe Logout Cancelled. Health not max");
					loggingOut.remove(player);
					cancel();
					return;

				}
				if(timer <= 0){
					player.setMetadata("SafeLogout", new FixedMetadataValue(getPlugin(), true));
					player.kickPlayer(ChatColor.GREEN + "Safe Logout");
					cancel();
					return;
				}
				timer--;
				player.sendMessage(ChatColor.GREEN + "Logging out: " + timer + " seconds");
			}
		}.runTaskTimer(getPlugin(), 20, 20);
		return false;
	}
}
