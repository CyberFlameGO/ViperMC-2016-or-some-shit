package net.libhalt.bukkit.kaede.support.builtin;

import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.libhalt.bukkit.kaede.support.IRegion;

public class WorldGuardRegion implements IRegion{
	private ProtectedRegion protectedRegion;
	private String world;
	public WorldGuardRegion(String world , ProtectedRegion protectedRegion) {
		this.protectedRegion = protectedRegion;
		this.world = world;
	}
	@Override
	public int getMinX() {
		return protectedRegion.getMinimumPoint().getBlockX();
	}

	@Override
	public int getMaxX() {
		return protectedRegion.getMaximumPoint().getBlockX();

	}

	@Override
	public int getMinZ() {
		return protectedRegion.getMinimumPoint().getBlockZ();
	}

	@Override
	public int getMaxZ() {
		return protectedRegion.getMaximumPoint().getBlockZ();

	}

	@Override
	public int getMinY() {
		return protectedRegion.getMinimumPoint().getBlockY();

	}

	@Override
	public int getMaxY() {
		return protectedRegion.getMaximumPoint().getBlockY();
	}

	@Override
	public String getWorld() {
		return world;
	}

	@Override
	public boolean canCombatTagedPlayerEnter() {
		return protectedRegion.getFlag(DefaultFlag.PVP) != State.DENY;
	}

	@Override
	public boolean shouldPvPTimerFreeze() {
		return protectedRegion.getFlag(DefaultFlag.PVP) == State.DENY;
	}
	
	@Override
	public boolean canPvPTimedPlayerEnter(Player player) {
		return true;
	}

}
