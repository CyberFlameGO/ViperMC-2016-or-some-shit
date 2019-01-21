package com.igodlik3.conquest.loot;

import com.igodlik3.ItemBuilder;
import org.bukkit.configuration.*;
import com.igodlik3.conquest.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import com.igodlik3.conquest.utils.*;
import java.text.*;
import java.util.*;

public class ChestKey
{
    private Configuration config;
    
    public ChestKey() {
        this.config = (Configuration)Conquest.getInstance().getConfig();
    }
    
    public void removeKey(final Player player) {
        final int amount = player.getItemInHand().getAmount();
        if (amount == 1) {
            player.setItemInHand((ItemStack)null);
        }
        else {
            player.getItemInHand().setAmount(amount - 1);
        }
    }
    
    public boolean isKey(final ItemStack item) {
        if (item != null && item.getType() == Material.TRIPWIRE_HOOK && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            final String display = item.getItemMeta().getDisplayName();
            final String d = this.config.getString("CONQUEST.Loot.Key.name");
            return display.equalsIgnoreCase(Utils.color(d));
        }
        return false;
    }
    
    public void giveKey(final Player player) {
        ItemStack key = null;
        final List<String> lore = (List<String>)this.config.getStringList("CONQUEST.Loot.Key.lore");
        final List<String> formatted = new ArrayList<String>();
        final String display = this.config.getString("CONQUEST.Loot.Key.name");
        final DateFormat dateFormat = new SimpleDateFormat(this.config.getString("Messages.Date-Format"));
        final Date date = new Date();
        for (final String stg : lore) {
            formatted.add(Utils.color(stg).replaceAll("%DATE%", dateFormat.format(date)).replaceAll("%PLAYER%", player.getName()));
        }
        key = new ItemBuilder(Material.TRIPWIRE_HOOK).displayname(display).lore(formatted).build();
        player.getInventory().addItem(new ItemStack[] { key });
    }
}
