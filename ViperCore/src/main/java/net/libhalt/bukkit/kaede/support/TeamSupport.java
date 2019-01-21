package net.libhalt.bukkit.kaede.support;

import java.util.List;

import org.bukkit.entity.Player;

public interface TeamSupport {
	
	public List<Player> getPlayerInTeamToApplyBard(Player player);

	public List<Player> getPlayerInTeam(Player player);

}
