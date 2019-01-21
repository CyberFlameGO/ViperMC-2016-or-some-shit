package net.libhalt.bukkit.kaede.manager;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.Manager;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class 	GMelonManager extends Manager implements Listener {
	public GMelonManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {

		final Server server = getPlugin().getServer();
		final ShapelessRecipe gmelon = new ShapelessRecipe(new ItemStack(Material.SPECKLED_MELON));
		gmelon.addIngredient(1, Material.GOLD_NUGGET);
		gmelon.addIngredient(1, Material.MELON);
		server.addRecipe((Recipe)gmelon);
		ShapedRecipe shapedRecipe = new ShapedRecipe(new ItemStack(Material.GOLDEN_APPLE));
		shapedRecipe.shape(" G " , "GAG" , " G ");
		shapedRecipe.setIngredient('G' , Material.GOLD_INGOT);
		shapedRecipe.setIngredient('A' , Material.APPLE);
		server.addRecipe(shapedRecipe);
	}


}
