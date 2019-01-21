package net.libhalt.bukkit.kaede.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;

public class EnchantmentLimitManager extends Manager implements Listener {
	private ConfigurationWrapper config;
	private Map<Enchantment , Integer> enchantmentLimits = new HashMap<Enchantment , Integer>();
	private boolean checkExsitingItem = false;
	public EnchantmentLimitManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.config = new ConfigurationWrapper("enchantment-limiter.yml", this.getPlugin());
		this.reload();
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
	}

	@Override
	public void reload() {
		checkExsitingItem = this.config.getConfig().getBoolean("remove-invalid-enchantments" , false);
		this.enchantmentLimits.clear();
		for(String string : this.config.getConfig().getConfigurationSection("enchantment-limit").getKeys(false)){
			int limit = this.config.getConfig().getInt("enchantment-limit." + string, Integer.MIN_VALUE);
			if (limit == Integer.MIN_VALUE) {
				this.getPlugin().getLogger().warning("Illegal level found for " + string + " in enchantment-limiter");
			} else {
				Enchantment enchantment = null;

				try {
					enchantment = Enchantment.getByName(string.toUpperCase());
				} catch (IllegalArgumentException var6) {
					this.getPlugin().getLogger().warning("Illegal enchantment " + string + " found in enchantment-limiter");
					continue;
				}
				if(enchantment == null){
					this.getPlugin().getLogger().warning("Null Enchantment at " + string);
					
				}

				this.enchantmentLimits.put(enchantment, Integer.valueOf(limit));
			}
		}

	}

	@EventHandler
	public void onPlayerFish(PlayerFishEvent event){
		if(event.getCaught() instanceof Item){
			Item item = (Item) event.getCaught();
			ItemStack stack = item.getItemStack();
			if(apply(stack , true)){
				item.setItemStack(stack);
			}
		}
	}
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		if (event.getItem() != null && this.apply(event.getItem().getItemStack())) {
			getPlugin().sendLocalized(event.getPlayer()	, "ILLGEL_ENCHANTMENT");
		}

	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (this.apply(event.getItem())) {
			getPlugin().sendLocalized(event.getPlayer()	, "ILLGEL_ENCHANTMENT");
		}

	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getSlotType() == SlotType.RESULT && event.getClickedInventory().getType() == InventoryType.ANVIL){
			Inventory inventory = event.getInventory();
			if (this.apply(event.getCursor() , true) || this.apply(event.getCurrentItem() , true)) {
				getPlugin().sendLocalized((Player)event.getWhoClicked(), "ILLGEL_ENCHANTMENT");
			}
		}else if (this.apply(event.getCursor()) || this.apply(event.getCurrentItem())) {
			getPlugin().sendLocalized((Player)event.getWhoClicked(), "ILLGEL_ENCHANTMENT");
		}

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDeath(EntityDeathEvent event){
		if(event.getEntity() instanceof Player){
			return;
		}
		for(ItemStack item : event.getDrops()){
			apply(item , true);
		}
	}
	@EventHandler
	public void onEnchantItem(EnchantItemEvent event){
		Iterator<Entry<Enchantment , Integer>> iter = event.getEnchantsToAdd().entrySet().iterator();
		boolean applied = false;
		while(iter.hasNext()){
			Entry<Enchantment , Integer> value = iter.next();
			if(this.enchantmentLimits.containsKey(value.getKey())){
				int max = this.enchantmentLimits.get(value.getKey());
				if(value.getValue() > max){
					value.setValue(max);
					applied = true;
				}
				if(value.getValue() <= 0){
					iter.remove();
				}
			}
		}

	}
	public boolean apply(ItemStack stack){
		return this.apply(stack , false);
	}

	public boolean apply(ItemStack stack , boolean force) {
		if (stack == null) {
			return false;
		} else {
			if(!force && !checkExsitingItem && stack.getType() != Material.ENCHANTED_BOOK){
				return false;
			}
			ItemMeta meta = stack.getItemMeta();
			boolean applied = false;
			if(meta instanceof EnchantmentStorageMeta){
				EnchantmentStorageMeta enchantmentStorage = (EnchantmentStorageMeta) meta;
				for(Enchantment enchantment : this.enchantmentLimits.keySet()){
					if(enchantmentStorage.hasEnchant(enchantment)){
						int level = enchantmentStorage.getStoredEnchantLevel(enchantment);
						if (this.enchantmentLimits.containsKey(enchantment)) {
							int max = this.enchantmentLimits.get(enchantment).intValue();
							if (level > max) {
								applied = true;
								enchantmentStorage.removeStoredEnchant(enchantment);
								if(max > 0){
									enchantmentStorage.addStoredEnchant(enchantment, max , true);
								}
							}
						}
					}
				}
				stack.setItemMeta(enchantmentStorage);
			}
			for(Enchantment enchantment : this.enchantmentLimits.keySet()){
				if(stack.containsEnchantment(enchantment)){
					int level = stack.getEnchantmentLevel(enchantment);
					if (this.enchantmentLimits.containsKey(enchantment)) {
						int max = this.enchantmentLimits.get(enchantment).intValue();
						if (level > max) {
							applied = true;
							stack.removeEnchantment(enchantment);
							if(max > 0){
								stack.addUnsafeEnchantment(enchantment, max);
							}
						}
					}
				}
			}

			return applied;
		}
	}
}
