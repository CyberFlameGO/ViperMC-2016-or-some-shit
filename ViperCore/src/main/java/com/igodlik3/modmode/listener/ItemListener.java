package com.igodlik3.modmode.listener;

import com.igodlik3.ItemBuilder;
import com.igodlik3.subclaims.FactionsUtils;
import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.syuu.common.utils.PlayerUtils;
import net.syuu.popura.faction.FactionType;
import org.bukkit.configuration.*;
import com.igodlik3.modmode.manager.*;
import com.igodlik3.modmode.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import java.util.*;
import org.bukkit.entity.*;
import com.igodlik3.modmode.event.*;
import org.bukkit.event.*;
import com.igodlik3.modmode.utils.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.*;
import org.bukkit.event.inventory.*;
import org.bukkit.*;

public class ItemListener implements Listener
{
    private Configuration config;
    private FreezeManager manager;
    private HCFactionPlugin plugin;

    public ItemListener(HCFactionPlugin instance) {
        this.config = (Configuration)ModMode.getInstance().getConfig();
        this.manager = ModMode.getInstance().getFreezeManager();
        this.plugin = instance;
    }
    
    @EventHandler
    public void onEntityInteract(final PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            final Player target = (Player)event.getRightClicked();
            final Player player = event.getPlayer();
            if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
                final ItemStack item = player.getItemInHand();
                if (item.getType() == Material.BOOK && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.color(this.config.getString("Messages.MOD-ITEMS.INSPECTION")))) {
                    if (this.isMod(player)) {
                        player.performCommand("invsee " + target.getName());
                        player.sendMessage(Utils.color(this.config.getString("Messages.MOD-ITEMS.OPENING-INVENTORY").replaceAll("%TARGET%", target.getName())));
                    }
                    else {

                        player.setItemInHand(null);
                        PlayerUtils.updateInventory(player);
                        //player.sendMessage(Utils.color(this.config.getString("Messages.MOD-ITEMS.MODE-NOT-TOGGLED")));
                    }
                }
                else if (item.getType() == Material.BLAZE_ROD && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.color(this.config.getString("Messages.MOD-ITEMS.FREEZE.ITEMS")))) {
                    if (this.isMod(player)) {
                        player.sendMessage(this.manager.freezePlayer(target));
                    }
                    else {
                        player.setItemInHand(null);
                        PlayerUtils.updateInventory(player);
                        //player.sendMessage(Utils.color(this.config.getString("Messages.MOD-ITEMS.MODE-NOT-TOGGLED")));
                    }
                }
            }
        }
    }

    public Player getCorrectPlayer(Player moderator){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(!p.equals(moderator)) {
                if(FactionsUtils.getFactionAt(p.getLocation()) == null){
                    return p;
                }

                String name = FactionsUtils.getFactionAt(p.getLocation()).getName();

                if(!name.equals("Spawn")){
                    return p;
                }
            }
        }
        return null;
    }

    public Player getRandomPlayer(Player moderator) {
        final Random r = new Random();
        final Player[] pls = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        Player target = pls[r.nextInt(pls.length)];

        while(target.equals(moderator)){
            target = pls[r.nextInt(pls.length)];
        }

        if(FactionsUtils.getFactionAt(target.getLocation()) != null){
            if(FactionsUtils.getFactionAt(target.getLocation()).getName().equals("Spawn")){
                target = getCorrectPlayer(moderator);
            }
        }
        return target;
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null && event.getItem().getType() != Material.AIR) {
            final ItemStack item = event.getItem();
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                switch (item.getType()) {
                    case EYE_OF_ENDER: {
                        event.setCancelled(true);
                        if(this.isMod(player)) {

                            if(Bukkit.getOnlinePlayers().size() <= 1){
                                player.sendMessage(plugin.configdata.getConfigMessage("only-one-online").replaceAll("&", "§"));
                                return;
                            }

                            if(getCorrectPlayer(player) == null) {
                                player.sendMessage(plugin.configdata.getConfigMessage("all-players-in-spawn").replaceAll("&", "§"));
                                return;
                            }

                            final Player target = getRandomPlayer(player);

                            if (target != player) {
                                player.teleport((Entity) target);
                                player.sendMessage(Utils.color(this.config.getString("Messages.MOD-ITEMS.RANDOM-TELEPORTATION").replaceAll("%PLAYER%", target.getName())));
                                break;
                            }
                        }else{
                            player.setItemInHand(null);
                            PlayerUtils.updateInventory(player);
                        }
                        break;
                    }
                    case INK_SACK: {
                        event.setCancelled(true);
                        if(this.isMod(player)) {

                            if (VanishEvent.vanished.contains(player)) {
                                Bukkit.getPluginManager().callEvent((Event) new VanishEvent(player, false));
                                player.setItemInHand(new ItemBuilder(Material.INK_SACK).durability((short) 1).displayname(this.config.getString("Messages.MOD-ITEMS.VANISH").replaceAll("%STATUT%", "&cOFF")).build());
                                break;
                            }
                            Bukkit.getPluginManager().callEvent((Event) new VanishEvent(player, true));
                            player.setItemInHand(new ItemBuilder(Material.INK_SACK).durability((short) 10).displayname(this.config.getString("Messages.MOD-ITEMS.VANISH").replaceAll("%STATUT%", "&aON")).build());
                        }else{
                            player.setItemInHand(null);
                            PlayerUtils.updateInventory(player);
                        }

                            break;
                    }
                    case CHEST: {

                        event.setCancelled(true);
                        if(this.isMod(player)) {

                            final Inventory inv = Bukkit.createInventory((InventoryHolder) null, 54, Utils.color("&bPlayers under level 16"));
                            Player[] onlinePlayers;
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (p.getLocation().getY() <= 16.0) {
                                    final ItemStack is = new ItemBuilder(Material.SKULL_ITEM).durability((short) 3).displayname("&b" + p.getName()).lore("", "&c�� &eClick to teleport !").build();
                                    final SkullMeta meta = (SkullMeta) is.getItemMeta();
                                    meta.setOwner(p.getName());
                                    is.setItemMeta((ItemMeta) meta);
                                    inv.addItem(new ItemStack[]{is});
                                }
                            }
                            player.openInventory(inv);
                        }else{
                            player.setItemInHand(null);
                            PlayerUtils.updateInventory(player);
                        }
                        break;
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Inventory inv = event.getInventory();
        final Player player = (Player)event.getWhoClicked();
        if (inv != null && inv.getName() != null && inv.getName().contains("level 16")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                final ItemStack item = event.getCurrentItem();
                if (event.getClickedInventory() == player.getOpenInventory().getTopInventory() && item.getType() == Material.SKULL_ITEM) {
                    player.closeInventory();
                    final Player pls = Bukkit.getPlayer(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                    if (pls == null) {
                        player.sendMessage(Utils.color(this.config.getString("Messages.MOD-ITEMS.GUI.NOT-ONLINE")));
                        return;
                    }
                    player.teleport((Entity)pls);
                    player.sendMessage(Utils.color(this.config.getString("Messages.MOD-ITEMS.GUI.TELEPORTATION").replaceAll("%PLAYER%", pls.getName())));
                }
            }
        }
    }

    private boolean isMod(final Player player) {
        return player.hasPermission("ModMode") && ModMode.getInstance().getModToggled().contains(player.getUniqueId());
    }
}
