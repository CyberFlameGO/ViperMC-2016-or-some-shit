package me.dreamzy.report.reports;

import com.igodlik3.ItemBuilder;
import me.dreamzy.report.*;
import org.bukkit.event.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import me.dreamzy.report.utils.*;

public class ReportListener implements Listener
{
    private ViperReport plugin;
    private ReportManager reportManager;
    
    public ReportListener() {
        this.plugin = ViperReport.getInstance();
        this.reportManager = this.plugin.getReportManager();
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player)event.getWhoClicked();
        final Inventory inv = event.getInventory();
        if (inv != null && inv.getName() != null && player.getOpenInventory().getTopInventory() == inv && event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
            final ItemStack item = event.getCurrentItem();
            if (inv.getName().equalsIgnoreCase(Utils.color(this.plugin.getPrefixReportGui()))) {
                event.setCancelled(true);
                if (item.getType() == Material.DIAMOND_SWORD) {
                    this.openCheatInventory(player);
                }
                else {
                    this.openChatInventory(player);
                }
            }
            else if (inv.getName().contains(Utils.color(this.plugin.getPrefixReportChatGui())) || inv.getName().contains(Utils.color(this.plugin.getPrefixReportCheatGui()))) {
                event.setCancelled(true);
                final Report report = this.reportManager.getReport(player);
                if (report == null) {
                    player.closeInventory();
                    return;
                }
                report.setReason(item.getItemMeta().getDisplayName());
                report.send();
                player.closeInventory();
            }
        }
    }
    
    public void openChatInventory(final Player player) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)player, 27, Utils.color(this.plugin.getPrefixReportChatGui()));
        inv.setItem(10, new ItemBuilder(Material.DIAMOND_SWORD).displayname(Utils.color("&6Spamming Chat")).build());
        inv.setItem(12, new ItemBuilder(Material.GOLD_SWORD).displayname(Utils.color("&5Spamming Msg")).build());
        inv.setItem(14, new ItemBuilder(Material.POTION).displayname(Utils.color("&3Spamming Faction Title")).build());
        inv.setItem(16, new ItemBuilder(Material.FEATHER).displayname(Utils.color("&cCAPS")).build());
        inv.setItem(22, new ItemBuilder(Material.BOW).displayname(Utils.color("&dOthers")).build());
        player.openInventory(inv);
    }
    
    public void openCheatInventory(final Player player) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)player, 27, Utils.color(this.plugin.getPrefixReportCheatGui()));
        inv.setItem(10, new ItemBuilder(Material.DIAMOND_SWORD).displayname(Utils.color("&6ForceField")).build());
        inv.setItem(12, new ItemBuilder(Material.GOLD_SWORD).displayname(Utils.color("&5KillAura")).build());
        inv.setItem(14, new ItemBuilder(Material.POTION).displayname(Utils.color("&3SpeedHack")).build());
        inv.setItem(16, new ItemBuilder(Material.FEATHER).displayname(Utils.color("&eFly")).build());
        inv.setItem(22, new ItemBuilder(Material.BOW).displayname(Utils.color("&dOthers")).build());
        player.openInventory(inv);
    }
}
