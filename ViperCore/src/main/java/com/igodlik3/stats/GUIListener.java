package com.igodlik3.stats;

import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;

public class GUIListener implements Listener
{
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Inventory inv = event.getInventory();
        if (inv != null && inv.getName() != null && inv.getName().contains("Stats")) {
            event.setCancelled(true);
        }
    }
}
