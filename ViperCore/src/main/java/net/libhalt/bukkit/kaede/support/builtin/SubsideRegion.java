package net.libhalt.bukkit.kaede.support.builtin;

import net.libhalt.bukkit.kaede.support.IRegion;
import org.bukkit.entity.Player;
import subside.plugins.koth.areas.Koth;

public class SubsideRegion implements IRegion{
	private Koth protectedRegion;
	public SubsideRegion(Koth protectedRegion) {
		this.protectedRegion = protectedRegion;
	}
	@Override
	public int getMinX() {
		return Math.min(protectedRegion.getAreas().get(0).getMin().getBlockX() , protectedRegion.getAreas().get(0).getMax().getBlockX());
	}

	@Override
	public int getMaxX() {
		return Math.max(protectedRegion.getAreas().get(0).getMin().getBlockX() , protectedRegion.getAreas().get(0).getMax().getBlockX());

	}

	@Override
	public int getMinZ() {
		return Math.min(protectedRegion.getAreas().get(0).getMin().getBlockZ() , protectedRegion.getAreas().get(0).getMax().getBlockZ());

	}

	@Override
	public int getMaxZ() {
		return Math.max(protectedRegion.getAreas().get(0).getMin().getBlockZ() , protectedRegion.getAreas().get(0).getMax().getBlockZ());

	}

	@Override
	public int getMinY() {
		return Math.min(protectedRegion.getAreas().get(0).getMin().getBlockY() , protectedRegion.getAreas().get(0).getMax().getBlockY());

	}

	@Override
	public int getMaxY() {
		return Math.max(protectedRegion.getAreas().get(0).getMin().getBlockY() , protectedRegion.getAreas().get(0).getMax().getBlockY());
	}

	@Override
	public String getWorld() {
		return protectedRegion.getAreas().get(0).getMiddle().getWorld().getName();
	}

	@Override
	public boolean canCombatTagedPlayerEnter() {
		return true;
	}

	@Override
	public boolean shouldPvPTimerFreeze() {
		return false;
	}
	
	@Override
	public boolean canPvPTimedPlayerEnter(Player player) {
		return false;
	}

}
