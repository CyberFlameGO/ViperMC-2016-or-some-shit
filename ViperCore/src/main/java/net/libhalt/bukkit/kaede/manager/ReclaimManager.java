package net.libhalt.bukkit.kaede.manager;

import com.google.common.collect.Lists;
import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import net.syuu.common.command.CommandRegistry;
import net.syuu.common.command.annotation.SimpleCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReclaimManager extends Manager implements Listener {
	private ConfigurationWrapper config;
	private List<ReclaimRank> reclaimRanks = Lists.newArrayList();
	public ReclaimManager(HCFactionPlugin plugin) {
		super(plugin);
	}
	private File reclaimedDir;
	private File liveReclaimedDir;

	private String alreadyReclaimed;
	private String nothingToReclaim;
	private String reclaimed;
	@Override
	public void init() {
		CommandRegistry.getInstance().registerSimpleCommand(this);
		reclaimedDir = new File(getPlugin().getDataFolder() , "reclaimed");
		liveReclaimedDir = new File(getPlugin().getDataFolder() , "livesclaim");

		reclaimedDir.mkdirs();
		reload();
	}

	@Override
	public void reload() {
		this.config = new ConfigurationWrapper("reclaim.yml", this.getPlugin());
		alreadyReclaimed = ChatColor.translateAlternateColorCodes('&' , config.getConfig().getString("message.already-reclaimed"));
		nothingToReclaim = ChatColor.translateAlternateColorCodes('&' , config.getConfig().getString("message.nothing-to-reclaim"));
		reclaimed = ChatColor.translateAlternateColorCodes('&' ,config.getConfig().getString("message.reclaimed"));

		Configuration configuration = config.getConfig();
		for(String key : configuration.getKeys(false)){
			if(key.equalsIgnoreCase("message")){
				continue;
			}
			ConfigurationSection section = configuration.getConfigurationSection(key);
			String permission = section.getString("permission");
			List<String> commands = section.getStringList("commands");
			int prirority = section.getInt("priority");
			ReclaimRank rank = new ReclaimRank(key , permission , prirority ,commands);
			System.out.println(rank);
			reclaimRanks.add(rank);
		}
	}
	@SimpleCommand(requireop = false , name =  "livesclaim")
	public void liveReclaimedDir(Player player , String[] args){

		List<ReclaimRank> all = Lists.newArrayList();
		for(ReclaimRank rank : reclaimRanks){
			if(player.hasPermission(rank.permission)){
				all.add(rank);
			}
		}
		File flagFile = new File(liveReclaimedDir , player.getUniqueId() + ".flag" );
		if(flagFile.exists()){
			player.sendMessage(alreadyReclaimed);
			return;
		}
		if(!all.isEmpty()){
			Collections.sort(all, new Comparator<ReclaimRank>() {
				@Override
				public int compare(ReclaimRank o1, ReclaimRank o2) {
					return o2.priority - o1.priority;
				}
			});
			System.out.println(all);
			ReclaimRank rank = all.iterator().next();
			for(String cmd : rank.commands){
				try {
					if (cmd.toLowerCase().contains("live")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			player.sendMessage(ChatColor.translateAlternateColorCodes('&' , "&9&lViper &7// &eYou have received your lives from your rank."));
			try {
				flagFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			player.sendMessage(nothingToReclaim);
		}

	}
	@SimpleCommand(requireop = false , name =  "reclaim")
	public void onCommand(Player player , String[] args){

		List<ReclaimRank> all = Lists.newArrayList();
		for(ReclaimRank rank : reclaimRanks){
			if(player.hasPermission(rank.permission)){
				all.add(rank);
			}
		}
		File flagFile = new File(reclaimedDir , player.getUniqueId() + ".flag" + all.size());
		if(flagFile.exists()){
			player.sendMessage(alreadyReclaimed);
			return;
		}
		if(!all.isEmpty()){
			Collections.sort(all, new Comparator<ReclaimRank>() {
				@Override
				public int compare(ReclaimRank o1, ReclaimRank o2) {
					return o2.priority - o1.priority;
				}
			});
			System.out.println(all);
			ReclaimRank rank = all.iterator().next();
			for(String cmd : rank.commands){
				try {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			player.sendMessage(reclaimed.replace("%rank%" ,   rank.name));
			try {
				flagFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			player.sendMessage(nothingToReclaim);
		}

	}
	class ReclaimRank {
		private int priority;
		private String name;
		private String permission;
		private List<String> commands;

		public ReclaimRank(String name, String permission,  int priority , List<String> commands) {
			this.name = name;
			this.permission = permission;
			this.commands = commands;
			this.priority = priority;
		}

		@Override
		public String toString() {
			return "ReclaimRank{" +
					"priority=" + priority +
					", name='" + name + '\'' +
					", permission='" + permission + '\'' +
					", commands=" + commands +
					'}';
		}
	}

}
