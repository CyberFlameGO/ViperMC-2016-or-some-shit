package net.libhalt.bukkit.kaede;

import com.google.common.collect.Lists;

import java.util.List;

public class PlayerData {
	private int pvpTime;
	private int kills;
	private boolean killOnLogin;
	private long combatTagMillisecond;
	private int lives ;
	private transient long createdTime;
	{
		createdTime = System.currentTimeMillis();
	}
	public int getPvpTime() {
		return this.pvpTime;
	}


	public long getCombatTagMillisecond() {
		return combatTagMillisecond;
	}



	public void setCombatTagMillisecond(long combatTagMillisecond) {
		this.combatTagMillisecond = combatTagMillisecond;
	}



	public void setPvpTime(int pvpTime) {
		this.pvpTime = pvpTime;
	}

	public int getKills() {
		return this.kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public boolean isKillOnLogin() {
		return this.killOnLogin;
	}

	public void setKillOnLogin(boolean killOnLogin) {
		this.killOnLogin = killOnLogin;
	}



	public int getLives() {
		return lives;
	}



	public void setLives(int lives) {
		this.lives = lives;
	}



	public long getCreatedTime() {
		return createdTime;
	}



	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}
	
		
}
