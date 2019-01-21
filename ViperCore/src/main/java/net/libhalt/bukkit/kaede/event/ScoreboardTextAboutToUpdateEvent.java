package net.libhalt.bukkit.kaede.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ScoreboardTextAboutToUpdateEvent extends PlayerEvent{
	private static final HandlerList handlers = new HandlerList();

	private String text;
	private boolean shouldAlwaysDisplay;
	public ScoreboardTextAboutToUpdateEvent(Player who , String text , boolean shouldAlwaysDisplay) {
		super(who);
		this.text = text;
		this.shouldAlwaysDisplay = shouldAlwaysDisplay;
	}
	
	
	public boolean isShouldAlwaysDisplay() {
		return shouldAlwaysDisplay;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
