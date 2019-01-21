package net.libhalt.bukkit.kaede.support.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardEntryProcessFinishEvent extends PlayerEvent{
	private static final HandlerList handlers = new HandlerList();

	private Scoreboard scoreboard;
	public ScoreboardEntryProcessFinishEvent(Player who , Scoreboard scoreboard) {
		super(who);
		this.scoreboard = scoreboard;
	}
	
	
	public Scoreboard getScoreboard() {
		return scoreboard;
	}
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
