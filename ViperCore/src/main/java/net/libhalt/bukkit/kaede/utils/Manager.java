package net.libhalt.bukkit.kaede.utils;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import org.bukkit.command.CommandExecutor;

public abstract class Manager {
	private HCFactionPlugin plugin;

	public Manager(HCFactionPlugin plugin) {
		this.plugin = plugin;
	}

	public void init() {
	}

	public void tear() {
	}

	public void reload() {
	}

	public void registerCommand(String cmd, CommandExecutor executor) {
		getPlugin().getCommand(cmd).setExecutor(executor);
	}

	public HCFactionPlugin getPlugin() {
		return this.plugin;
	}
}
