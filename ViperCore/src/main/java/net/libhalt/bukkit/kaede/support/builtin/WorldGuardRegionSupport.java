package net.libhalt.bukkit.kaede.support.builtin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.libhalt.bukkit.kaede.support.IRegion;
import net.libhalt.bukkit.kaede.support.RegionSupport;

public class WorldGuardRegionSupport implements RegionSupport{

	@Override
	public List<IRegion> getRegionsAt(Location location) {
		List<IRegion> regions = new ArrayList<IRegion>();
		for(ProtectedRegion region :  WGBukkit.getPlugin().getRegionManager(location.getWorld()).getApplicableRegions(location)){
			if(region !=  null){
				regions.add(new WorldGuardRegion(location.getWorld().getName(), region));
			}
		}
		return regions;
	}

	@Override
	public List<IRegion> getNearByRegion(Location location) {
		List<IRegion> regions = new ArrayList<IRegion>();
		for(ProtectedRegion region :  WGBukkit.getPlugin().getRegionManager(location.getWorld()).getRegions().values()){
			if(region !=  null){
				regions.add(new WorldGuardRegion(location.getWorld().getName(), region));
			}
		}
		return regions;
	}


}
