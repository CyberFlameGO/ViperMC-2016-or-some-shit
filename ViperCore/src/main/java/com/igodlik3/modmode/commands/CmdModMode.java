package com.igodlik3.modmode.commands;

import com.igodlik3.ItemBuilder;
import org.bukkit.configuration.*;
import java.util.*;
import com.igodlik3.modmode.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import com.igodlik3.modmode.utils.*;

public class CmdModMode implements CommandExecutor
{
    private Configuration config;
    private Set<UUID> modToggled;
    
    public CmdModMode() {
        this.config = (Configuration)ModMode.getInstance().getConfig();
        this.modToggled = ModMode.getInstance().getModToggled();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        final Player player = (Player)sender;
        final String e = this.config.getString("Messages.STATUT.enabled");
        final String d = this.config.getString("Messages.STATUT.disabled");
        if (player.hasPermission("ModMode")) {
            if (this.modToggled.contains(player.getUniqueId())) {
                this.modToggled.remove(player.getUniqueId());
                player.sendMessage(Utils.color(this.config.getString("Messages.MOD-MODE").replaceAll("%STATUT%", d)));
                player.getInventory().clear();
            }
            else {
                this.modToggled.add(player.getUniqueId());
                player.sendMessage(Utils.color(this.config.getString("Messages.MOD-MODE").replaceAll("%STATUT%", e)));
                this.getModItems(player);
            }
        }
        return false;
    }
    
    private void getModItems(final Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setFlySpeed(0.5f);
        player.getInventory().clear();
        player.getInventory().setArmorContents((ItemStack[])null);
        player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS).displayname(this.config.getString("Messages.MOD-ITEMS.COMPASS")).build());
        player.getInventory().setItem(1, new ItemBuilder(Material.WOOD_AXE).displayname(this.config.getString("Messages.MOD-ITEMS.AXE")).build());
        player.getInventory().setItem(2, new ItemBuilder(Material.EYE_OF_ENDER).displayname(this.config.getString("Messages.MOD-ITEMS.RANDOM-TP")).build());
        player.getInventory().setItem(5, new ItemBuilder(Material.CHEST).displayname(this.config.getString("Messages.MOD-ITEMS.CHEST")).build());
        player.getInventory().setItem(6, new ItemBuilder(Material.INK_SACK).durability((short)1).displayname(this.config.getString("Messages.MOD-ITEMS.VANISH").replaceAll("%STATUT%", "&cOFF")).build());
        player.getInventory().setItem(7, new ItemBuilder(Material.BLAZE_ROD).displayname(this.config.getString("Messages.MOD-ITEMS.FREEZE.ITEMS")).build());
        player.getInventory().setItem(8, new ItemBuilder(Material.BOOK).displayname(this.config.getString("Messages.MOD-ITEMS.INSPECTION")).build());
    }
}
