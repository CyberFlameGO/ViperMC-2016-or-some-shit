package net.libhalt.bukkit.kaede.support;

import java.util.List;

import org.bukkit.Location;

public interface RegionSupport {

	public List<IRegion> getRegionsAt(Location location);
	
	public List<IRegion> getNearByRegion(Location location);
	
}
