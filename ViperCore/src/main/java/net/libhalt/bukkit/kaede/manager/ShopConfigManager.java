package net.libhalt.bukkit.kaede.manager;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.PlayerData;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import net.syuu.popura.event.ShopParseEvent;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ShopConfigManager extends Manager implements Listener {
	private Map<String , ItemStack> alias = Maps.newHashMap();
	private ConfigurationWrapper config;

	public ShopConfigManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.config = new ConfigurationWrapper("shop.yml", this.getPlugin());
		reload();
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
	}

	@Override
	public void reload() {
		config.saveDefault();
		config.reloadConfig();
		alias.clear();
		for(String key : config.getConfig().getKeys(false)){
			ConfigurationSection section = config.getConfig().getConfigurationSection(key);
			System.out.println("Loaded shop for " + key  + ":" + parse(section));
			alias.put(key.toLowerCase() , parse(section));
		}
	}


	public ItemStack parse(ConfigurationSection configurationSection){
		Material material = Material.valueOf(configurationSection.getString("material"));
		int amount = configurationSection.getInt("amount");
		int damage = configurationSection.getInt("damage");
		ItemStack itemStack = new ItemStack(material , amount , (short) damage);
		if(configurationSection.contains("enchantments")) {
			ConfigurationSection enchantsection = configurationSection.getConfigurationSection("enchantments");
			for (String string : enchantsection.getKeys(false)) {
				int limit = configurationSection.getInt(string);
				Enchantment enchantment = null;
				try {
					enchantment = Enchantment.getByName(string.toUpperCase());
				} catch (IllegalArgumentException var6) {
					this.getPlugin().getLogger().warning("Illegal enchantment " + string + " found in enchantment-limiter");
					continue;
				}
				if (enchantment == null) {
					this.getPlugin().getLogger().warning("Null Enchantment at " + string);
				}
				itemStack.addUnsafeEnchantment(enchantment, limit);
			}

		}
		return itemStack;
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onShopParse(ShopParseEvent event) {
		if(alias.containsKey(event.getInput().toLowerCase())){
			ItemStack itemStack = alias.get(event.getInput().toLowerCase());
			event.setItemStack(itemStack);
		}
	}
}
