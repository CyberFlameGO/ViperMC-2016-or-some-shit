package me.dreamzy.report.stats;

import me.dreamzy.report.utils.*;
import me.dreamzy.report.*;
import org.bukkit.plugin.java.*;
import org.bukkit.entity.*;
import org.bukkit.configuration.*;
import java.util.*;

public class StatsManager
{
    private Map<String, StatsData> statsMap;
    private ConfigCreator config;
    
    public StatsManager() {
        this.statsMap = new HashMap<String, StatsData>();
        this.config = new ConfigCreator("data", ViperReport.getInstance().getPlugin());
    }
    
    public StatsData getUser(final Player player) {
        final String id = player.getUniqueId().toString();
        if (this.statsMap.containsKey(id)) {
            return this.statsMap.get(id);
        }
        final StatsData data = new StatsData(player);
        this.statsMap.put(id, data);
        return data;
    }
    
    public StatsData getUser(final String id) {
        return this.statsMap.get(id);
    }
    
    public void reloadData() {
        final ConfigurationSection section = this.config.getConfigurationSection("data");
        if (section != null) {
            for (final String id : section.getKeys(false)) {
                final String path = String.valueOf(id) + ".";
                this.statsMap.put(id, new StatsData(section.getInt(String.valueOf(path) + "totalDiamond"), section.getInt(String.valueOf(path) + "totalIron"), section.getInt(String.valueOf(path) + "totalGold"), section.getInt(String.valueOf(path) + "totalEmerald"), section.getInt(String.valueOf(path) + "totalCoal")));
            }
        }
    }
    
    public void saveData() {
        final ConfigurationSection section = this.config.createSection("data");
        for (final String id : this.statsMap.keySet()) {
            final StatsData data = this.statsMap.get(id);
            section.set(String.valueOf(id) + ".totalDiamond", (Object)data.getTotalDiamond());
            section.set(String.valueOf(id) + ".totalIron", (Object)data.getTotalIron());
            section.set(String.valueOf(id) + ".totalGold", (Object)data.getTotalGold());
            section.set(String.valueOf(id) + ".totalEmerald", (Object)data.getTotalEmerald());
            section.set(String.valueOf(id) + ".totalCoal", (Object)data.getTotalCoal());
        }
        this.config.save();
    }
}
