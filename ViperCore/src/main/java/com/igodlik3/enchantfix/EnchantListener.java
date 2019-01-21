package com.igodlik3.enchantfix;

import org.bukkit.configuration.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;
import org.bukkit.event.enchantment.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.*;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import net.md_5.bungee.api.chat.*;

public class EnchantListener implements Listener
{
    private Configuration config;
    
    public EnchantListener() {
        this.config = (Configuration)EnchantFix.getInstance().getConfig();
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() != Material.AIR) {
            final ItemStack item = event.getItem();
            if (item.getEnchantments() != null && !item.getEnchantments().isEmpty()) {
                final Map<Enchantment, Integer> enchants = (Map<Enchantment, Integer>)item.getEnchantments();
                try {
                    for (final String enchant : this.config.getStringList("Enchant-Limiter")) {
                        final String[] parse = enchant.split(":");
                        final Enchantment enchantment = Enchantment.getByName(parse[0]);
                        final int level = Integer.parseInt(parse[1]);
                        if (enchants.containsKey(enchantment) && enchants.get(enchantment) > level) {
                            event.getPlayer().setItemInHand((ItemStack)null);
                            this.notifyAdmins(event.getPlayer());
                        }
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    @EventHandler
    public void onEnchantItem(final EnchantItemEvent e) {
        final Map<Enchantment, Integer> enchants = (Map<Enchantment, Integer>)e.getEnchantsToAdd();
        try {
            for (final String enchant : this.config.getStringList("Enchant-Limiter")) {
                final String[] parse = enchant.split(":");
                final Enchantment enchantment = Enchantment.getByName(parse[0]);
                final int level = Integer.parseInt(parse[1]);
                if (enchants.containsKey(enchantment) && enchants.get(enchantment) > level) {
                    enchants.remove(enchantment);
                    if (level <= 0) {
                        continue;
                    }
                    enchants.put(enchantment, level);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory().getType() == InventoryType.ANVIL && e.getSlotType() == InventoryType.SlotType.RESULT) {
            final ItemStack item = e.getCurrentItem();
            try {
                for (final String blockedEnchantments : this.config.getStringList("Enchant-Limiter")) {
                    final String[] parse = blockedEnchantments.split(":");
                    final Enchantment selectedEnchantment = Enchantment.getByName(parse[0]);
                    final int level = Integer.parseInt(parse[1]);
                    if (item.getType() == Material.ENCHANTED_BOOK) {
                        final EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta)item.getItemMeta();
                        if (bookMeta.getStoredEnchants().containsKey(selectedEnchantment) && bookMeta.getStoredEnchants().get(selectedEnchantment) > level) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                    if (item.getEnchantments().containsKey(selectedEnchantment) && item.getEnchantments().get(selectedEnchantment) > level) {
                        e.setCancelled(true);
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @EventHandler
    public void onPlayerFish(final PlayerFishEvent event) {
        final Entity entity = event.getCaught();
        if (entity != null && entity instanceof ItemStack) {
            final ItemStack item = (ItemStack)entity;
            if (item.getEnchantments() != null && !item.getEnchantments().isEmpty()) {
                final Map<Enchantment, Integer> enchants = (Map<Enchantment, Integer>)item.getEnchantments();
                try {
                    for (final String enchant : this.config.getStringList("Enchant-Limiter")) {
                        final String[] parse = enchant.split(":");
                        final Enchantment enchantment = Enchantment.getByName(parse[0]);
                        final int level = Integer.parseInt(parse[1]);
                        if (enchants.containsKey(enchantment) && enchants.get(enchantment) > level) {
                            item.removeEnchantment(enchantment);
                            if (level <= 0) {
                                continue;
                            }
                            item.addEnchantment(enchantment, level);
                        }
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        final List<ItemStack> items = (List<ItemStack>)event.getDrops();
        if (items != null) {
            for (final ItemStack item : items) {
                if (item != null && item.getType() != Material.AIR && item.getEnchantments() != null && !item.getEnchantments().isEmpty()) {
                    final Map<Enchantment, Integer> enchants = (Map<Enchantment, Integer>)item.getEnchantments();
                    try {
                        for (final String enchant : this.config.getStringList("Enchant-Limiter")) {
                            final String[] parse = enchant.split(":");
                            final Enchantment enchantment = Enchantment.getByName(parse[0]);
                            final int level = Integer.parseInt(parse[1]);
                            if (enchants.containsKey(enchantment) && enchants.get(enchantment) > level) {
                                item.removeEnchantment(enchantment);
                                if (level <= 0) {
                                    continue;
                                }
                                item.addEnchantment(enchantment, level);
                            }
                        }
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    
    private void notifyAdmins(final Player player) {
        final TextComponent text = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&8[&cALERT&8] &c" + player.getName() + " &ehas an illegal item! &6(&cClick here to TP&6)"));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getName()));
        for (Player pls : Bukkit.getOnlinePlayers()){
            if (pls.hasPermission("illegal.notify")) {
                pls.spigot().sendMessage((BaseComponent)text);
            }
        }
    }
}
