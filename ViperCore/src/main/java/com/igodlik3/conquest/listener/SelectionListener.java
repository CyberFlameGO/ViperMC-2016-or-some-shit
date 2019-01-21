package com.igodlik3.conquest.listener;

import com.igodlik3.ItemBuilder;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;
import java.util.*;
import com.igodlik3.conquest.utils.*;

public class SelectionListener implements Listener
{
    private static Map<Player, Map<String, Location>> selectionMap;
    
    static {
        SelectionListener.selectionMap = new HashMap<Player, Map<String, Location>>();
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getClickedBlock();
        final Map<String, Location> map = SelectionListener.selectionMap.containsKey(player) ? SelectionListener.selectionMap.get(player) : new HashMap<String, Location>();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null && event.getItem().getType() == Material.GOLD_HOE) {
                final ItemStack item = event.getItem();
                if (this.isWand(item)) {
                    event.setCancelled(true);
                    map.put("1", block.getLocation());
                    SelectionListener.selectionMap.put(player, map);
                    player.sendMessage(Utils.color("&dSelected the 2# point !"));
                }
            }
        }
        else if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getItem() != null && event.getItem().getType() == Material.GOLD_HOE) {
            final ItemStack item = event.getItem();
            if (this.isWand(item)) {
                event.setCancelled(true);
                map.put("2", block.getLocation());
                SelectionListener.selectionMap.put(player, map);
                player.sendMessage(Utils.color("&dSelected the 1# point !"));
            }
        }
    }
    
    public static boolean isCorrectSelection(final Player player) {
        return SelectionListener.selectionMap.containsKey(player) && SelectionListener.selectionMap.get(player).size() == 2;
    }
    
    public static List<Location> getSelection(final Player player) {
        final List<Location> result = new ArrayList<Location>();
        if (!isCorrectSelection(player)) {
            return null;
        }
        final Location loc1 = SelectionListener.selectionMap.get(player).get("1");
        final Location loc2 = SelectionListener.selectionMap.get(player).get("2");
        result.add(loc1);
        result.add(loc2);
        return result;
    }
    
    private boolean isWand(final ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.color("&e&lRegion &6Wand"));
    }
    
    public static void getWand(final Player player) {
        final ItemStack wand = new ItemBuilder(Material.GOLD_HOE).displayname("&e&lRegion &6Wand").build();
        player.getInventory().addItem(new ItemStack[] { wand });
        player.updateInventory();
    }
}
