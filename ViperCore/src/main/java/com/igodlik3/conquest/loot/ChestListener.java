package com.igodlik3.conquest.loot;

import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.faction.bean.FactionPlayer;
import org.bukkit.configuration.*;
import com.igodlik3.conquest.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import com.igodlik3.conquest.utils.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.block.*;
import java.util.*;
import org.bukkit.event.*;

public class ChestListener implements Listener
{
    private Configuration config;
    private ChestLootManager manager;
    private LootManager lm;
    private ChestKey key;
    
    public ChestListener() {
        this.config = (Configuration)Conquest.getInstance().getConfig();
        this.manager = new ChestLootManager();
        this.lm = Conquest.getInstance().getLootManager();
        this.key = new ChestKey();
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final Block block = event.getClickedBlock();
            if (this.manager.isChestLoot(block.getLocation())) {
                event.setCancelled(true);
                final ItemStack item = event.getItem();
                if (this.key.isKey(item)) {
                    final int lootNumber = this.config.getInt("CONQUEST.Loot-Number");
                    if (this.lm.getLootList().isEmpty()) {
                        player.sendMessage(Utils.color("&4&lLoot not found ! &cPlease fill the chest loot with items &e/conquest editloot"));
                        return;
                    }
                    final List<ItemStack> set = new ArrayList<ItemStack>(this.lm.getLootList());
                    if (set.size() < lootNumber) {
                        player.sendMessage(Utils.color("&cPlease fill the chest loot with items. &eLoot size: " + set.size() + " &7- &eLoot needed: " + lootNumber));
                        return;
                    }
                    final Random r = new Random();
                    final FactionPlayer fp = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
                    Bukkit.broadcastMessage(Utils.color(this.config.getString("Messages.LOOT")).replaceAll("%PLAYER%", player.getName()).replaceAll("%FACTION%", fp.getFaction().getName()));
                    for (int i = 0; i < lootNumber; ++i) {
                        final ItemStack is = set.get(r.nextInt(set.size()));
                        player.getInventory().addItem(new ItemStack[] { is });
                        Bukkit.broadcastMessage(Utils.color("&7- &e" + this.stringifyItem(is)));
                    }
                    player.updateInventory();
                    this.key.removeKey(player);
                }
                else {
                    player.sendMessage(Utils.color(this.config.getString("Messages.CHEST-LOOT-NO-KEY")));
                }
            }
        }
    }
    
    private String stringifyItem(final ItemStack item) {
        String itemName;
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            itemName = item.getItemMeta().getDisplayName();
        }
        else {
            final String matName = item.getType().toString().replace("_", " ").toLowerCase();
            itemName = String.valueOf(Character.toUpperCase(matName.charAt(0))) + matName.substring(1);
        }
        if (item.getAmount() > 1) {
            return String.valueOf(item.getAmount()) + "x " + itemName;
        }
        return itemName;
    }
}
