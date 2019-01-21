package com.igodlik3.viperutils;

import org.bukkit.configuration.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;

public class BoostXPListener implements Listener
{
    private Configuration config;
    
    public BoostXPListener() {
        this.config = (Configuration)ViperUtils.getInstance().getConfig();
    }
    
    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null && event.getEntity().getKiller() instanceof Player) {
            final Player player = event.getEntity().getKiller();
            if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
                final ItemStack item = player.getItemInHand();
                if (item.containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) {
                    event.setDroppedExp(event.getDroppedExp() * (item.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) + this.config.getInt("XPBonus-Multiplier")));
                }
            }
        }
    }
}
