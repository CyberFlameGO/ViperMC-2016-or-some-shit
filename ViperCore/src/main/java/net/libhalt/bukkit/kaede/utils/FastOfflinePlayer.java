package net.libhalt.bukkit.kaede.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;

public class FastOfflinePlayer implements OfflinePlayer {
	private final String playerName;

	public FastOfflinePlayer(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public boolean isOnline() {
		return false;
	}

	@Override
	public String getName() {
		return this.playerName;
	}

	@Override
	public UUID getUniqueId() {
		return UUID.nameUUIDFromBytes(this.playerName.getBytes(Charsets.UTF_8));
	}

	@Override
	public boolean isBanned() {
		return false;
	}

	@Override
	public void setBanned(boolean banned) {
	}

	@Override
	public boolean isWhitelisted() {
		return false;
	}

	@Override
	public void setWhitelisted(boolean value) {
	}

	@Override
	public Player getPlayer() {
		return null;
	}

	@Override
	public long getFirstPlayed() {
		return System.currentTimeMillis();
	}

	@Override
	public long getLastPlayed() {
		return System.currentTimeMillis();
	}

	@Override
	public boolean hasPlayedBefore() {
		return false;
	}

	@Override
	public Location getBedSpawnLocation() {
		return new Location(Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D);
	}

	@Override
	public boolean isOp() {
		return false;
	}

	@Override
	public void setOp(boolean value) {
	}

	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String , Object> result = Maps.newLinkedHashMap();
		result.put("UUID", this.getUniqueId().toString());
		result.put("name", this.playerName);
		return result;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(new Object[] { this.playerName });
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FastOfflinePlayer)) {
			return false;
		} else {
			FastOfflinePlayer other = (FastOfflinePlayer) obj;
			return Objects.equal(this.playerName, other.playerName);
		}
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("playerName", this.playerName).toString();
	}
}
