package net.libhalt.bukkit.kaede.support.builtin;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.claim.Position2D;
import net.syuu.popura.faction.bean.ClaimedRegion;
import org.bukkit.Location;


import net.libhalt.bukkit.kaede.support.IRegion;
import net.libhalt.bukkit.kaede.support.RegionSupport;

public class ShockDTRFactionRegionSupport implements RegionSupport {

	@Override
	public List<IRegion> getRegionsAt(Location location) {
		List<IRegion> regions = new ArrayList<IRegion>();

		ClaimedRegion flocation = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(location.getWorld().getName() , location.getBlockX() , location.getBlockZ()));
		if(flocation != null) {
			regions.add(new ShockDTRFactionRegion(flocation));
		}
		return regions;
	}

	@Override
	public List<IRegion> getNearByRegion(Location location) {
		List<IRegion> regions = new ArrayList<IRegion>();
		ClaimedRegion flocation = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(location.getWorld().getName() , location.getBlockX() , location.getBlockZ()));
		if(flocation != null) {
			regions.add(new ShockDTRFactionRegion(flocation));
		}
		for(int x = -4; x <= 4; ++x) {
			for(int z = -4; z <= 4; ++z) {
				ClaimedRegion claimedRegion = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(location.getWorld().getName() , location.getBlockX() + x , location.getBlockZ() + z));
				if(claimedRegion != null) {
					ShockDTRFactionRegion shock = new ShockDTRFactionRegion(claimedRegion);
					if (!regions.contains(shock)) {
						regions.add(shock);
					}
				}
			}
		}
		return regions;

	}

}
