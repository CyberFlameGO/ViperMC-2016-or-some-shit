package net.libhalt.bukkit.kaede;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;

import cloth.bugs.FenceGateBug;
import cloth.pearls.PearlInteractEvent;
import cloth.portals.NetherPortalEvent;
import cloth.xp.XpBottleCommand;
import cloth.xp.XpBottleEvent;
import com.confinement.filter.Filter;
import com.google.common.collect.Lists;
import com.igodlik3.conquest.Conquest;
import com.igodlik3.enchantfix.EnchantFix;
import com.igodlik3.modmode.ModMode;
import com.igodlik3.stats.Stats;
import com.igodlik3.subclaims.Subclaims;
import com.igodlik3.viperfix.ViperFix;
import com.igodlik3.vipermisc.Misc;
import com.igodlik3.viperutils.ViperUtils;
import config.MyConfig;
import config.MyConfigManager;
import config.ConfigData;
import me.dreamzy.report.ViperReport;
import net.libhalt.bukkit.kaede.manager.*;
import net.libhalt.dev.plugin.armor.ArmorPlugin;
import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.combattag.CombatTagManager;
import net.syuu.popura.command.faction.HomeCommand;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_7_R4.SpigotTimings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.libhalt.bukkit.kaede.event.ScoreboardTextAboutToUpdateEvent;
import net.libhalt.bukkit.kaede.manager.crowbar.CrowbarManager;
import net.libhalt.bukkit.kaede.support.Support;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import net.libhalt.bukkit.kaede.utils.MilliToSecondFormatter;
import org.spigotmc.CustomTimingsHandler;

public class HCFactionPlugin extends JavaPlugin {

	private static HCFactionPlugin INSTANCE;
	public static HCFactionPlugin getInstance(){
		return INSTANCE;
	}
	private Map<Class<? extends Manager> , Manager> managersMap = new LinkedHashMap<Class <? extends Manager > , Manager>();
	private List<Class<? extends Manager>> disabled = new ArrayList<Class <? extends Manager>>();
	private ConfigurationWrapper lang;
	private Configuration defaults;
	public MyConfigManager manager = new MyConfigManager(this);
	public MyConfig messages = manager.getNewConfig("messages.yml");
	public ConfigData configdata = new ConfigData(this);


	//test

	private long since = System.currentTimeMillis();
	{
		INSTANCE = this;
		List<Manager> managers = new ArrayList<Manager>();
		managers.add(new CrowbarManager(this));
		//managers.add(new FasterTileEntityManager(this));
		managers.add(new ShopConfigManager(this));

		managers.add(new MiscDisableManager(this));
		managers.add(new PotionLimitManager(this));
		managers.add(new EnchantmentLimitManager(this));
		managers.add(new EnderPearlManager(this));
		managers.add(new ReclaimManager(this));
		//managers.add(new KitManager(this));
		managers.add(new GlassManager(this));
		managers.add(new TabManupilator(this));

		managers.add(new PvPTimerManager(this));
		managers.add(new DeathKillManager(this));
		managers.add(new CombatLoggerManager(this));
		managers.add(new PlayerDataManager(this));
		managers.add(new FreezeManager(this));
		managers.add(new ScoreboardManager(this));
		managers.add(new DeathBanManager(this));
		managers.add(new MobMergeListener(this));
		managers.add(new LootingManager(this));
		managers.add(new UnEnchantBookManager(this));
		managers.add(new EndWorldManager(this));
		managers.add(new FoundDiamondManager(this));
		managers.add(new WorldBorderManager(this));
		managers.add(new DeathSignManager(this));
		managers.add(new DeathMessageManager(this));
		managers.add(new CraftingDisableManager(this));
		managers.add(new EnderchestDisableManager(this));
		managers.add(new Conquest(this));
		managers.add(new Subclaims(this));
		managers.add(new Stats(this));
		managers.add(new ModMode(this));
		managers.add(new EnchantFix(this));
		managers.add(new ViperFix(this));
		managers.add(new ViperUtils(this));
		managers.add(new ViperReport(this));
		managers.add(new Misc(this));
		managers.add(new HideStreamManager(this));
		managers.add(new ArmorPlugin(this));
		managers.add(new ElevatorManager(this));
		managers.add(new GMelonManager(this));
		managers.add(new GappleManager(this));
		managers.add(new HungerFixManager(this));
		managers.add(new PotionFixManager(this));
		managers.add(new Filter(this));
		managers.add(new SwordStatTrack(this));

		for(Manager manager : managers){
			managersMap.put(manager.getClass(), manager);
		}
	}

