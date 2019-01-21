package net.libhalt.bukkit.kaede;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.faction.bean.FactionPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import net.libhalt.bukkit.kaede.utils.Manager;

public class PlayerDataManager extends Manager implements Listener {
	private HashMap<Player , PlayerData> dataMap = new HashMap<Player , PlayerData> ();
	private File playerDataFolder;

	public PlayerDataManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.playerDataFolder = new File(this.getPlugin().getDataFolder(), "playerdata");
		this.playerDataFolder.mkdir();
		for(Player player : Bukkit.getOnlinePlayers()){
			onLogin(player);
		}

		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
	}

	@Override
	public void tear() {
		for(Player player : Bukkit.getOnlinePlayers()){
			onLogout(player);
		}

	}

	public void onLogin(Player player) {
		File file = new File(this.playerDataFolder, player.getUniqueId().toString() + ".dat");
		PlayerData data = new PlayerData();
		if (file.exists()) {
			YamlConfiguration dataConfig = YamlConfiguration.loadConfiguration(file);
			data.setKills(dataConfig.getInt("kills"));
			if(!dataConfig.contains("pvptimes")){
				data.setPvpTime(this.getPlugin().getPvPTimerManager().getDefaultPvPTimer());
			}else {
				data.setPvpTime(dataConfig.getInt("pvptimes"));
			}
			data.setKillOnLogin(false);
			data.setCombatTagMillisecond(dataConfig.getLong("combatTag" , -1L));
			data.setLives(dataConfig.getInt("lives"));
		} else {
			data.setPvpTime(this.getPlugin().getPvPTimerManager().getDefaultPvPTimer());
			savePlayerData(player.getUniqueId().toString(), data);
		}

		this.dataMap.put(player, data);
		if (data.isKillOnLogin()) {
			data.setKillOnLogin(false);
			player.getInventory().setContents(new ItemStack[player.getInventory().getContents().length]);
			player.getInventory().setArmorContents(new ItemStack[4]);
			FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
			if(factionPlayer != null && factionPlayer.getFaction() != null){
				factionPlayer.getFaction().setDtr(factionPlayer.getFaction().getDtr() + 1);
			}
			player.setHealth(0.0D);
		}

	}

	public void onLogout(Player player) {
		if (this.dataMap.containsKey(player)) {
			PlayerData data = this.dataMap.get(player);
			savePlayerData(player.getUniqueId().toString(), data);
			dataMap.remove(player);
		}

	}

	public PlayerData getPlayerData(Player player) {
		return this.dataMap.get(player);
	}

	public PlayerData getOfflinePlayerData(String uuidString) {
		UUID uuid = UUID.fromString(uuidString);
		File file = new File(this.playerDataFolder, uuid.toString() + ".dat");
		PlayerData data = new PlayerData();
		if (file.exists()) {
			YamlConfiguration dataConfig = YamlConfiguration.loadConfiguration(file);
			data.setKills(dataConfig.getInt("kills"));
			data.setPvpTime(dataConfig.getInt("pvptimes"));
			data.setKillOnLogin(false);
			data.setLives(dataConfig.getInt("lives"));
		} else {
			data.setPvpTime(this.getPlugin().getPvPTimerManager().getDefaultPvPTimer());
		}

		return data;
	}

	public void savePlayerData(String uuidString, PlayerData data) {
		UUID uuid = UUID.fromString(uuidString);
		File file = new File(this.playerDataFolder, uuid.toString() + ".dat");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration config = new YamlConfiguration();
		config.set("kills", Integer.valueOf(data.getKills()));
		config.set("pvptimes", Integer.valueOf(data.getPvpTime()));
		config.set("killOnLogin", Boolean.valueOf(data.isKillOnLogin()));
		config.set("combatTag", data.getCombatTagMillisecond());
		config.set("lives" , data.getLives());
		try {
			config.save(file);
		} catch (IOException var7) {
			var7.printStackTrace();
		}

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		this.onLogout(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		this.onLogin(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerKick(PlayerKickEvent event) {
		this.onLogout(event.getPlayer());
	}
}
