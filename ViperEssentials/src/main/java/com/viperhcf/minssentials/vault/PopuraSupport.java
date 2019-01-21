package com.viperhcf.minssentials.vault;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.viperhcf.minssentials.Minssentials;

import net.libhalt.bukkit.kaede.utils.MilliToSecondFormatter;
import net.md_5.bungee.api.ChatColor;


public class PopuraSupport implements Listener{

	private Minssentials plugin;
	public void init(Minssentials plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onScoreboard(net.libhalt.bukkit.kaede.event.ScoreboardTextAboutToUpdateEvent event){
		if(event.getText() != null && event.getText().contains("%mutechat%")){
			if(event.getPlayer().hasPermission("mutechat.sb") && plugin.getCommandMuteChat().isMuted()){
				event.setText(event.getText().replace("%mutechat%", ChatColor.RED + "Yes"));
			}else{
				event.setText(null);
			}
		}
		if(event.getText() != null && event.getText().contains("%slowchat%")){
			if(event.getPlayer().hasPermission("slowchat.sb") && plugin.getCommandSlowChat().isSlowed()){
				event.setText(event.getText().replace("%slowchat%", String.valueOf(plugin.getCommandSlowChat().getInteerval())));
			}else{
				event.setText(null);
			}
		}
	}
}
