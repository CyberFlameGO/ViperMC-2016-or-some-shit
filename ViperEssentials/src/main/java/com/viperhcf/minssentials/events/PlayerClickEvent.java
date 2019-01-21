package com.viperhcf.minssentials.events;

import com.viperhcf.minssentials.Minssentials;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by Brennan on 6/27/2017.
 */
public class PlayerClickEvent implements Listener
{

    Minssentials plugin;
    public PlayerClickEvent(Minssentials instance){
        this.plugin = instance;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e)
    {

        if(ChatColor.stripColor(e.getInventory().getTitle()).startsWith("Tags")){

            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();


            if(e.getCurrentItem() == null){
                return;
            }

            if(e.getCurrentItem().getType() == Material.IRON_DOOR)
            {
                p.closeInventory();
                return;
            }

            boolean hasTag = false;
            String tag = "";

            if(!Minssentials.getInstance().playerdata.getString(p.getName() + ".tag").equals("None"))
            {
                hasTag = true;
                tag = Minssentials.getInstance().playerdata.getString(p.getName() + ".tag").replaceAll("&", "§");
            }

            if(e.getCurrentItem().getType() == Material.SKULL_ITEM)
            {
                if(hasTag)
                {
                    p.sendMessage("§7(§aViper§7)§f Your tag has been removed.");
                    p.closeInventory();
                    Minssentials.getInstance().playerdata.set(p.getName() + ".tag", "None");
                    Minssentials.getInstance().playerdata.saveConfig();
                    return;
                }
            }

            if(e.getCurrentItem().getType() == Material.NAME_TAG)
            {
                String clicked_tag = e.getCurrentItem().getItemMeta().getDisplayName().replace("§aTag §f» ", "");

                if(tag.equals(clicked_tag)){
                    return;
                }

                String permission = Minssentials.getInstance().tags.getString(clicked_tag + ".permission");

                if(!p.hasPermission(permission)){
                    p.sendMessage("§7(§aViper§7)§f You don't have permission to use this tag.");
                    p.closeInventory();
                    return;
                }

                p.sendMessage("§7(§aViper§7)§f Your tag has been set to: " + Minssentials.getInstance().tags.getString(clicked_tag + ".name").replaceAll("&", "§"));
                p.closeInventory();
                Minssentials.getInstance().playerdata.set(p.getName() + ".tag", clicked_tag);
                Minssentials.getInstance().playerdata.saveConfig();
                return;
            }

            return;
        }

        String title = plugin.getConfig().getString("nickname-gui-title").replaceAll("&", "§");
        if(e.getInventory().getTitle().equalsIgnoreCase(title)) {

            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            if(e.getCurrentItem() != null)
            {
                if(e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
                    String name = e.getCurrentItem().getItemMeta().getDisplayName();

                    switch (ChatColor.stripColor(name)) {
                        case "Dark Green":
                            plugin.playerdata.set(p.getName() + ".color", "&2");
                            plugin.playerdata.saveConfig();
                            break;
                        case "Light Green":
                            plugin.playerdata.set(p.getName() + ".color", "&a");
                            plugin.playerdata.saveConfig();
                            break;
                        case "Aqua":
                            plugin.playerdata.set(p.getName() + ".color", "&b");
                            plugin.playerdata.saveConfig();
                            break;
                        case "Blue":
                            plugin.playerdata.set(p.getName() + ".color", "&9");
                            plugin.playerdata.saveConfig();
                            break;
                        case "Purple":
                            plugin.playerdata.set(p.getName() + ".color", "&5");
                            plugin.playerdata.saveConfig();
                            break;
                        case "Pink":
                            plugin.playerdata.set(p.getName() + ".color", "&d");
                            plugin.playerdata.saveConfig();
                            break;
                        case "Yellow":
                            plugin.playerdata.set(p.getName() + ".color", "&e");
                            plugin.playerdata.saveConfig();
                            break;
                        case "Gold":
                            plugin.playerdata.set(p.getName() + ".color", "&6");
                            plugin.playerdata.saveConfig();
                            break;
                        case "Red":
                            plugin.playerdata.set(p.getName() + ".color", "&c");
                            plugin.playerdata.saveConfig();
                            break;
                        case "White":
                            plugin.playerdata.set(p.getName() + ".color", "&f");
                            plugin.playerdata.saveConfig();
                            break;
                        case "Dark Grey":
                            plugin.playerdata.set(p.getName() + ".color", "&8");
                            plugin.playerdata.saveConfig();
                            break;
                        case "Light Grey":
                            plugin.playerdata.set(p.getName() + ".color", "&7");
                            plugin.playerdata.saveConfig();
                            break;
                    }

                    String msg = plugin.getConfig().getString("nickname-changed").replaceAll("&", "§");
                    p.sendMessage(msg);
                    p.closeInventory();
                    return;
                }
            }
            return;
        }
    }
}
