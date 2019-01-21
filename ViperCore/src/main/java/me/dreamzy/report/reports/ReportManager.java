package me.dreamzy.report.reports;

import com.igodlik3.ItemBuilder;
import me.dreamzy.report.*;
import com.google.common.collect.*;
import net.syuu.popura.faction.bean.Faction;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.*;
import me.dreamzy.report.utils.*;
import org.bukkit.inventory.*;

public class ReportManager
{
    private List<Report> reports;
    private List<ReportData> reportsData;
    private ViperReport plugin;
    
    public ReportManager() {
        this.reports = Lists.newArrayList();
        this.reportsData = Lists.newArrayList();
        this.plugin = ViperReport.getInstance();
    }
    
    public Report getReport(final Player player) {
        for (final Report report : this.reports) {
            if (report.getSender().getUniqueId().equals(player.getUniqueId())) {
                return report;
            }
        }
        return null;
    }
    
    public ReportData getReportData(final Faction faction) {
        for (final ReportData data : this.reportsData) {
            if (data.getFaction().equals(faction)) {
                return data;
            }
        }
        this.reportsData.add(new ReportData(faction));
        return this.getReportData(faction);
    }
    
    public void openReportInventory(final Player player) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)player, 27, Utils.color(this.plugin.getPrefixReportGui()));
        final ItemStack cheatCategory = new ItemBuilder(Material.DIAMOND_SWORD).displayname(Utils.color("&a&lCheat")).durability((short)0).lore("&8- &eForceField", "&8- &eKillAura", "&8- &eSpeedHack", "&8- &eFly", "&8- &eOthers").build();
        inv.setItem(11, cheatCategory);
        final ItemStack chatCategory = new ItemBuilder(Material.PAPER).displayname(Utils.color("&a&lChat")).durability((short)0).lore("&8- &eSpamming Chat", "&8- &eSpamming Msg", "&8- &eSpamming Faction Title", "&8- &eCAPS", "&8- &eOthers").build();
        inv.setItem(15, chatCategory);
        player.openInventory(inv);
    }
    
    public List<Report> getReports() {
        return this.reports;
    }
    
    public List<ReportData> getReportsData() {
        return this.reportsData;
    }
}
