package com.cloth.viperkits.events;

import com.cloth.viperkits.ViperKits;
import com.cloth.viperkits.config.ConfigurationSetup;
import com.cloth.viperkits.util.RewardsManager;
import com.cloth.viperkits.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import sun.security.krb5.Config;

import java.util.ArrayList;

/**
 * Created by Brennan on 6/22/2017.
 */
public class ClickEvent implements Listener
{
    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        String title = ConfigurationSetup.gui.getString("Title").replaceAll("&", "§");

        if(e.getInventory().getTitle().endsWith("Kit")){
            e.setCancelled(true);
            return;
        }

        if(e.getInventory().getTitle().equalsIgnoreCase(title)){

            final Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);

            if(!Util.kitExists(Util.getKitFromSlot(e.getSlot()))){
                return;
            }

            final String kit = Util.getKitFromSlot(e.getSlot());
            ItemStack[] armor = RewardsManager.getArmor(kit).toArray(new ItemStack[RewardsManager.getArmor(kit).size()]);
            ItemStack[] items = RewardsManager.getItems(kit).toArray(new ItemStack[RewardsManager.getItems(kit).size()]);

            if(e.getClick() == ClickType.RIGHT)
            {
                Inventory inv = Bukkit.createInventory(null, 45, kit + " Kit");

                for(int i = 3; i >= 0; i--)
                {
                    try{
                        if(armor[i] != null){
                            inv.addItem(armor[i]);
                        }
                    } catch (Exception error){}
                }

                for(ItemStack item : items){
                    inv.addItem(item);
                }

                p.openInventory(inv);
                return;
            }

            if(e.getClick() == ClickType.LEFT)
            {
                if(Util.isKitAvailable(p, kit))
                {
                    if(ConfigurationSetup.playerdata.getInt(p.getName().toLowerCase() + "." + kit.toLowerCase() + "_cooldown") > 0){
                        String msg = ConfigurationSetup.messages.getString("on_cooldown").replaceAll("&", "§");
                        p.sendMessage(msg.replace("%duration%", Util.cooldownToString(ConfigurationSetup.playerdata.getInt(p.getName().toLowerCase() + "." + kit.toLowerCase() + "_cooldown"))));
                        p.closeInventory();
                        return;
                    }

                    String msg = ConfigurationSetup.messages.getString("redeemed_kit").replaceAll("&", "§");
                    p.sendMessage(msg.replace("%kit%", kit));

                    final int cooldown = ConfigurationSetup.kits.getInt(kit + ".Cooldown");

                    new BukkitRunnable()
                    {
                        public void run()
                        {
                            ConfigurationSetup.playerdata.set(p.getName().toLowerCase() + "." + kit.toLowerCase() + "_cooldown", cooldown);
                            ConfigurationSetup.playerdata.saveConfig();
                        }
                    }.runTaskAsynchronously(ViperKits.getInstance());

                    p.getInventory().setArmorContents(armor);

                    for(ItemStack item : items)
                    {
                        if(item != null)
                        {
                            p.getInventory().addItem(item);
                        }
                    }

                    p.closeInventory();
                    p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1,1);
                    return;
                } else {
                    String msg = ConfigurationSetup.messages.getString("unavailable_kit").replaceAll("&", "§");
                    p.sendMessage(msg);
                    p.closeInventory();
                    return;
                }
            }
        }
    }
}
