package me.dreamzy.report.reports;

import me.dreamzy.report.*;
import net.syuu.popura.faction.bean.Faction;

import java.util.*;

public class ReportData
{
    private Faction faction;
    private Map<String, Integer> reportedPlayers;
    private final int MAX_REPORT_PER_FACTION;
    
    public ReportData(final Faction faction) {
        this.MAX_REPORT_PER_FACTION = ViperReport.getInstance().getConfig().getInt("MAX-REPORT-PER-FACTION");
        this.faction = faction;
        this.reportedPlayers = new HashMap<String, Integer>();
    }
    
    public boolean reportPlayer(final String name) {
        if (this.reportedPlayers.containsKey(name)) {
            final int reportTime = this.reportedPlayers.put(name, this.reportedPlayers.get(name) + 1);
            return reportTime < this.MAX_REPORT_PER_FACTION;
        }
        this.reportedPlayers.put(name, 1);
        return true;
    }
    
    public Faction getFaction() {
        return this.faction;
    }
    
    public Map<String, Integer> getReportedPlayers() {
        return this.reportedPlayers;
    }
    
    public int getMAX_REPORT_PER_FACTION() {
        return this.MAX_REPORT_PER_FACTION;
    }
    
    public void setFaction(final Faction faction) {
        this.faction = faction;
    }
    
    public void setReportedPlayers(final Map<String, Integer> reportedPlayers) {
        this.reportedPlayers = reportedPlayers;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReportData)) {
            return false;
        }
        final ReportData other = (ReportData)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$faction = this.getFaction();
        final Object other$faction = other.getFaction();
        Label_0065: {
            if (this$faction == null) {
                if (other$faction == null) {
                    break Label_0065;
                }
            }
            else if (this$faction.equals(other$faction)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$reportedPlayers = this.getReportedPlayers();
        final Object other$reportedPlayers = other.getReportedPlayers();
        if (this$reportedPlayers == null) {
            if (other$reportedPlayers == null) {
                return this.getMAX_REPORT_PER_FACTION() == other.getMAX_REPORT_PER_FACTION();
            }
        }
        else if (this$reportedPlayers.equals(other$reportedPlayers)) {
            return this.getMAX_REPORT_PER_FACTION() == other.getMAX_REPORT_PER_FACTION();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReportData;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $faction = this.getFaction();
        result = result * 59 + (($faction == null) ? 43 : $faction.hashCode());
        final Object $reportedPlayers = this.getReportedPlayers();
        result = result * 59 + (($reportedPlayers == null) ? 43 : $reportedPlayers.hashCode());
        result = result * 59 + this.getMAX_REPORT_PER_FACTION();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReportData(faction=" + this.getFaction() + ", reportedPlayers=" + this.getReportedPlayers() + ", MAX_REPORT_PER_FACTION=" + this.getMAX_REPORT_PER_FACTION() + ")";
    }
}
