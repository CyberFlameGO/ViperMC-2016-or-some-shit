package net.libhalt.bukkit.kaede.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.libhalt.bukkit.kaede.support.builtin.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.libhalt.bukkit.kaede.HCFactionPlugin;

public class Support{

	private static Support INSTANCE = new Support();

	public static Support getInstance(){
		return INSTANCE;
	}

	private List<RegionSupport> regionSupporters = new ArrayList<RegionSupport>();
	private List<TeamSupport> teamSupporters = new ArrayList<TeamSupport>();

	public void register(HCFactionPlugin plugin){
		if(Bukkit.getPluginManager().getPlugin("WorldGuard") != null){
			regionSupporters.add(new WorldGuardRegionSupport());
		}
		if(Bukkit.getPluginManager().getPlugin("Popura") != null){
			try{
				regionSupporters.add(new ShockDTRFactionRegionSupport());
				teamSupporters.add(new ShockDTRFactionTeamSupport());
				/*
				new ShockDTRScoreboardSupport().register(plugin);
				new ShockDTRCombatLoggerSupport().register(plugin);
				new ShockDTRSpawnSupport().register(plugin);

*/
			}catch(Throwable t){
				t.printStackTrace();
			}
		}

		regionSupporters.add(new SubsideRegionSupport());
		plugin.getServer().getPluginManager().registerEvents(new SubsideKothSupportListener(), plugin);

	}

	public Set<Player> getEveryoneInTeam(Player player){
		Set<Player> set = new HashSet<Player>();
		for(TeamSupport teamSupport: getTeamSupporters()){
			set.addAll(teamSupport.getPlayerInTeam(player));
		}
		return set;
	}

	public Set<Player> getEveryoneInTeamNearByForBard(Player player){
		Set<Player> set = new HashSet<Player>();
		for(TeamSupport teamSupport: getTeamSupporters()){
			set.addAll(teamSupport.getPlayerInTeamToApplyBard(player));
		}
		return set;
	}
	public List<TeamSupport> getTeamSupporters(){
		return teamSupporters;
	}
	public List<RegionSupport> getRegionSupporters(){
		return regionSupporters;
	}
	public boolean isIn(IRegion region, Location location) {
		int xmin = Math.min(region.getMinX(), region.getMaxX());
		int xmax = Math.max(region.getMinX(), region.getMaxX());
		int zmin = Math.min(region.getMinZ(), region.getMaxZ());
		int zmax = Math.max(region.getMinZ(), region.getMaxZ());
		int ymin = Math.min(region.getMinY(), region.getMaxY());
		int ymax = Math.max(region.getMinY(), region.getMaxY());
		String world = region.getWorld();
		return world.equals(location.getWorld().getName()) && (location.getBlockY() >= ymin && location.getBlockY() <= ymax || location.getBlockY() <= ymin && location.getBlockY() >= ymax) && (location.getBlockX() >= xmin && location.getBlockX() <= xmax || location.getBlockX() <= xmin && location.getBlockX() >= xmax) && (location.getBlockZ() >= zmin && location.getBlockZ() <= zmax || location.getBlockZ() <= zmin && location.getBlockZ() >= zmax);
	}
	public boolean isInIgnoreWorld(IRegion region, Location location) {
		int xmin = Math.min(region.getMinX(), region.getMaxX());
		int xmax = Math.max(region.getMinX(), region.getMaxX());
		int zmin = Math.min(region.getMinZ(), region.getMaxZ());
		int zmax = Math.max(region.getMinZ(), region.getMaxZ());
		int ymin = Math.min(region.getMinY(), region.getMaxY());
		int ymax = Math.max(region.getMinY(), region.getMaxY());
		return (location.getBlockY() >= ymin && location.getBlockY() <= ymax || location.getBlockY() <= ymin && location.getBlockY() >= ymax) && (location.getBlockX() >= xmin && location.getBlockX() <= xmax || location.getBlockX() <= xmin && location.getBlockX() >= xmax) && (location.getBlockZ() >= zmin && location.getBlockZ() <= zmax || location.getBlockZ() <= zmin && location.getBlockZ() >= zmax);
	}

}
