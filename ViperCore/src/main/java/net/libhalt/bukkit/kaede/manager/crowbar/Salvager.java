package net.libhalt.bukkit.kaede.manager.crowbar;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Salvager {

	void onSalavage(Player player , ItemStack item , Block block);
}
