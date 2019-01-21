package net.libhalt.bukkit.kaede.support.builtin;

import net.libhalt.bukkit.kaede.support.IRegion;
import net.libhalt.bukkit.kaede.support.RegionSupport;
import net.libhalt.bukkit.kaede.support.Support;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import subside.plugins.koth.KothPlugin;
import subside.plugins.koth.gamemodes.RunningKoth;

import java.util.ArrayList;
import java.util.List;

public class SubsideRegionSupport implements RegionSupport{

	@Override
	public List<IRegion> getRegionsAt(Location location) {
		List<IRegion> regions = new ArrayList<IRegion>();
		for(RunningKoth wrap : JavaPlugin.getPlugin(KothPlugin.class).getKothHandler().getRunningKoths()){
			SubsideRegion subsideRegion = new SubsideRegion(wrap.getKoth());
			if(Support.getInstance().isIn(subsideRegion , location)){
				regions.add(new SubsideRegion(wrap.getKoth()));
			}
		}
		return regions;
	}

	@Override
	public List<IRegion> getNearByRegion(Location location) {
		List<IRegion> regions = new ArrayList<IRegion>();
		for(RunningKoth wrap : JavaPlugin.getPlugin(KothPlugin.class).getKothHandler().getRunningKoths()){
			regions.add(new SubsideRegion(wrap.getKoth()));
		}
		return regions;
	}


}
