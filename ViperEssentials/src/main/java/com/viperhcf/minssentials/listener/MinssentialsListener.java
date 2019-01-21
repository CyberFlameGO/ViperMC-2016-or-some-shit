package com.viperhcf.minssentials.listener;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.viperhcf.minssentials.Minssentials;
import com.viperhcf.minssentials.PlayerStorage;
import com.viperhcf.minssentials.StoredData;

import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;

public class MinssentialsListener implements Listener{

	private static final Pattern URL_PATTERN = Pattern.compile("(http(s)?:\\/\\/.)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event){
		if(event.getResult() == Result.KICK_FULL && event.getPlayer().hasPermission("minssentials.fulljoin")){
			event.setResult(Result.ALLOWED);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerCommandPreprocessEvent event){
		String command = event.getMessage();
		
		if(event.getMessage().contains(":")){
			command = event.getMessage().split(Pattern.quote(":"))[1];
		}
		command = command.toLowerCase();
		if(command.startsWith("calc") || command.startsWith("/calc") || command.startsWith("//calc") || command.startsWith("calc")
				||command.startsWith("eval") ||  command.startsWith("/eval") || command.startsWith("//eval")
				||command.startsWith("sol") ||  command.startsWith("/sol") || command.startsWith("//sol")){

			event.setMessage("/itriedtocrashteserverwith/calc");
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		if(player.hasPermission("minssentials.chat.color")){
			event.setMessage(translateAlternateStrictlyColor('&' , event.getMessage()));
		}
		if(player.hasPermission("minssentials.chat.format")){
			event.setMessage(translateAlternateStrictlyFormat('&' , event.getMessage()));
		}
		if(!player.hasPermission("minssentials.chat.link") && URL_PATTERN.matcher(event.getMessage().trim()).find()){
			event.setMessage(event.getMessage().replace(".", " "));
		}
		Iterator<Player> iter = event.getRecipients().iterator();
		while (iter.hasNext()) {
			Player other = (Player) iter.next();
			StoredData data = PlayerStorage.getInstance().getData(other);
			if(data != null && data.getIgnoredName().contains(player.getName())){
				iter.remove();	
			}
		}
	}

	public static String translateAlternateStrictlyFormat(char altColorChar, String textToTranslate) {
		char[] b = textToTranslate.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == altColorChar && "KkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
				b[i] = ChatColor.COLOR_CHAR;
				b[i+1] = Character.toLowerCase(b[i+1]);
			}
		}
		return new String(b);
	}
	public static String translateAlternateStrictlyColor(char altColorChar, String textToTranslate) {
		char[] b = textToTranslate.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == altColorChar && "0123456789AaBbCcDdEeFf".indexOf(b[i+1]) > -1) {
				b[i] = ChatColor.COLOR_CHAR;
				b[i+1] = Character.toLowerCase(b[i+1]);
			}
		}
		return new String(b);
	}
}
