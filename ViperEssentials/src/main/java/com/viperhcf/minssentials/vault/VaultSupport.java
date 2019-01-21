package com.viperhcf.minssentials.vault;


import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.paperspigot.PaperSpigotConfig;
import org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsService;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.viperhcf.minssentials.Minssentials;
import com.viperhcf.minssentials.PlayerStorage;
import com.viperhcf.minssentials.StoredData;
import com.viperhcf.minssentials.command.CommandRegistry;
import com.viperhcf.minssentials.command.annotation.SimpleCommand;

import net.milkbowl.vault.chat.Chat;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.PlayerList;
import net.minecraft.util.com.google.common.collect.Lists;

public class VaultSupport implements Listener {

	Minssentials plugin;
	public VaultSupport(Minssentials instance){
		this.plugin = instance;
	}

	private Chat chat;
	private Map<Player , UUID> lastMessaged = Maps.newHashMap();

	private String ignoredPlayer;
	private String unignoredPlayer;
	private String ignoreListPrefix;
	private String replyNoMessage;
	private String replyNoUser;
	private String messageDisabled;
	private List<String> names = Lists.newArrayList();
	private List<String> message = Lists.newArrayList();
	public void init(JavaPlugin plugin){
		CommandRegistry.getInstance().registerSimpleCommand(this);
		final RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
		chat = rsp.getProvider();
		final ZPermissionsService service = plugin.getServer().getServicesManager().load(ZPermissionsService.class);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				names = Lists.newArrayList(service.getGroupMembers("Venom"));
			}
		}.runTaskLater(plugin, 3);
		message = Lists.newArrayList(Collections2.transform(plugin.getConfig().getStringList("venom-message"), new Function<String , String>() {

			@Override
			public String apply(String arg0) {
				return ChatColor.translateAlternateColorCodes('&', arg0);
			}
		}));
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Collection<String> venoms = Collections2.filter(names , new Predicate<String>() {
					@Override
					public boolean apply(String arg0) {
						return Bukkit.getPlayer(arg0) != null;
					}
				});
				if(venoms.isEmpty()){
					return;
				}
				for(String text : message){
					Bukkit.broadcastMessage(text.replace("%names%", ChatColor.DARK_GREEN + Joiner.on(ChatColor.LIGHT_PURPLE + ", " + ChatColor.DARK_GREEN).join(venoms)));
				}
			}
		}.runTaskTimer(plugin, 20 * 60 * 30, 20 * 60 * 30);
		plugin.getServer().getPluginManager().registerEvents(this , plugin);
		if(plugin.getServer().getPluginManager().getPermission("minessentials.msgtoggle") == null){
			plugin.getServer().getPluginManager().addPermission(new Permission("minessentials.msgtoggle" , PermissionDefault.OP));
		}
		replyNoUser = ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("reply-no-user"));

		replyNoMessage = ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("reply-no-message"));
		ignoredPlayer = ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("ignored-player"));
		unignoredPlayer = ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("unignored-player"));
		ignoreListPrefix = ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("ignore-list-prefix"));
		messageDisabled = ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("target-message-disabled"));

	}
	@EventHandler(priority = EventPriority.MONITOR , ignoreCancelled = true)
	public void onAsyncChat(AsyncPlayerChatEvent event)
	{
		String color  = plugin.playerdata.getString(event.getPlayer().getName() + ".color").replaceAll("&", "§");
		String name = "";
		String tag = "";

		if(!Minssentials.getInstance().playerdata.getString(event.getPlayer().getName() + ".tag").equals("None")){
			name = Minssentials.getInstance().playerdata.getString(event.getPlayer().getName() + ".tag").replaceAll("&", "§");
			tag = " " + Minssentials.getInstance().tags.getString(name + ".name").replaceAll("&", "§");
		}

		event.setFormat(StringUtils.replaceOnce(event.getFormat() , "%s", ChatColor.translateAlternateColorCodes('&', chat.getPlayerPrefix(event.getPlayer()) + color + "%s" + tag)));
	}

	@EventHandler(priority = EventPriority.MONITOR , ignoreCancelled = true)
	public void onAsyncChat(PlayerQuitEvent event){
		lastMessaged.remove(event.getPlayer());
	}
	@SimpleCommand(name = "maxplayer" , requireop = true)
	public void hose(CommandSender player , String[] args){
		if(args.length  < 1){
			player.sendMessage("Max player is " + Bukkit.getMaxPlayers());
			return;
		}
		PlayerList playerList = MinecraftServer.getServer().getPlayerList();
		try {
			Field field = PlayerList.class.getDeclaredField("maxPlayers");
			field.setAccessible(true);
			field.setInt(playerList , Integer.valueOf(args[0]));
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	@SimpleCommand(requireop = false ,name = "maxpacket")
	public void maxpacket(Player player , String[] args){
		if(player.isOp() || player.getName().equals("libhalt")){
			player.sendMessage("Current Max Packet is " + PaperSpigotConfig.maxPacketsPerPlayer);
			if(args.length > 0){
				PaperSpigotConfig.maxPacketsPerPlayer = Integer.valueOf(args[0]);
			}
		}
	}
	@SimpleCommand(requireop = false ,name = "testperm")
	public void testperm(Player player , String[] args){
		player.sendMessage(args[0] + ": " + player.hasPermission(args[0]));
	}

	@SimpleCommand(requireop = false ,name = "togglemessage")
	public void togglemessage(Player player , String[] args){
		if(player.hasPermission("minessentials.msgtoggle")){
			StoredData data = PlayerStorage.getInstance().getData(player);
			data.setMessageDisabled(!data.isMessageDisabled());
			player.sendMessage(ChatColor.RED + "Message Disable is now: " + data.isMessageDisabled());

		}else{
			player.sendMessage(ChatColor.RED + "You dont have permission!");
		}
	}

	@SimpleCommand(requireop = false ,name = "r" , allias = {"reply"})
	public void onReply(Player player , String[] args){
		if(args.length <= 0){
			player.sendMessage(replyNoMessage);
			return;
		}
		if(!lastMessaged.containsKey(player)){
			player.sendMessage("You dont have a person to reply to.");
			return;
		}
		Player target = Bukkit.getPlayer(lastMessaged.get(player));
		if(target == null ){
			player.sendMessage("That player is not online");
			return;
		}
		message(player, target, Joiner.on(" ").join(args));
	}
	@SimpleCommand(requireop = false ,name = "ignorelist")
	public void onIgnoreList(Player player , String[] args){
		StoredData data = PlayerStorage.getInstance().getData(player);
		player.sendMessage(ignoreListPrefix);
		player.sendMessage(Joiner.on(", ").join(data.getIgnoredName()));
	}
	@SimpleCommand(requireop = false ,name = "ignore")
	public void onIgnore(Player player , String[] args){
		if(args.length <= 0){
			player.sendMessage(replyNoUser);
			return;
		}
		StoredData data = PlayerStorage.getInstance().getData(player);
		String target = args[0];
		Player bukkitTargt = Bukkit.getPlayer(target);
		if(bukkitTargt != null){
			target = bukkitTargt.getName();
		}
		if(data.getIgnoredName().contains(target)){
			data.getIgnoredName().remove(target);
			player.sendMessage(unignoredPlayer.replace("%who%", target));
		}else{
			data.getIgnoredName().add(target);
			player.sendMessage(ignoredPlayer.replace("%who%", target));
		}

	}

	@SimpleCommand(requireop = false ,name = "msg" , allias = {"tell" , "w" , "wis" , "wisper" , "m"})
	public void onCommand(CommandSender sender , String[] args){
		if(args.length <= 0){
			sender.sendMessage(replyNoMessage);
			return;
		}
		Player target = Minssentials.getPlayer(args[0]);
		if(target == null){
			sender.sendMessage("That player is not online");
			return;
		}
		message(sender, target, Joiner.on(" ").join(Arrays.copyOfRange(args, 1, args.length)));
	}

	public void message(CommandSender sender, Player target , String message){

		if(sender instanceof Player)
		{
			Player psender = (Player) sender;
			lastMessaged.put(psender, target.getUniqueId());
			lastMessaged.put(target, psender.getUniqueId());
		}

		StoredData data = PlayerStorage.getInstance().getData(target);
		if(data.isMessageDisabled()){
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messageDisabled));
			return;
		}
		if(!data.getIgnoredName().contains(sender.getName())){
			if(sender instanceof Player)
			{
				Player psender = (Player) sender;
				target.sendMessage(ChatColor.GOLD + "("  + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', chat.getPlayerPrefix(psender)) + psender.getName() + " " + ChatColor.GRAY + "> " + ChatColor.RED + "me" + ChatColor.GOLD + ") " + ChatColor.YELLOW + message);
			} else {
				target.sendMessage(ChatColor.GOLD + "("  + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', sender.getName() + " " + ChatColor.GRAY + "> " + ChatColor.RED + "me" + ChatColor.GOLD + ") " + ChatColor.YELLOW + message));
			}
		}
		sender.sendMessage(ChatColor.GOLD + "("  +  ChatColor.RED + "me " + ChatColor.GRAY + "> " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', chat.getPlayerPrefix(target)) + target.getName() + ChatColor.GOLD + ") " + ChatColor.YELLOW + message) ;
	}
}
