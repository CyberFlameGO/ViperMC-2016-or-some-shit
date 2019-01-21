package me.dreamzy.report.reports;

import me.dreamzy.report.*;
import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.faction.bean.FactionPlayer;
import org.bukkit.configuration.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import me.dreamzy.report.utils.*;
import org.bukkit.*;

public class CmdReport implements CommandExecutor
{
    private ViperReport plugin;
    private Configuration config;
    private ReportManager reportManager;
    
    public CmdReport() {
        this.plugin = ViperReport.getInstance();
        this.config = (Configuration)ViperReport.getInstance().getConfig();
        this.reportManager = this.plugin.getReportManager();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.color(this.config.getString("Messages.NO-PERM")));
            return true;
        }
        final Player player = (Player)sender;
        if (args.length == 0) {
            this.sendHelp(player);
        }
        else {
            if (CooldownUtils.isOnCooldown("Report", player)) {
                sender.sendMessage(Utils.color(this.config.getString("Messages.REPORT-COOLDOWN")).replace("%TIME%", String.valueOf(CooldownUtils.getCooldownForPlayerInt("Report", player))));
                return true;
            }
            final Player cheater = Bukkit.getPlayer(args[0]);
            if (cheater != null) {

                final FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
                if (player == cheater) {
                    player.sendMessage(Utils.color(this.config.getString("Messages.SELF-REPORT")));
                    return true;
                }
                if (factionPlayer.getFaction() != null) {
                    final ReportData data = this.reportManager.getReportData(factionPlayer.getFaction());
                    if (!data.reportPlayer(cheater.getName())) {
                        player.sendMessage(Utils.color(this.config.getString("Messages.MAX-REPORT-FACTION")).replace("%REPORT_COUNT%", String.valueOf(data.getMAX_REPORT_PER_FACTION())));
                        return true;
                    }
                }
                if (this.reportManager.getReport(player) != null) {
                    this.reportManager.getReports().remove(this.reportManager.getReport(player));
                }
                final Report report = new Report(player, args[0]);
                this.reportManager.getReports().add(report);
                this.reportManager.openReportInventory(player);
            }
            else {
                player.sendMessage(Utils.color(this.config.getString("Messages.REPORT-USAGE")));
            }
        }
        return false;
    }
    
    public void sendHelp(final Player player) {
        for (final String msg : this.config.getStringList("Messages.HELP-CMD")) {
            player.sendMessage(Utils.color(msg));
        }
    }
}
