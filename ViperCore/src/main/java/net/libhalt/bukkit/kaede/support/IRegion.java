package net.libhalt.bukkit.kaede.support;

import org.bukkit.entity.Player;

public interface IRegion {

	public int getMinX();
	public int getMaxX();

	public int getMinZ();


	public int getMaxZ();
	public int getMinY();

	public int getMaxY();
	
	
	public String getWorld();
	
	public boolean canCombatTagedPlayerEnter();
	
	public boolean shouldPvPTimerFreeze();


	public boolean canPvPTimedPlayerEnter(Player player);
}

