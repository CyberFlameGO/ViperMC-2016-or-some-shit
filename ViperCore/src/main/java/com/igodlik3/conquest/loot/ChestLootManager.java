package com.igodlik3.conquest.loot;

import org.bukkit.configuration.*;
import com.igodlik3.conquest.*;
import org.bukkit.*;
import com.igodlik3.conquest.utils.*;
import java.util.*;

public class ChestLootManager
{
    private Configuration storage;
    
    public ChestLootManager() {
        this.storage = (Configuration)Conquest.getInstance().getStorage().getConfig();
    }
    
    public boolean isChestLoot(final Location loc) {
        final List<Location> list = this.getLocations();
        if (list.isEmpty()) {
            return false;
        }
        for (final Location l : list) {
            if (l != null && l.getBlockX() == loc.getBlockX() && l.getBlockZ() == loc.getBlockZ() && l.getBlockY() == loc.getBlockY()) {
                return true;
            }
        }
        return false;
    }
    
    public List<Location> getLocations() {
        final List<Location> list = new ArrayList<Location>();
        if (!this.storage.contains("chestloot.location")) {
            return list;
        }
        for (final String stg : this.storage.getStringList("chestloot.location")) {
            list.add(Utils.destringifyLocation(stg));
        }
        return list;
    }
    
    public void removeLocation(final Location loc) {
        final String string = Utils.stringifyLocation(loc);
        final List<String> strings = new ArrayList<String>(this.storage.getStringList("chestloot.location"));
        for (final String stg : this.storage.getStringList("chestloot.location")) {
            if (stg.equals(string)) {
                strings.remove(string);
            }
        }
        this.storage.set("chestloot.location", (Object)strings);
        Conquest.getInstance().getStorage().saveConfig();
    }
    
    public void addLocation(final Location location) {
        final String string = Utils.stringifyLocation(location);
        final List<String> strings = new ArrayList<String>(this.storage.getStringList("chestloot.location"));
        if (strings.contains(string)) {
            return;
        }
        strings.add(string);
        this.storage.set("chestloot.location", (Object)strings);
        Conquest.getInstance().getStorage().saveConfig();
    }
}
