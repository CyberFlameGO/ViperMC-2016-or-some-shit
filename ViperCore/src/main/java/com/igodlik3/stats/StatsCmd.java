package com.igodlik3.stats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.igodlik3.ItemBuilder;

import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.faction.bean.FactionPlayer;

public class StatsCmd implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players !");
            return true;
        }
        final Player player = (Player)sender;
        if (args.length == 0) {
            this.openStatsInv(player, player);
        }
        else {
            final Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(this.color("&cPlayer not found !"));
            }
            else {
                this.openStatsInv(target, player);
            }
        }
        return true;
    }
    
    private double getKDR(final Player player, final int kill, final int mort) {
        if (mort == 0 && kill == 0) {
            return 0.0;
        }
        if (mort == 0) {
            return kill;
        }
        return Math.round(kill / mort);
    }
    
    private void openStatsInv(final Player player, final Player to) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 45, this.color("&6Stats of &c" + player.getName()));
        final FactionPlayer fp = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
        inv.setItem(0, new ItemBuilder(Material.DIAMOND_SWORD).unsafeStackSize(true).amount((fp.getKill()== 0) ? 1 : fp.getKill()).displayname("&6Kills&8: &c" +fp.getKill()).build());
        inv.setItem(1, new ItemBuilder(Material.SKULL_ITEM).unsafeStackSize(true).amount((player.getStatistic(Statistic.DEATHS) == 0) ? 1 : player.getStatistic(Statistic.DEATHS)).displayname("&6Deaths&8: &c" + player.getStatistic(Statistic.DEATHS)).build());
        inv.setItem(2, new ItemBuilder(Material.ANVIL).displayname("&6Ratio&8: &c" + this.getKDR(player, player.getStatistic(Statistic.PLAYER_KILLS), player.getStatistic(Statistic.DEATHS))).build());
        inv.setItem(9, new ItemBuilder(Material.BLAZE_POWDER).displayname("&4Faction&8: &c" + (fp.getFaction() != null ? fp.getFaction().getName() : "&cNone")).build());
        inv.setItem(18, new ItemBuilder(Material.DIAMOND_ORE).unsafeStackSize(true).amount(player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE)).displayname("&6Ores&8: &b" + player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE) + " diamonds(s)").build());
        inv.setItem(19, new ItemBuilder(Material.EMERALD_ORE).unsafeStackSize(true).amount(player.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE)).displayname("&6Ores&8: &a" + player.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE) + " emerald(s)").build());
        inv.setItem(20, new ItemBuilder(Material.GOLD_ORE).unsafeStackSize(true).amount(player.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE)).displayname("&6Ores&8: &e" + player.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE) + " gold(s)").build());
        inv.setItem(21, new ItemBuilder(Material.IRON_ORE).unsafeStackSize(true).amount(player.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE)).displayname("&6Ores&8: &f" + player.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE) + " iron(s)").build());
        inv.setItem(22, new ItemBuilder(Material.REDSTONE_ORE).unsafeStackSize(true).amount(player.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE)).displayname("&6Ores&8: &f" + player.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE) + " redstone(s)").build());
        inv.setItem(23, new ItemBuilder(Material.COAL_ORE).unsafeStackSize(true).amount(player.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE)).displayname("&6Ores&8: &f" + player.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE) + " coal(s)").build());
        inv.setItem(24, new ItemBuilder(Material.STONE).unsafeStackSize(true).amount(player.getStatistic(Statistic.MINE_BLOCK, Material.STONE)).displayname("&6Ores&8: &7" + player.getStatistic(Statistic.MINE_BLOCK, Material.STONE) + " stone(s)").build());
        inv.setItem(27, new ItemBuilder(Material.EMPTY_MAP).displayname("&6Money&8: &c" + fp.getMoney()).build());
        inv.setItem(28, new ItemBuilder(Material.DIAMOND).displayname("&6Rank&8: &f" + Stats.getInstance().getPerms().getPrimaryGroup(player)).build());
        to.openInventory(inv);
    }
    
    private String color(final String stg) {
        return ChatColor.translateAlternateColorCodes('&', stg);
    }
}
