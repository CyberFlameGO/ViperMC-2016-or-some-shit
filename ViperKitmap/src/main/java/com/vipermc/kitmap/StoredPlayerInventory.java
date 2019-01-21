package com.vipermc.kitmap;

import org.bukkit.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.configuration.file.*;

public class StoredPlayerInventory
{
    private ItemStack[] content;
    private ItemStack[] armorcontent;
    
    public StoredPlayerInventory(final Player player) {
        this.content = this.deepCopy(player.getInventory().getContents());
        this.armorcontent = this.deepCopy(player.getInventory().getArmorContents());
    }
    
    public StoredPlayerInventory(final YamlConfiguration yaml) {
        final int contentsize = yaml.getInt("content-size");
        final int armorcontentsize = yaml.getInt("armor-content-size");
        this.content = new ItemStack[contentsize];
        this.armorcontent = new ItemStack[armorcontentsize];
        for (int i = 0; i < contentsize; ++i) {
            this.content[i] = yaml.getItemStack("content-" + i);
        }
        for (int i = 0; i < armorcontentsize; ++i) {
            this.armorcontent[i] = yaml.getItemStack("armor-content-" + i);
        }
    }
    
    public void paste(final Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(this.deepCopy(this.armorcontent));
        player.getInventory().setContents(this.deepCopy(this.content));
        player.updateInventory();
    }
    
    public void paste(final YamlConfiguration yaml) {
        yaml.set("content-size", this.content.length);
        for (int i = 0; i < this.content.length; ++i) {
            yaml.set("content-" + i, this.content[i]);
        }
        yaml.set("armor-content-size", this.armorcontent.length);
        for (int i = 0; i < this.armorcontent.length; ++i) {
            yaml.set("armor-content-" + i, this.armorcontent[i]);
        }
    }
    
    private ItemStack[] deepCopy(final ItemStack[] item) {
        final ItemStack[] copy = new ItemStack[item.length];
        for (int i = 0; i < item.length; ++i) {
            final ItemStack from = item[i];
            if (from != null) {
                copy[i] = from.clone();
            }
        }
        return copy;
    }
}
