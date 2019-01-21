package net.libhalt.bukkit.kaede.manager;

import java.util.*;

import com.google.common.collect.Lists;
import net.libhalt.dev.plugin.armor.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.PlayerData;
import net.libhalt.bukkit.kaede.support.IRegion;
import net.libhalt.bukkit.kaede.support.RegionSupport;
import net.libhalt.bukkit.kaede.support.Support;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;

public class PvPTimerManager extends Manager implements Listener, CommandExecutor {
	private ConfigurationWrapper config;
	private int defaultPvPTime;
	private List<Material> blackListMaterial = new ArrayList<Material>();
	private List<String> disabledCommand = new ArrayList<String>();
	private Set<Player> notified = Collections.newSetFromMap(new WeakHashMap<Player , Boolean>());
	private List<String> messagePvp = Lists.newArrayList();
	public PvPTimerManager(HCFactionPlugin plugin) {
		super(plugin);
	}
	@Override
	public void init() {
		if(getPlugin().getServer().getPluginManager().getPermission("pvptimer.give") == null){
			getPlugin().getServer().getPluginManager().addPermission(new Permission("pvptimer.give" , PermissionDefault.OP));
		}
		(new BukkitRunnable() {
			@Override
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()){
					if(player.isDead()){
						continue;
					}
					PlayerData data = PvPTimerManager.this.getPlugin().getPlayerDataManager().getPlayerData(player);
					if(data == null){
						continue;
					}
					boolean freeze = false;
					breakpoint: for(RegionSupport regionSupport : Support.getInstance().getRegionSupporters()){
						for(IRegion region : regionSupport.getRegionsAt(player.getLocation())){
							if(region.shouldPvPTimerFreeze()){
								freeze = true;
								break breakpoint;
							}
						}
					}
					if (data.getPvpTime() > 0) {
						if(!freeze){
							if(notified.contains(player)){
								notified.remove(player);
								getPlugin().sendLocalized(player, "PVP_TIMER_UNFROZEN");
							}
							data.setPvpTime(data.getPvpTime() - 1);
						}else{
							if(!notified.contains(player)){
								notified.add(player);
								getPlugin().sendLocalized(player, "PVP_TIMER_FROZEN");
							}
						}
					}
				}

			}
		}).runTaskTimer(this.getPlugin(), 20L, 20L);
		this.config = new ConfigurationWrapper("pvp-timer.yml", this.getPlugin());
		this.getPlugin().getCommand("pvp").setExecutor(this);
		this.reload();
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
	}

	public int getDefaultPvPTimer() {
		return this.defaultPvPTime;
	}

    public List<String> getDisabledCommands(){
        return disabledCommand;
    }

	@Override
	public void reload() {
		this.defaultPvPTime = this.config.getConfig().getInt("pvp-timer");
		blackListMaterial.clear();

        disabledCommand = this.config.getConfig().getStringList("disable-commands");

        for(String material : this.config.getConfig().getStringList("unpickupable")){
			Material type;
			try{
				type = Material.valueOf(material);
			}catch(IllegalArgumentException e){
				getPlugin().getLogger().severe("Can not parse " + material + " as a Item in PvPTimer's UnPickupables");
				return;
			}
			blackListMaterial.add(type);
		}
		messagePvp = Color.translate(config.getConfig().getStringList("message-pvp"));

	}

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event){
        PlayerData data = this.getPlugin().getPlayerDataManager().getPlayerData(event.getPlayer());
        if (data != null && data.getPvpTime() > 0) {
            String command = event.getMessage().toLowerCase();
            String first = command.split(" ")[0];
            int index = first.indexOf(":");
            if(index != -1){
                command = "/" + command.substring(index + 1, command.length());
            }
            Collection<String> restricted = getDisabledCommands();
            if(restricted != null){
                for(String string : restricted){
                    if(command.startsWith(string)){
                        this.getPlugin().sendLocalized(event.getPlayer(), "COMMAND_CANCALLED_PVPTIMER");
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent event){
		if(event.getCause() == TeleportCause.END_PORTAL){
			PlayerData data = this.getPlugin().getPlayerDataManager().getPlayerData(event.getPlayer());
			if (data != null && data.getPvpTime() > 0) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		PlayerData data = this.getPlugin().getPlayerDataManager().getPlayerData(event.getPlayer());
		Location location = event.getPlayer().getLocation();
		boolean freeze = false;
		breakpoint: for(RegionSupport regionSupport : Support.getInstance().getRegionSupporters()){
			for(IRegion region : regionSupport.getRegionsAt(location)){
				if(region.shouldPvPTimerFreeze()){
					freeze = true;
					break breakpoint;
				}
			}
		}
		if (data.getPvpTime() > 0 && freeze)  {
			notified.add(event.getPlayer());
		}
	}
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		PlayerData data = this.getPlugin().getPlayerDataManager().getPlayerData(event.getPlayer());
		if(data == null){
			return;//TODO RESPANED STARIGHT AFTER LOGIN
		}
		data.setPvpTime(this.defaultPvPTime);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		PlayerData data = this.getPlugin().getPlayerDataManager().getPlayerData(event.getEntity());
		data.setPvpTime(this.defaultPvPTime);
	}

	@EventHandler
	public void onPickUp(PlayerPickupItemEvent event) {
		PlayerData data = this.getPlugin().getPlayerDataManager().getPlayerData(event.getPlayer());
		if (data != null && data.getPvpTime() > 0) {
			if(blackListMaterial.contains(event.getItem().getItemStack().getType())){
				event.setCancelled(true);
			}
		}

	}
	@EventHandler
	public void onFoodLevelChange(EntityDamageEvent event){
		Entity entity = event.getEntity();
		if(entity instanceof Player){
			Player player = (Player) entity;
			PlayerData data = this.getPlugin().getPlayerDataManager().getPlayerData(player);
			if (data.getPvpTime() > 0) {
				if(event.getCause() == EntityDamageEvent.DamageCause.WITHER){
					event.setCancelled(true);
				}
				if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
					event.setCancelled(true);
				}
				if(event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION){
					event.setCancelled(true);
				}
				if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event){
		HumanEntity entity = event.getEntity();
		if(entity instanceof Player){
			Player player = (Player) entity;
			PlayerData data = this.getPlugin().getPlayerDataManager().getPlayerData(player);
			if (data != null && data.getPvpTime() > 0) {
				event.setFoodLevel(20);
				player.setSaturation(20);
			}
		}
	}
	@EventHandler
	public void onSplash(PotionSplashEvent event) {
		ProjectileSource shooter = event.getEntity().getShooter();
		if (shooter instanceof Player) {
			PlayerData data = this.getPlugin().getPlayerDataManager().getPlayerData((Player) shooter);
			if(data.getPvpTime() > 0){
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onEntityDamageByEntityLogger(EntityDamageByEntityEvent event) {
		Player damager = getDamager(event);
		if(damager == null){
			return;
		}
		PlayerData dataDamager = this.getPlugin().getPlayerDataManager().getPlayerData(damager);
		if (dataDamager.getPvpTime() > 0) {
			if (event.getEntity().getType() == EntityType.VILLAGER) {
				event.setCancelled(true);
				damager.sendMessage(ChatColor.RED + "You are not permitted do this while your PvP Timer is enabled!");
			}
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Player damagee = getEntity(event);
		Player damager = getDamager(event);
		if(damagee == null || damager == null){
			return;
		}
		PlayerData dataDamager = this.getPlugin().getPlayerDataManager().getPlayerData(damager);
		PlayerData dataDamagee = this.getPlugin().getPlayerDataManager().getPlayerData(damagee);
		if (dataDamager.getPvpTime() > 0) {
			event.setCancelled(true);
			damager.sendMessage(ChatColor.RED + "You are not permitted do this while your PvP Timer is enabled!");
		}

		if (dataDamagee.getPvpTime() > 0) {
			event.setCancelled(true);
			damager.sendMessage(ChatColor.RED + "That player has PvP Timer active!");
		}
	}

	private Player getEntity(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player){
			return (Player) event.getEntity();
		}
		if (event.getEntity() instanceof Projectile) {
			Projectile projectile1 = (Projectile) event.getEntity();
			if (projectile1.getShooter() instanceof Player && projectile1.getShooter() != event.getEntity()) {
				Player player = (Player) projectile1.getShooter();
				return player;
			}
		}
		return null;

	}
	private Player getDamager(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player){
			return (Player) event.getDamager();
		}
		if (event.getDamager() instanceof Projectile) {
			Projectile projectile1 = (Projectile) event.getDamager();
			if (projectile1.getShooter() instanceof Player && projectile1.getShooter() != event.getEntity()) {
				Player player = (Player) projectile1.getShooter();
				return player;
			}
		}
		return null;

	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				for(String input : messagePvp){
					sender.sendMessage(input);
				}
			} else {
				if (args[0].equalsIgnoreCase("enable")) {
					PlayerData data = this.getPlugin().getPlayerDataManager().getPlayerData(player);
					data.setPvpTime(0);
					this.getPlugin().sendLocalized(player, "COMMAND_ENABLED_PVPTIMER");
				} else if (args[0].equalsIgnoreCase("time")) {
					PlayerData data = this.getPlugin().getPlayerDataManager().getPlayerData(player);
					this.getPlugin().sendLocalized(player, "COMMAND_LEFT_PVPTIMER" , data.getPvpTime());
				} else if(args[0].equalsIgnoreCase("verz")){
					player.sendMessage(ChatColor.GREEN + "1.0.0.SNAPSHOT");
				}else if(args[0].equalsIgnoreCase("give") && player.hasPermission("pvptimer.give")){
					if(args.length <= 1){
						player.sendMessage("/pvp give <name>");
						return true;
					}
					Player target = Bukkit.getPlayer(args[1]);
					if(target == null){
						player.sendMessage("That Player is not online");
						return true;
					}
					PlayerData data = this.getPlugin().getPlayerDataManager().getPlayerData(target);
					data.setPvpTime(this.defaultPvPTime);
				}else {
					for(String input : messagePvp){
						sender.sendMessage(input);
					}
				}
			}
		} else {
			sender.sendMessage("Only Player\'s can run this command");
		}

		return false;
	}
}
