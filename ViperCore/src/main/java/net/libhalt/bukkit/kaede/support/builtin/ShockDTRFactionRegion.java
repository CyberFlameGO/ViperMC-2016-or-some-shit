package net.libhalt.bukkit.kaede.support.builtin;

import org.bukkit.entity.Player;

import net.libhalt.bukkit.kaede.support.IRegion;
import net.syuu.popura.faction.FactionType;
import net.syuu.popura.faction.bean.ClaimedRegion;
import net.syuu.popura.faction.bean.Faction;
import net.syuu.popura.faction.bean.FactionPlayer;

public class ShockDTRFactionRegion implements IRegion{
	private ClaimedRegion flocation;
	public ShockDTRFactionRegion(ClaimedRegion flocation) {
		this.flocation = flocation;
	}
	@Override
	public int getMinX() {
		return flocation.getMinX();
	}

	@Override
	public int getMaxX() {
		return flocation.getMaxX();
	}

	@Override
	public int getMinZ() {
	    return flocation.getMinZ();

	}

	@Override
	public int getMaxZ() {
		return flocation.getMaxZ();
	}

	@Override
	public int getMinY() {
		return Integer.MIN_VALUE;
	}

	@Override
	public int getMaxY() {
		return Integer.MAX_VALUE;
	}

	@Override
	public String getWorld() {
		return this.flocation.getWorld();
	}
	@Override
	public boolean canCombatTagedPlayerEnter() {
		Faction faction = flocation.getOwner();
		if(faction.getFactionType()  == FactionType.SAFEZONE){
			return false;
		}
		return true;
	}
	@Override
	public boolean shouldPvPTimerFreeze() {
		Faction faction = flocation.getOwner();
		if(faction.getFactionType()  == FactionType.SAFEZONE){
			return true;
		}
		return false;
	}
	@Override
	public boolean canPvPTimedPlayerEnter(Player player) {
		Faction faction = flocation.getOwner();
		if(faction.getFactionType() != FactionType.NORMAL){
			if(faction.getName().toLowerCase().contains("koth") || faction.getName().toLowerCase().contains("conquest")){
				return false;
			}
			return true;
		}

		return false;
	}

	public boolean canPvPTimedPlayerEnter(FactionPlayer factionPlayer){
		Faction faction = flocation.getOwner();
		if(faction.getFactionType() != FactionType.NORMAL){
			if(faction.getName().toLowerCase().contains("koth") || faction.getName().toLowerCase().contains("conquest")){
				return false;
			}
			return true;
		}

		return false;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ShockDTRFactionRegion that = (ShockDTRFactionRegion) o;

		return flocation != null ? flocation.equals(that.flocation) : that.flocation == null;

	}

	@Override
	public int hashCode() {
		return flocation != null ? flocation.hashCode() : 0;
	}
}
