package me.dreamzy.report.stats;

import org.bukkit.entity.*;
import org.bukkit.*;

public class StatsData
{
    private int totalDiamond;
    private int totalIron;
    private int totalGold;
    private int totalEmerald;
    private int totalCoal;
    
    public StatsData(final Player player) {
        this.saveStats(player);
    }
    
    public StatsData(final int d, final int i, final int g, final int e, final int c) {
        this.totalDiamond = d;
        this.totalIron = i;
        this.totalGold = g;
        this.totalEmerald = e;
        this.totalCoal = c;
    }
    
    public void saveStats(final Player player) {
        this.totalDiamond = player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE);
        this.totalIron = player.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE);
        this.totalGold = player.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE);
        this.totalEmerald = player.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE);
        this.totalCoal = player.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE);
    }
    
    public int getTotalDiamond() {
        return this.totalDiamond;
    }
    
    public int getTotalIron() {
        return this.totalIron;
    }
    
    public int getTotalGold() {
        return this.totalGold;
    }
    
    public int getTotalEmerald() {
        return this.totalEmerald;
    }
    
    public int getTotalCoal() {
        return this.totalCoal;
    }
    
    public void setTotalDiamond(final int totalDiamond) {
        this.totalDiamond = totalDiamond;
    }
    
    public void setTotalIron(final int totalIron) {
        this.totalIron = totalIron;
    }
    
    public void setTotalGold(final int totalGold) {
        this.totalGold = totalGold;
    }
    
    public void setTotalEmerald(final int totalEmerald) {
        this.totalEmerald = totalEmerald;
    }
    
    public void setTotalCoal(final int totalCoal) {
        this.totalCoal = totalCoal;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StatsData)) {
            return false;
        }
        final StatsData other = (StatsData)o;
        return other.canEqual(this) && this.getTotalDiamond() == other.getTotalDiamond() && this.getTotalIron() == other.getTotalIron() && this.getTotalGold() == other.getTotalGold() && this.getTotalEmerald() == other.getTotalEmerald() && this.getTotalCoal() == other.getTotalCoal();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof StatsData;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getTotalDiamond();
        result = result * 59 + this.getTotalIron();
        result = result * 59 + this.getTotalGold();
        result = result * 59 + this.getTotalEmerald();
        result = result * 59 + this.getTotalCoal();
        return result;
    }
    
    @Override
    public String toString() {
        return "StatsData(totalDiamond=" + this.getTotalDiamond() + ", totalIron=" + this.getTotalIron() + ", totalGold=" + this.getTotalGold() + ", totalEmerald=" + this.getTotalEmerald() + ", totalCoal=" + this.getTotalCoal() + ")";
    }
}
