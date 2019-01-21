package net.libhalt.bukkit.kaede.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;

public class CraftingDisableManager extends Manager implements Listener{

	private ConfigurationWrapper config;
	private Map<Material , List<Short>> disabledCrafting = new HashMap<>();
	public CraftingDisableManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		config = new ConfigurationWrapper("disable-crafting.yml", getPlugin());	
		reload();
		getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
		
	}
	
	@Override
	public void reload() {
		disabledCrafting.clear();
		ConfigurationSection section = config.getConfig().getConfigurationSection("disabled-crafting");
		for(String key : section.getKeys(false)){
			List<Short> bytes = section.getShortList(key);
			Material material = null;
			try{
				material = Material.valueOf(key);;
			}catch(IllegalArgumentException e){
				getPlugin().getLogger().severe("While reading CraftingDisabled, " + key + " could not be parased as material");
				continue;
			}
			disabledCrafting.put(material, bytes);
		}
	}

	
	@EventHandler
	public void onCraft(PrepareItemCraftEvent event){
		ItemStack item = event.getInventory().getResult();
		if(disabledCrafting.containsKey(item.getType()) && disabledCrafting.get(item.getType()).contains(item.getDurability())){
			event.getInventory().setResult(new ItemStack(Material.AIR));
		}
	}
	

}
