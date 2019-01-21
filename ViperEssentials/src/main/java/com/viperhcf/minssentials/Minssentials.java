package com.viperhcf.minssentials;

import com.viperhcf.minssentials.command.Command;
import com.viperhcf.minssentials.command.CommandRegistry;
import com.viperhcf.minssentials.commands.*;
import com.viperhcf.minssentials.config.MyConfig;
import com.viperhcf.minssentials.config.MyConfigManager;
import com.viperhcf.minssentials.events.CustomCommandEvent;
import com.viperhcf.minssentials.events.PlayerClickEvent;
import com.viperhcf.minssentials.events.PlayerConnectEvent;
import com.viperhcf.minssentials.utils.NicknameUtil;
import com.viperhcf.minssentials.utils.TagUtil;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import com.viperhcf.minssentials.listener.MinssentialsListener;
import com.viperhcf.minssentials.vault.PopuraSupport;
import com.viperhcf.minssentials.vault.VaultSupport;

import java.util.List;
import java.util.UUID;

public class Minssentials extends JavaPlugin {

	public static Minssentials INSTANCE;

	public CommandSlowChat getCommandSlowChat() {
		return commandSlowChat;
	}
	public CommandMuteChat getCommandMuteChat() {
		return commandMuteChat;
	}

	private CommandSlowChat commandSlowChat;
	private CommandMuteChat commandMuteChat;

	public static Chat chat = null;

	public MyConfigManager manager = new MyConfigManager(this);
	public MyConfig playerdata = manager.getNewConfig("playerdata.yml");
	public MyConfig tags = manager.getNewConfig("tags.yml");

	public Inventory NicknameGUI;

	@Override
	public void onEnable() {
		INSTANCE = this;

		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new MinssentialsListener(), this);
		getCommand("fly").setExecutor(new CommandFly());
		getCommand("gmc").setExecutor(new CommandGameModeCreative());
		getCommand("gms").setExecutor(new CommandGameModeSurvival());
		getCommand("spawner").setExecutor(new CommandSpawner());
		getCommand("tphere").setExecutor(new CommandTpHere());
		getCommand("tppos").setExecutor(new CommandTpPos());
		getCommand("tp").setExecutor(new CommandTp());
		getCommand("give").setExecutor(new CommandGive());
		getCommand("seen").setExecutor(new CommandSeen(this));
		getCommand("feed").setExecutor(new CommandFeed(this));
		getCommand("playtime").setExecutor(new CommandPlayTime(this));
		getCommand("nick").setExecutor(new CommandNick(this));
		getCommand("broadcast").setExecutor(new CommandBroadcast());
		getCommand("workbench").setExecutor(new CommandWorkbench());
		getCommand("near").setExecutor(new CommandNear(this));
		getCommand("spawn").setExecutor(new CommandSpawn(this));
		getCommand("setspawn").setExecutor(new CommandSetSpawn());
		getCommand("invsee").setExecutor(new CommandInvSee());
		getCommand("fix").setExecutor(new CommandFix());
		getCommand("god").setExecutor(new CommandGod(this));
		getCommand("mutechat").setExecutor((commandMuteChat = new CommandMuteChat(this)));
		getCommand("slowchat").setExecutor((commandSlowChat = new CommandSlowChat(this)));
		getCommand("clearentity").setExecutor(new CommandClearEntity());
		getCommand("clearchat").setExecutor(new CommandClearChat(this));
		getCommand("sudo").setExecutor(new CommandSudo());
		getCommand("rename").setExecutor(new CommandRename());
		getCommand("tgc").setExecutor(new CommandToggleChat(this));
		getCommand("createtag").setExecutor(new CommandCreateTag());
		getCommand("tags").setExecutor(new CommandTag());
		registerEvents();

		if(getServer().getPluginManager().getPlugin("ViperCore") != null){
			new PopuraSupport().init(this);
		}
		new VaultSupport(this).init(this);
		PlayerStorage.getInstance().prepare(this);

		setupCooldowns();
		setupChat();

		NicknameGUI = NicknameUtil.setupGUI();
	}

	public void setupCooldowns()
	{
		if(playerdata.getConfigurationSection("").getKeys(false) != null){
			for(String x : playerdata.getConfigurationSection("").getKeys(false))
			{
				try{
					if((long)playerdata.get(x + ".fix-hand-cooldown") > 0)
					{
						CommandFix.hand_cooldown.put(Bukkit.getOfflinePlayer(x).getUniqueId(), (long) playerdata.get(x + ".fix-hand-cooldown"));
					}
					if((long)playerdata.get(x + ".fix-all-cooldown") > 0)
					{
						CommandFix.all_cooldown.put(Bukkit.getOfflinePlayer(x).getUniqueId(), (long) playerdata.get(x + ".fix-all-cooldown"));
					}
				} catch (Exception error){}
			}
		}
	}

	public void onDisable(){
		for(UUID uuid : CommandFix.all_cooldown.keySet())
		{
			if(CommandFix.all_cooldown.get(uuid) > 0)
			{
				if(Bukkit.getPlayer(uuid) != null){
					playerdata.set(Bukkit.getPlayer(uuid).getName() + ".fix-all-cooldown", CommandFix.all_cooldown.get(uuid));
					playerdata.saveConfig();
				} else {
					playerdata.set(Bukkit.getOfflinePlayer(uuid).getName() + ".fix-all-cooldown", CommandFix.all_cooldown.get(uuid));
					playerdata.saveConfig();
				}
			}
		}


		for(UUID uuid : CommandFix.hand_cooldown.keySet())
		{
			if(CommandFix.hand_cooldown.get(uuid) > 0)
			{
				if(Bukkit.getPlayer(uuid) != null){
					playerdata.set(Bukkit.getPlayer(uuid).getName() + ".fix-hand-cooldown", CommandFix.hand_cooldown.get(uuid));
					playerdata.saveConfig();
				} else {
					playerdata.set(Bukkit.getOfflinePlayer(uuid).getName() + ".fix-hand-cooldown", CommandFix.hand_cooldown.get(uuid));
					playerdata.saveConfig();
				}
			}
		}
	}

	public void registerEvents()
	{
		getServer().getPluginManager().registerEvents(new PlayerClickEvent(this), this);
		getServer().getPluginManager().registerEvents(new PlayerConnectEvent(this), this);
		getServer().getPluginManager().registerEvents(new CustomCommandEvent(this), this);
	}

	public static Minssentials getInstance()
	{
		return INSTANCE;
	}

    @Deprecated
    public static Player getPlayer(String input){
		Player target = Bukkit.getPlayer(input);
		if(target != null){
			return target;
		}
		for(Player player : Bukkit.getOnlinePlayers()){
			if(player.getName().toLowerCase().startsWith(input.toLowerCase())){
				return player;
			}
		}
		for(Player player : Bukkit.getOnlinePlayers()){
			if(player.getName().toLowerCase().contains(input.toLowerCase())){
				return player;
			}
		}
		return null;
	}

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		chat = rsp.getProvider();
		return chat != null;
	}
}
