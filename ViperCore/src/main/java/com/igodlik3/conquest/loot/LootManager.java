package com.igodlik3.conquest.loot;

import org.bukkit.configuration.*;
import com.igodlik3.conquest.*;
import org.bukkit.plugin.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.entity.*;
import com.igodlik3.conquest.utils.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;

public class LootManager implements Listener
{
    private Configuration storage;
    private Set<UUID> lootEditors;
    private Inventory inv;
    private List<ItemStack> lootList;
    
    public LootManager() {
        this.storage = (Configuration)Conquest.getInstance().getStorage().getConfig();
        this.lootEditors = new HashSet<UUID>();
        this.lootList = new ArrayList<ItemStack>();
        this.reloadLoot();
        Bukkit.getPluginManager().registerEvents((Listener)this, Conquest.getInstance().getPlugin());
    }
    
    public void loadLoot() {
        final Object value = this.storage.get("Loot");
        if (value != null) {
            ItemStack[] contents = null;
            if (value instanceof ItemStack[]) {
                contents = (ItemStack[])value;
            }
            else if (value instanceof List) {
                contents = (ItemStack[]) ((List)value).toArray(new ItemStack[0]);
            }
            this.inv.setContents(contents);
            ItemStack[] array;
            for (int length = (array = contents).length, i = 0; i < length; ++i) {
                final ItemStack is = array[i];
                if (is != null && is.getType() != Material.AIR) {
                    this.lootList.add(is);
                }
            }
        }
    }
    
    public void saveLoot() {
        this.storage.set("Loot", (Object)this.inv.getContents());
        Conquest.getInstance().getStorage().saveConfig();
        this.lootList.clear();
        ItemStack[] contents;
        for (int length = (contents = this.inv.getContents()).length, i = 0; i < length; ++i) {
            final ItemStack is = contents[i];
            if (is != null && is.getType() != Material.AIR) {
                this.lootList.add(is);
            }
        }
    }
    
    public void reloadLoot() {
        final int size = 36;
        this.inv = Bukkit.createInventory((InventoryHolder)null, size, "Conquest Loot");
        this.loadLoot();
    }
    
    public ItemStack getRandomLoot() {
        final Random r = new Random();
        return this.lootList.get(r.nextInt(this.lootList.size()));
    }
    
    public void openLoot(final Player player, final boolean access) {
        player.openInventory(this.inv);
        if (access) {
            this.lootEditors.add(player.getUniqueId());
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClose(final InventoryCloseEvent event) {
        final Player player = (Player)event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final Inventory inv = event.getInventory();
        boolean eventInv = false;
        if (inv != null && inv.getName() != null) {
            if (inv.getName().equalsIgnoreCase("Conquest Loot")) {
                eventInv = true;
            }
            if (eventInv) {
                final String name = inv.getName();
                if (this.lootEditors.contains(uuid)) {
                    this.saveLoot();
                    player.sendMessage(Utils.color("&aSaving loot for &d" + name + " &a!"));
                    this.lootEditors.remove(uuid);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player)event.getWhoClicked();
        final UUID uuid = player.getUniqueId();
        final Inventory inv = event.getInventory();
        boolean eventInv = false;
        if (inv != null && inv.getName() != null) {
            if (inv.getName().equalsIgnoreCase("Conquest Loot")) {
                eventInv = true;
            }
            if (eventInv && !this.lootEditors.contains(uuid)) {
                event.setCancelled(true);
            }
        }
    }
    
    public Inventory getInv() {
        return this.inv;
    }
    
    public List<ItemStack> getLootList() {
        return this.lootList;
    }
}