	@Override
	public void onEnable() {

		configdata.setupDefaultMessages();

		registerEvents();
		registerCommands();

		lang =  new ConfigurationWrapper("en_US.yml", this);
		ConfigurationWrapper wrap = new ConfigurationWrapper("modules.yml", this);
		Configuration config = wrap.getConfig();
		for(Manager manager : managersMap.values()){
			if(config.getBoolean(manager.getClass().getSimpleName().replace("Manager", "") , true)){
				try{
					manager.init();
				}catch(OutOfMemoryError rethrow){
					throw rethrow;
				}catch(Throwable t){
					t.printStackTrace();
					getLogger().severe("Could not enable " + manager.getClass().getSimpleName() + "It is recommended to resolve the problem to prevent unexpected behavior.");
				}
			}else{
				disabled.add(manager.getClass());
			}
		}
		if(!isDisabled(ScoreboardManager.class)){
			this.initScoreboard();
		}
		try(InputStream stream = getResource("en_US.yml"); InputStreamReader reader = new InputStreamReader(stream , "UTF-8")){
			defaults = YamlConfiguration.loadConfiguration(reader);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Support.getInstance().register(this);
		getCommand("lag").setExecutor(new CommandExecutor() {
			private DecimalFormat formatter = new DecimalFormat("0.0");
			@Override
			public boolean onCommand(CommandSender sendeer, Command command, String s, String[] strings) {
				double[] ts = Bukkit.getServer().spigot().getTPS();
				sendeer.sendMessage(ChatColor.GOLD + "TPS from last 1m, 5m, 15m: " + ChatColor.GREEN + formatter.format(ts[0] / 1.0D) + ", " + formatter.format(ts[1]/1.0D) + ", " + formatter.format(ts[2]/ 1.0D));
				try {
					CustomTimingsHandler server = SpigotTimings.serverTickTimer;
					Field totalTimefield = CustomTimingsHandler.class.getDeclaredField("totalTime");
					Field countfield = CustomTimingsHandler.class.getDeclaredField("count");
					totalTimefield.setAccessible(true);
					countfield.setAccessible(true);
					long time = totalTimefield.getLong(server);
					long count = countfield.getLong(server);
					long avg = time / count;
					double value = avg / 1000.0D / 1000.0D;
					sendeer.sendMessage(ChatColor.GOLD + "Server has been up for " + DurationFormatUtils.formatDurationWords(System.currentTimeMillis() - since , true , true));

					sendeer.sendMessage(ChatColor.GOLD + "Full tick: " + ChatColor.YELLOW + formatter.format(value )+ "ms");

				} catch (ReflectiveOperationException e) {
					e.printStackTrace();
				}

				return true;
			}
		});
	}

	public void registerEvents()
	{
		Bukkit.getPluginManager().registerEvents(new PearlInteractEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new FenceGateBug(this), this);
		Bukkit.getPluginManager().registerEvents(new NetherPortalEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new XpBottleEvent(this), this);
	}

	public void registerCommands()
	{
		getCommand("bottle").setExecutor(new XpBottleCommand(this));
	}

	@Override
	public void onDisable() {
		for(Manager manager : managersMap.values()){
			if(!disabled.contains(manager.getClass())){
				manager.tear();
			}
		}
	}

	public PvPTimerManager getPvPTimerManager() {
		return getManager(PvPTimerManager.class);
	}

	public PlayerDataManager getPlayerDataManager() {
		return getManager(PlayerDataManager.class);
	}

	public boolean isDisabled(Class<? extends Manager> clazz){
		return disabled.contains(clazz);
	}
	@SuppressWarnings("unchecked")
	public <T extends Manager> T getManager(Class<T> clazz){
		return (T) managersMap.get(clazz);
	}
	public void initScoreboard() {
		final ScoreboardManager scoreboardManager = getManager(ScoreboardManager.class);
		scoreboardManager.init();
		getServer().getPluginManager().registerEvents(new Listener() {
			@EventHandler(priority = EventPriority.MONITOR)
			public void onScoreboardPreUpdate(net.syuu.common.event.ScoreboardPreRenderEvent e){
				for (String string : scoreboardManager.getConfiguredContext()) {
					ScoreboardTextAboutToUpdateEvent event = new ScoreboardTextAboutToUpdateEvent(e.getPlayer(), string, scoreboardManager.isAlwaysDisplay());
					Bukkit.getPluginManager().callEvent(event);
					if (event.getText() != null) {
						String result = HCFactionPlugin.this.processTag(e.getPlayer(), event.getText(), scoreboardManager);
						if (result != null) {
							result = result.replace(Pattern.quote("|"), "");
							e.addText(result);
						}
					}
				}
				List<String> text = Lists.newArrayList();
				String lastAdded = null;
				for(String input : e.getTexts()){
					if(input == null){
						continue;
					}
					if(lastAdded == null){
						text.add(input);
						lastAdded = input;
					}else{
						if(!input.equalsIgnoreCase(lastAdded)){
							lastAdded = input;
							text.add(input);
						}
					}
				}
				e.getTexts().clear();
				e.getTexts().addAll(text);
				if(e.getTexts().size() <= 2){
					e.getTexts().clear();
				}
			}
		} , this);
	}

	public String processTag(Player player, String input , ScoreboardManager manager) {
		String result = input;
		String str;
		boolean shouldAlwaysDisplay = manager.isAlwaysDisplay();
		if (input.contains("%fhome_timer%")) {
			long left = HomeCommand.INSTANCE.getFHomingUntil(player);
			if (left <= 0) {
				if(shouldAlwaysDisplay){
					if(manager.useDecimalForEnderPearls()){
						result = input.replace("%fhome_timer%", "0.000");
					}else{
						result = input.replace("%fhome_timer%", "00:00");
					}
				}else{
					return null;
				}
			}else{
				/*
				if(manager.useDecimalForEnderPearls()){
					result = input.replace("%fhome_timer%", MilliToSecondFormatter.format(left - System.currentTimeMillis()));
				}else{
					int data = (int) (left/ 1000.0D);
					int minutes = (int) (data / 60.0D);
					int seconds = data % 60;
					str = String.format("%d:%02d", Integer.valueOf(minutes), Integer.valueOf(seconds) );
					result = input.replace("%fhome_timer%", str);
				}*/
				long value = left - System.currentTimeMillis();
				if(value < 0){
					return null;
				}else {
					result = input.replace("%fhome_timer%", MilliToSecondFormatter.format( value));
				}
			}
		}
		if (input.contains("%enderpearl_time%")) {
			EnderPearlManager enderPearlManager = getManager(EnderPearlManager.class);
			if (!enderPearlManager.isCooldownActive(player)) {
				if(shouldAlwaysDisplay){
					if(manager.useDecimalForEnderPearls()){
						result = input.replace("%enderpearl_time%", "0.000");
					}else{
						result = input.replace("%enderpearl_time%", "00:00");
					}
				}else{
					return null;
				}
			}else{
				if(manager.useDecimalForEnderPearls()){
					result = input.replace("%enderpearl_time%", MilliToSecondFormatter.format(enderPearlManager.getMillisecondLeft(player)));
				}else{
					int data = (int) (enderPearlManager.getMillisecondLeft(player) / 1000.0D);
					int minutes = (int) (data / 60.0D);
					int seconds = data % 60;
					str = String.format("%d:%02d", Integer.valueOf(minutes), Integer.valueOf(seconds) );
					result = input.replace("%enderpearl_time%", str);
				}
			}
		}

		if (result.contains("%spawntag_time%")) {
			CombatTagManager combatTag = PopuraPlugin.getInstance().getPopura().getCombatTagManager();
			if (!combatTag.isCombatTagActive(player)) {
				if(shouldAlwaysDisplay){
					if(manager.useDecimalForSpawnTag()){
						result = input.replace("%spawntag_time%", "0.000");
					}else{
						result = input.replace("%spawntag_time%", "00:00");
					}
				}else{
					return null;
				}
			}
			if(manager.useDecimalForSpawnTag()){
				result = input.replace("%spawntag_time%", MilliToSecondFormatter.format(combatTag.getMillisecondLeft(player)));
			}else{
				int data = (int) (combatTag.getMillisecondLeft(player) / 1000.0D);
				int minutes = (int) (data / 60.0D);
				int seconds = data % 60;
				str = String.format("%d:%02d", Integer.valueOf(minutes), Integer.valueOf(seconds) );
				result = input.replace("%spawntag_time%", str);
			}
		}
		if (result.contains("%pvp_timer%")) {
			PlayerData data1 = getPlayerDataManager().getPlayerData(player);
			if (data1 == null || data1.getPvpTime() <= 0) {
				if(shouldAlwaysDisplay){
					result = input.replace("%pvp_timer%", "0");
				}else{
					return null;
				}
			}
			if(manager.useMinuteSeperatorForPvPTimer()){
				int data = data1.getPvpTime();
				int minutes = (int) (data / 60.0D);
				int seconds = data % 60;
				String format = "%d:%02d";
				if(manager.appendZeroForMinuteAsPvPTimer()){
					format = "%02d:%02d";
				}
				str = String.format(format, Integer.valueOf(minutes), Integer.valueOf(seconds) );
				result = input.replace("%pvp_timer%", str);
			}else{
				result = result.replace("%pvp_timer%", String.valueOf(data1.getPvpTime()));
			}
		}
		if (result.contains("%kill%")) {
			result = result.replace("%kill%", String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS)));
		}
		if (result.contains("%death%")) {
			result = result.replace("%death%", String.valueOf(player.getStatistic(Statistic.DEATHS)));
		}

		return result;
	}

	public static String color(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}
	public String getLocalized (Player player , String key , Object... params){
		String message = key;
		if(lang.getConfig().contains(key)){
			message = lang.getConfig().getString(key);
		}else{
			if(defaults.contains(key)){
				lang.getConfig().set(key, defaults.get(key));
				try {
					lang.getConfig().save(lang.getFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
				message = defaults.getString(key);
			}else{
				getLogger().severe("Can not find language referense for " + key);
			}
		}
		return color(String.format(message, params));
	}
	
	public void sendLocalized(Player player , String key , Object... params){
		player.sendMessage(getLocalized(player, key, params));
	}
}
