package net.libhalt.bukkit.kaede.support.builtin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.faction.bean.Faction;
import net.syuu.popura.faction.bean.FactionPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.libhalt.bukkit.kaede.support.TeamSupport;

public class ShockDTRFactionTeamSupport implements TeamSupport{

	@Override
	public List<Player> getPlayerInTeamToApplyBard(Player player) {
		List<Player> team = new ArrayList<Player>();
		FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);

		Faction faction = factionPlayer.getFaction();
		if (faction != null) {
			Collection<Player> players = faction.getOnlineMembers();
			for(Entity entity :player.getNearbyEntities(12.0D, 12.0D, 12.0D) ){
				if (entity instanceof Player) {
					Player other = (Player) entity;
					if (players.contains(other)) {
						team.add(other);
					}
				}
			}
		}
		team.add(player);
		return team;
	}

	@Override
	public List<Player> getPlayerInTeam(Player player) {
		List<Player> team = new ArrayList<Player>();
		FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
		Faction faction = factionPlayer.getFaction();

		if (faction != null) {
			for(Player other : faction.getOnlineMembers()){
				if (!team.contains(other)) {
					team.add(other);
				}
			}
		}
		team.add(player);
		return team;

	}


}
