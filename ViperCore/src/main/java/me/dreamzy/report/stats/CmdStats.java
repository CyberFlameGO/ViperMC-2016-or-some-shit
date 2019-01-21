package me.dreamzy.report.stats;

import net.syuu.common.command.Command;
import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.command.condition.MustBeInFaction;
import net.syuu.popura.faction.FactionDataManager;
import net.syuu.popura.faction.FactionType;
import net.syuu.popura.faction.bean.Faction;
import net.syuu.popura.faction.bean.FactionPlayer;
import org.bukkit.configuration.*;
import me.dreamzy.report.*;
import me.dreamzy.report.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class CmdStats extends Command
{
    private StatsManager statsManager;
    private Configuration config;
    
    public CmdStats() {
        super("stat");
        this.statsManager = ViperReport.getInstance().getStatsManager();
        this.config = (Configuration)ViperReport.getInstance().getConfig();
        addAllias("stats");
        addCondition(new MustBeInFaction());
    }

    public void proccess(final Player player, final String[] args) {
            final FactionDataManager factionDataManager = ((PopuraPlugin)PopuraPlugin.getPlugin(PopuraPlugin.class)).getPopura().getFactionDataManager();
            Faction faction = factionDataManager.getPlayer(player).getFaction();
        if(args.length > 0){
            faction = factionDataManager.getFaction(args[0]);

        }
        if (faction == null) {
            return;
        }
        if (faction.getFactionType() != FactionType.NORMAL) {
            player.sendMessage(Utils.color("&cInvalid faction."));
            return;
        }
        int totalDiamond = 0;
        int totalIron = 0;
        int totalGold = 0;
        int totalEmerald = 0;
        int totalCoal = 0;
        int totalKills = 0;
        int totalDeaths = 0;
        for (final FactionPlayer fp : faction.getPlayers()) {
            totalKills += fp.getKill();
            totalDeaths = fp.getDeath();
            Player bukkitPlayer = Bukkit.getPlayer(fp.getName());
            if (bukkitPlayer != null) {
                totalDiamond = bukkitPlayer.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE);
                totalIron = bukkitPlayer.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE);
                totalGold = bukkitPlayer.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE);
                totalEmerald = bukkitPlayer.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE);
                totalCoal = bukkitPlayer.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE);
            }
            else {
                final StatsData data = this.statsManager.getUser(fp.getUuid().toString());
                if (data == null) {
                    continue;
                }
                totalDiamond = data.getTotalDiamond();
                totalIron = data.getTotalIron();
                totalGold = data.getTotalGold();
                totalEmerald = data.getTotalEmerald();
                totalCoal = data.getTotalCoal();
            }
        }
        for (String msg : this.config.getStringList("Messages.STATS-CMD")) {
            msg = msg.replace("%FACTION%", faction.getName());
            msg = msg.replace("%KILLS%", String.valueOf(totalKills));
            msg = msg.replace("%DEATHS%", String.valueOf(totalDeaths));
            msg = msg.replace("%DIAMOND%", String.valueOf(totalDiamond));
            msg = msg.replace("%IRON%", String.valueOf(totalIron));
            msg = msg.replace("%GOLD%", String.valueOf(totalGold));
            msg = msg.replace("%EMERALD%", String.valueOf(totalEmerald));
            msg = msg.replace("%COAL%", String.valueOf(totalCoal));
            player.sendMessage(Utils.color(msg));
        }
    }
    
}
