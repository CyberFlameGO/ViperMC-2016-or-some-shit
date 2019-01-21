package net.libhalt.bukkit.kaede.event;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CombatLoggerDeathEvent extends Event{
	private static final HandlerList handlers = new HandlerList();

	private Entity loggerEntity;
	private UUID playerUUID;
	
	
	public CombatLoggerDeathEvent(Entity loggerEntity, UUID playerUUID) {
		this.loggerEntity = loggerEntity;
		this.playerUUID = playerUUID;
	}
	
	

	public Entity getLoggerEntity() {
		return loggerEntity;
	}



	public UUID getPlayerUUID() {
		return playerUUID;
	}



	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
