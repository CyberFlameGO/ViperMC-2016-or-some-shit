package net.libhalt.bukkit.kaede.manager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.Manager;

public class UnEnchantBookManager extends Manager implements Listener {
	public UnEnchantBookManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE && event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType() == Material.ENCHANTED_BOOK) {
			event.getPlayer().sendMessage(ChatColor.GREEN + "You have disenchanted the book");
			event.getPlayer().setItemInHand(new ItemStack(Material.BOOK));
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ANVIL_USE, 1, 1);
		}

	}
}
