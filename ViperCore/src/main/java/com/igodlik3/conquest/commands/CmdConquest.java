package com.igodlik3.conquest.commands;

import org.bukkit.configuration.*;
import com.igodlik3.conquest.loot.*;
import com.igodlik3.conquest.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.igodlik3.conquest.utils.*;
import com.igodlik3.conquest.listener.*;
import com.igodlik3.conquest.event.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.block.*;

public class CmdConquest implements CommandExecutor
{
    private Configuration config;
    private ConquestManager cm;
    private LootManager lm;
    private ChestLootManager chest;
    private ChestKey key;
    
    public CmdConquest() {
        this.config = (Configuration)Conquest.getInstance().getConfig();
        this.cm = Conquest.getInstance().getConquestManager();
        this.lm = Conquest.getInstance().getLootManager();
        this.chest = new ChestLootManager();
        this.key = new ChestKey();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        final Player player = (Player)sender;
        if (args.length == 0) {
            this.sendUsage(player);
        }
        else if (args[0].equalsIgnoreCase("create")) {
            if (!this.hasPermission(player)) {
                return true;
            }
            if (args.length < 2) {
                this.sendUsage(player);
            }
            else {
                final String name = args[1];
                if (this.cm.gameExists(name)) {
                    player.sendMessage(Utils.color(this.config.getString("Messages.ALREADY-EXIST")));
                    return true;
                }
                this.cm.saveOrUpdateGame(new ConquestGame(name));
                player.sendMessage(Utils.color(this.config.getString("Messages.CREATED").replaceAll("%NAME%", name)));
            }
        }
        else if (args[0].equalsIgnoreCase("delete")) {
            if (!this.hasPermission(player)) {
                return true;
            }
            if (args.length < 2) {
                this.sendUsage(player);
            }
            else {
                final String name = args[1];
                final ConquestGame game = this.cm.getGame(name);
                if (game == null) {
                    player.sendMessage(Utils.color(this.config.getString("Messages.DOESNT-EXIST")));
                }
                else if (this.cm.getRunningGame() != null && game.getName().equals(this.cm.getRunningGame().getName())) {
                    player.sendMessage(Utils.color(this.config.getString("Messages.CONQUEST-RUNNING")));
                }
                else {
                    this.cm.deleteGame(game);
                    player.sendMessage(Utils.color(this.config.getString("Messages.DELETED").replaceAll("%NAME%", name)));
                }
            }
        }
        else if (args[0].equalsIgnoreCase("start")) {
            if (!this.hasPermission(player)) {
                return true;
            }
            if (args.length < 2) {
                this.sendUsage(player);
            }
            else {
                if (this.cm.getRunningGame() != null) {
                    player.sendMessage(Utils.color(this.config.getString("Messages.CONQUEST-RUNNING")));
                    return true;
                }
                final ConquestGame game2 = this.cm.getGame(args[1]);
                if (game2 == null) {
                    player.sendMessage(Utils.color(this.config.getString("Messages.DOESNT-EXIST")));
                    return true;
                }
                if (!game2.isComplete()) {
                    player.sendMessage(Utils.color(this.config.getString("Messages.INCOMPLET-CONQUEST")));
                    return true;
                }
                this.cm.startConquest(game2);
                this.cm.setRunningGame(game2);
            }
        }
        else if (args[0].equalsIgnoreCase("stop")) {
            if (!this.hasPermission(player)) {
                return true;
            }
            final ConquestGame game2 = this.cm.getRunningGame();
            if (game2 == null) {
                player.sendMessage(Utils.color(this.config.getString("Messages.NONE-RUNNING-CONQUEST")));
                return true;
            }
            this.cm.setRunningGame(null);
            this.cm.resetRunningGameData();
            Bukkit.broadcastMessage(Utils.color(this.config.getString("Messages.CONQUEST-STOPPED").replaceAll("%NAME%", game2.getName())));
        }
        else if (args[0].equalsIgnoreCase("area")) {
            if (!this.hasPermission(player)) {
                return true;
            }
            if (args.length < 3) {
                this.sendUsage(player);
            }
            else {
                final ConquestGame game2 = this.cm.getGame(args[1]);
                if (game2 == null) {
                    player.sendMessage(Utils.color(this.config.getString("Messages.DOESNT-EXIST")));
                    return true;
                }
                if (this.cm.getRunningGame() != null && game2.getName().equals(this.cm.getRunningGame().getName())) {
                    player.sendMessage(Utils.color(this.config.getString("Messages.CONQUEST-RUNNING")));
                    return true;
                }
                if (!SelectionListener.isCorrectSelection(player)) {
                    player.sendMessage(Utils.color(this.config.getString("Messages.NO-SELECTION")));
                    return true;
                }
                ConquestArea.Type type;
                try {
                    type = ConquestArea.Type.valueOf(args[2].toUpperCase());
                }
                catch (IllegalArgumentException e) {
                    player.sendMessage(Utils.color(this.config.getString("Messages.INVALID-AREA")));
                    return true;
                }
                final Location loc1 = SelectionListener.getSelection(player).get(0);
                final Location loc2 = SelectionListener.getSelection(player).get(1);
                final boolean wasComplete = game2.isComplete();
                game2.setArea(type, new ConquestArea(game2, type, loc1, loc2));
                this.cm.saveOrUpdateGame(game2);
                player.sendMessage(Utils.color(this.config.getString("Messages.AREA-UPTADED")));
                if (!wasComplete && game2.isComplete()) {
                    player.sendMessage(Utils.color(this.config.getString("Messages.CONQUEST-READY").replaceAll("%NAME%", args[1])));
                }
            }
        }
        else if (args[0].equalsIgnoreCase("list")) {
            if (!this.hasPermission(player)) {
                return true;
            }
            final Set<String> games = this.cm.listGames();
            if (games == null || games.isEmpty()) {
                player.sendMessage(Utils.color(this.config.getString("Messages.NONE-CONQUEST")));
            }
            else {
                for (final String game3 : games) {
                    player.sendMessage(Utils.color("&c- &e" + game3));
                }
            }
        }
        else if (args[0].equalsIgnoreCase("tp")) {
            if (!this.hasPermission(player)) {
                return true;
            }
            if (args.length < 2) {
                this.sendUsage(player);
            }
            else {
                final String name = args[1];
                final ConquestGame game = this.cm.getGame(name);
                if (game == null) {
                    player.sendMessage(Utils.color(this.config.getString("Messages.DOESNT-EXIST")));
                    return true;
                }
                final Location loc3 = this.cm.getConquestCubo(game.getArea(ConquestArea.Type.RED)).getCenter();
                player.teleport(loc3);
                player.sendMessage(Utils.color(this.config.getString("Messages.TELEPORTATION")));
            }
        }
        else if (args[0].equalsIgnoreCase("editloot")) {
            if (!this.hasPermission(player)) {
                return true;
            }
            this.lm.openLoot(player, true);
        }
        else if (args[0].equalsIgnoreCase("wand")) {
            if (!this.hasPermission(player)) {
                return true;
            }
            SelectionListener.getWand(player);
        }
        else if (args[0].equalsIgnoreCase("loot")) {
            this.lm.openLoot(player, false);
        }
        else if (args[0].equalsIgnoreCase("chest")) {
            if (!this.hasPermission(player)) {
                return true;
            }
            if (args.length < 2) {
                this.sendUsage(player);
            }
            else if (args[1].equalsIgnoreCase("create")) {
                final Block block = player.getTargetBlock((HashSet)null, 10);
                if ((block != null && block.getType() == Material.CHEST) || block.getType() == Material.ENDER_CHEST || block.getType() == Material.TRAPPED_CHEST) {
                    if (this.chest.isChestLoot(block.getLocation())) {
                        player.sendMessage(Utils.color(this.config.getString("Messages.ALREADY-CHESTLOOT")));
                    }
                    else {
                        player.sendMessage(Utils.color(this.config.getString("Messages.CHEST-LOOT-CREATED")));
                        this.chest.addLocation(block.getLocation());
                    }
                }
                else {
                    player.sendMessage(Utils.color(this.config.getString("Messages.NOT-A-CHEST")));
                }
            }
            else if (args[1].equalsIgnoreCase("delete")) {
                final Block block = player.getTargetBlock((HashSet)null, 10);
                if ((block != null && block.getType() == Material.CHEST) || block.getType() == Material.ENDER_CHEST || block.getType() == Material.TRAPPED_CHEST) {
                    if (this.chest.isChestLoot(block.getLocation())) {
                        player.sendMessage(Utils.color(this.config.getString("Messages.CHEST-LOOT-REMOVED")));
                        this.chest.removeLocation(block.getLocation());
                    }
                    else {
                        player.sendMessage(Utils.color(this.config.getString("Messages.NOT-A-CHESTLOOT")));
                    }
                }
                else {
                    player.sendMessage(Utils.color(this.config.getString("Messages.NOT-A-CHEST")));
                }
            }
            else if (args[1].equalsIgnoreCase("key")) {
                this.key.giveKey(player);
                player.sendMessage(Utils.color("&aKey received !"));
            }
        }
        return true;
    }
    
    private void sendUsage(final Player player) {
        if (this.hasPermission(player)) {
            for (final String stg : this.config.getStringList("Messages.USAGE.STAFF")) {
                player.sendMessage(Utils.color(stg));
            }
        }
        else {
            for (final String stg : this.config.getStringList("Messages.USAGE.PLAYER")) {
                player.sendMessage(Utils.color(stg));
            }
        }
    }
    
    private boolean hasPermission(final Player player) {
        if (player.hasPermission("Conquest.Admin")) {
            return true;
        }
        player.sendMessage(Utils.color(this.config.getString("Messages.NO-PERM")));
        return false;
    }
}
