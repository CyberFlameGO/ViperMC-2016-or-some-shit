package net.libhalt.bukkit.kaede.manager;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import net.libhalt.dev.plugin.armor.utils.Color;
import net.syuu.popura.PopuraPlugin;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.scheduler.BukkitRunnable;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.PlayerData;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;

public class DeathBanManager extends Manager implements Listener , CommandExecutor {
	private ConfigurationWrapper config;
	private long deathBan;
	private File plDataFolder;
	private Map<String , Integer> deathBanByPerm = new HashMap<String , Integer>();
	private int limit;
	private List<String> messageLives = Lists.newArrayList();
	private List<String> messageHelpLive = Lists.newArrayList();
	public DeathBanManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.config = new ConfigurationWrapper("death-bans.yml", this.getPlugin());
		this.plDataFolder = new File(this.getPlugin().getDataFolder(), "deathbans");
		this.plDataFolder.mkdirs();
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
		this.reload();
		getPlugin().getCommand("lives").setExecutor(this);
	}

	@Override
	public void reload() {
		this.deathBanByPerm.clear();
		limit = this.config.getConfig().getInt("live-limit" , 100);
		if(this.config.getConfig().contains("death-ban-by-perm")){
			ConfigurationSection section = this.config.getConfig().getConfigurationSection("death-ban-by-perm");
			for(String string : section.getKeys(false)){
				this.deathBanByPerm.put(this.config.getConfig().getString("death-ban-by-perm." + string) , Integer.valueOf(string));
				if(getPlugin().getServer().getPluginManager().getPermission(string) == null){
					Permission perm = new Permission(string, PermissionDefault.FALSE);
					getPlugin().getServer().getPluginManager().addPermission(perm);
				}
			}
		}
		this.deathBan = this.config.getConfig().getLong("death-ban") * 1000L;


		messageLives = Color.translate(config.getConfig().getStringList("message-live"));
		messageHelpLive = Color.translate(config.getConfig().getStringList("message-help-live"));
	}

	@EventHandler
	public void onAsyncPlayerLogin(AsyncPlayerPreLoginEvent event) {
		final UUID uuid = event.getUniqueId();
		File file = new File(this.plDataFolder, uuid.toString() + ".yml");
		PlayerData offlineData = getPlugin().getPlayerDataManager().getOfflinePlayerData(uuid.toString());
		if (file.exists()) {
			YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
			long until = data.getLong("ban_until");
			if (System.currentTimeMillis() < until) {
				long left = until - System.currentTimeMillis();
				if(!PopuraPlugin.getInstance().getPopura().isEotw() && offlineData.getLives() > 0){

						offlineData.setLives(offlineData.getLives() - 1);
						data.set("ban_until", 0L);
						file.delete();
						getPlugin().getPlayerDataManager().savePlayerData(uuid.toString(), offlineData);
						new BukkitRunnable() {

							@Override
							public void run() {
								Player player = Bukkit.getPlayer(uuid);
								if(player != null){
									PlayerData data = getPlugin().getPlayerDataManager().getPlayerData(player);
									getPlugin().sendLocalized(player, "NOTIFICATION_LIVE_CONSUMED", String.valueOf(data.getLives()));
								}
							}
						}.runTaskLater(getPlugin(), 100);

					return;
				}
				event.setKickMessage(getPlugin().getLocalized(null, "DEATH_BANNED", DurationFormatUtils.formatDurationWords(left, true, true)));
				event.setLoginResult(Result.KICK_BANNED);
			} else {
				file.delete();
			}
		}

	}

	public long getDeathBan(Player player){
		if(PopuraPlugin.getInstance().getPopura().isEotw()){
			return 1000 * 60 * 24 * 356;
		}
		long ban = deathBan;
		for(Entry<String , Integer> entry : deathBanByPerm.entrySet()){
			if(entry.getValue() * 1000L < ban && player.hasPermission(entry.getKey())){
				ban = entry.getValue() * 1000L;
			}
		}
		return ban;
	}
	@EventHandler
	public void onPlayerDeath(final PlayerDeathEvent event) {
		(new BukkitRunnable() {
			@Override
			public void run() {
				DeathBanManager.this.ban(event.getEntity());
			}
		}).runTaskLater(this.getPlugin(), 10L);
	}

	public void ban(Player player) {
		File file = new File(this.plDataFolder, player.getUniqueId().toString() + ".yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException var6) {
				var6.printStackTrace();
			}
		}

		YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
		long duration = getDeathBan(player);
		data.set("ban_until", Long.valueOf(System.currentTimeMillis() + duration));

		try {
			data.save(file);
		} catch (IOException var5) {
			var5.printStackTrace();
		}

		player.kickPlayer(ChatColor.RED + "You are deathbanned for " + DurationFormatUtils.formatDurationWords(duration, true, true));
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command arg1, String arg2, String[] args) {
		if(args.length == 0){
			if(sender instanceof Player){
				Player player = ((Player)sender);
				PlayerData data = getPlugin().getPlayerDataManager().getPlayerData(player);
				for(String message : messageLives){
					player.sendMessage(message.replace("%live%" , String.valueOf(data.getLives())));
				}
			}else {
				sender.sendMessage(ChatColor.GREEN + "Note: use /lives help for list of command");
			}
		}else if(args[0].equalsIgnoreCase("help")){
			for(String message : messageHelpLive){
				sender.sendMessage(message);
			}
		}else if(args[0].equalsIgnoreCase("check")){
			if(args.length < 2){
				sender.sendMessage(ChatColor.RED + "/lives check <name> - check the live of player. works only for online player");
				return true;
			}
			String name = args[1];
			Player target = Bukkit.getPlayer(name);
			if(target == null){
				sender.sendMessage(ChatColor.RED + "That player is not online");
				return true;
			}
			int lives = getPlugin().getPlayerDataManager().getPlayerData(target).getLives();
			sender.sendMessage(ChatColor.GREEN + target.getName() + " has " + lives + " lives left.");
		}else if(args[0].equalsIgnoreCase("set")){
			if(!sender.hasPermission("live.admin")){
				sender.sendMessage(ChatColor.RED + "No Permission");
				return true;

			}
			if(args.length < 3){
				sender.sendMessage(ChatColor.RED + "/lives set <name> <amount> - set the live of target by name. works only for online players");
				return true;
			}
			final String name = args[1];
			final String input = args[2];
			final Player target = Bukkit.getPlayer(name);
			final int amount;
			try{
				amount = Integer.valueOf(input);
			}catch(IllegalArgumentException e){
				sender.sendMessage(ChatColor.RED + input + " does not look like a integer to me.");
				return true;
			}

			if (amount > limit) {
				sender.sendMessage(ChatColor.RED + "Too many lives. Limit is " + limit);
				return true;
			}
			if(target == null){
					final int famount = amount;
					new BukkitRunnable() {
						@Override
						public void run() {
							final UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
							new BukkitRunnable() {
								@Override
								public void run() {
									PlayerData data = getPlugin().getPlayerDataManager().getOfflinePlayerData(uuid.toString());
									data.setLives(famount);
									sender.sendMessage(ChatColor.GREEN + name + "'s live is now " + data.getLives());
									getPlugin().getPlayerDataManager().savePlayerData(uuid.toString(), data);
								}
							}.runTask(getPlugin());
						}
					}.runTaskAsynchronously(getPlugin());
			}else {
				PlayerData data = getPlugin().getPlayerDataManager().getPlayerData(target);
				data.setLives(amount);
				sender.sendMessage(ChatColor.GREEN + target.getName() + "'s live is now " + data.getLives());
			}
		}else if(args[0].equalsIgnoreCase("add")){
			if(/*!sender.hasPermission("live.admin") || */sender instanceof Player){
				sender.sendMessage(ChatColor.RED + "No Permission");
				return true;
			}
			if(args.length < 3){
				sender.sendMessage(ChatColor.RED + "/lives add <name> <amount> - adds live by name. only online players");
				return true;
			}
			final String name = args[1];
			String input = args[2];
			Player target = Bukkit.getPlayer(name);
			int amount = 0;
			try{
				amount = Integer.valueOf(input);
			}catch(IllegalArgumentException e){
				sender.sendMessage(ChatColor.RED + input + " does not look like a integer to me.");
				return true;
			}
			if(target != null){
				PlayerData data = getPlugin().getPlayerDataManager().getPlayerData(target);

				if(data.getLives() + amount > limit){
					sender.sendMessage(ChatColor.RED + "You have too many lives. Limit is " + limit);
					return true;
				}
				data.setLives(data.getLives() + amount);
				sender.sendMessage(ChatColor.GREEN + target.getName() + "'s live is now " + data.getLives());
			}else{
				final int famount = amount;
				new BukkitRunnable() {

					@Override
					public void run() {
						final UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
						new BukkitRunnable() {

							@Override
							public void run() {
								PlayerData data = getPlugin().getPlayerDataManager().getOfflinePlayerData(uuid.toString());

								if(data.getLives() + famount > limit){
									sender.sendMessage(ChatColor.RED + "You have too many lives. Limit is " + limit);
									return;
								}
								data.setLives(data.getLives() + famount);
								sender.sendMessage(ChatColor.GREEN + name + "'s live is now " + data.getLives());
								getPlugin().getPlayerDataManager().savePlayerData(uuid.toString(), data);
							}
						}.runTask(getPlugin());
					}
				}.runTaskAsynchronously(getPlugin());
			}
		}else if(args[0].equalsIgnoreCase("send")){
			if(!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "Must be player to send lives");
				return true;
			}
			if(!sender.hasPermission("live.send")){
				sender.sendMessage(ChatColor.RED + "No Permission");
				return true;
			}
			if(args.length < 3){
				sender.sendMessage(ChatColor.RED + "/lives send <name> <amount> ");
				return true;
			}
			final String name = args[1];
			String input = args[2];
			Player target = Bukkit.getPlayer(name);
			int amount = 0;
			try{
				amount = Integer.valueOf(input);
			}catch(IllegalArgumentException e){
				sender.sendMessage(ChatColor.RED + input + " does not look like a integer to me.");
				return true;
			}

			final PlayerData self = getPlugin().getPlayerDataManager().getPlayerData((Player) sender);
			if(self.getLives() < amount || amount <= 0){
				sender.sendMessage(ChatColor.RED + "Not enough lives");
				return true;
			}
			if(target != null){
				PlayerData data = getPlugin().getPlayerDataManager().getPlayerData(target);
				if(data.getLives() + amount > limit){
					sender.sendMessage(ChatColor.RED + "You have too many lives. Limit is " + limit);
					return true;
				}
				self.setLives(self.getLives() - amount);
				data.setLives(data.getLives() + amount);
				sender.sendMessage(ChatColor.GREEN + "Successfuly send lives to " + target.getName() + ". " + target.getName() + " now has " + data.getLives() + " lives left.");
			}else{
				final int famount = amount;
				new BukkitRunnable() {

					@Override
					public void run() {
						final UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
						new BukkitRunnable() {

							@Override
							public void run() {
								PlayerData data = getPlugin().getPlayerDataManager().getOfflinePlayerData(uuid.toString());
								if(data.getLives() + famount > limit){
									sender.sendMessage(ChatColor.RED + "You have too many lives. Limit is " + limit);
									return;
								}
								self.setLives(self.getLives() - famount);
								data.setLives(data.getLives() + famount);
								sender.sendMessage(ChatColor.GREEN + name + "'s live is now " + data.getLives());
								getPlugin().getPlayerDataManager().savePlayerData(uuid.toString(), data);
							}
						}.runTask(getPlugin());
					}
				}.runTaskAsynchronously(getPlugin());
			}
		}else if(args[0].equalsIgnoreCase("revive")){
			if(!sender.hasPermission("live.admin") && !sender.hasPermission("live.revive")){
				sender.sendMessage(ChatColor.RED + "No Permission");
				return true;
			}
			if(args.length < 2){
				sender.sendMessage(ChatColor.RED + "/lives revive <name>");
				return true;
			}
			final String name = args[1];
			Player target = Bukkit.getPlayer(name);
			

			if(target != null){
				File file = new File(this.plDataFolder, target.getUniqueId().toString() + ".yml");
				file.delete();
				sender.sendMessage(ChatColor.GREEN + "Revived that person successfully");
			}else{
				new BukkitRunnable() {

					@Override
					public void run() {
						final UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
						new BukkitRunnable() {
							@Override
							public void run() {
								File file = new File(plDataFolder, uuid.toString() + ".yml");
								file.delete();
								sender.sendMessage(ChatColor.GREEN + "Revived that person successfully");
							}
						}.runTask(getPlugin());
					}
				}.runTaskAsynchronously(getPlugin());
			}
		}
		return true;
	}




}
