package net.libhalt.bukkit.kaede.manager;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.HashMap;

public class GappleManager extends Manager implements Listener , CommandExecutor {
	private HashMap<String, Boolean> player_cooldown;
	private ConfigurationWrapper config;

	public YamlConfiguration getConfig(){
		return config.getConfig();
	}
	public GappleManager(HCFactionPlugin plugin) {
		super(plugin);
		this.player_cooldown = new HashMap<String, Boolean>();
	}

	@Override
	public void init() {
		getPlugin().getServer().getPluginManager().registerEvents(this	 , getPlugin());
		this.config = new ConfigurationWrapper("gapple.yml", this.getPlugin());
		getPlugin().getCommand("gapplecooldown").setExecutor(this);
	}

	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		final Player p = (Player)sender;
		if (!sender.hasPermission("gapplecooldown.version")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("NoPermission message")));
		}
		else if (sender.hasPermission("gapplecooldown.version")) {
			p.sendMessage("This server is running GappleCooldown " + ChatColor.RED + "v1.0.1" + ChatColor.RESET + ".");
		}
		return false;
	}

	@EventHandler
	public void Gapple_Cooldown(final PlayerItemConsumeEvent e) {
		final Player p = e.getPlayer();
		final ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 1, (short)1);
		if (e.getItem().getType().equals((Object)Material.GOLDEN_APPLE) && e.getItem().getData().equals((Object)apple.getData())) {
			if (!this.player_cooldown.containsKey(p.getName())) {
				this.player_cooldown.put(p.getName(), false);
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Eat message")));
			}
			if (!this.player_cooldown.get(p.getName())) {
				Bukkit.getScheduler().runTaskLater(getPlugin(), (Runnable)new Runnable() {
					@Override
					public void run() {
						player_cooldown.put(p.getName(), false);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Expiry message")));
					}
				}, (long)(this.getConfig().getInt("Cooldown time") * 1200));
				this.player_cooldown.put(p.getName(), true);
			}
			else {
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Cooldown message")));
				e.setCancelled(true);
			}
		}
	}

}
