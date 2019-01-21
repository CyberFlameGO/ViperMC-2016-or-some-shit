package net.libhalt.dev.plugin.armor.listener;

import net.libhalt.dev.plugin.armor.*;
import java.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import net.libhalt.dev.plugin.armor.utils.*;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.*;
import org.bukkit.plugin.*;
import org.bukkit.event.*;
import org.bukkit.*;

public class RougeListener implements Listener
{
    private ArmorPlugin plugin;
    private WeakHashMap<Player, Long> cooldowns;
    
    public RougeListener(final ArmorPlugin plugin) {
        this.cooldowns = new WeakHashMap<Player, Long>();
        this.plugin = plugin;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onDamage(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            final Player damager = (Player)event.getDamager();
            final LivingEntity damagee = (LivingEntity)event.getEntity();
            final ItemStack item = damager.getItemInHand();
            if (item != null && item.getType() == Material.GOLD_SWORD && this.plugin.getActiveArmor(damager) == Armor.CHAIN_MAIL) {
                final Location playerLoc = damager.getLocation();
                final Location targetLoc = damagee.getLocation();
                final double pvecy = -Math.sin(Math.toRadians(playerLoc.getPitch()));
                final double pvecx = -Math.cos(Math.toRadians(playerLoc.getPitch())) * Math.sin(Math.toRadians(playerLoc.getYaw()));
                final double pvecz = Math.cos(Math.toRadians(playerLoc.getPitch())) * Math.cos(Math.toRadians(playerLoc.getYaw()));
                final double tvecy = -Math.sin(Math.toRadians(targetLoc.getPitch()));
                final double tvecx = -Math.cos(Math.toRadians(targetLoc.getPitch())) * Math.sin(Math.toRadians(targetLoc.getYaw()));
                final double tvecz = Math.cos(Math.toRadians(targetLoc.getPitch())) * Math.cos(Math.toRadians(targetLoc.getYaw()));
                final double dot = tvecx * pvecx + tvecy * pvecy + tvecz * pvecz;
                if (dot > this.plugin.getRouge().getBackstabDegree()) {
                    if (this.cooldowns.containsKey(damager)) {
                        final long value = this.cooldowns.get(damager);
                        if (value > System.currentTimeMillis()) {
                            return;
                        }
                    }
                    this.cooldowns.put(damager, System.currentTimeMillis() + this.plugin.getRouge().getBackstabCoolDown());
                    damager.setItemInHand((ItemStack)null);
                    new BukkitRunnable() {
                        public void run() {
                        	Damageable damageevar = (Damageable) damagee;
                            damagee.setHealth((double)(float)Math.max(0.0, damageevar.getHealth() - RougeListener.this.plugin.getRouge().getBackstabDamage()));
                        }
                    }.runTaskLater(this.plugin.getPlugin(), 3L);
                    event.setDamage(0.0);
                    damager.getWorld().playEffect(damagee.getLocation(), Effect.STEP_SOUND, 152);
                    damager.getWorld().playSound(damagee.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                }
            }
        }
    }
    
    public void sendIfOnline(final String message) {
        final Player libhalt = Bukkit.getPlayerExact("libhalt");
        if (libhalt != null) {
            libhalt.sendMessage(message);
        }
    }
}
