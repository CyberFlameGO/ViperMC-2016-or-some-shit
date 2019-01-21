package com.cloth.viperkits.config;

import com.cloth.viperkits.ViperKits;
import org.bukkit.DyeColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

/**
 * Created by Brennan on 6/21/2017.
 */
public class ConfigurationSetup implements Listener
{

    public static MyConfigManager manager = new MyConfigManager(ViperKits.getInstance());
    public static MyConfig gui = manager.getNewConfig("gui.yml");
    public static MyConfig messages = manager.getNewConfig("messages.yml");
    public static MyConfig playerdata = manager.getNewConfig("playerdata.yml");
    public static MyConfig kits = manager.getNewConfig("kits.yml");

    public static void setupGUI(){
        if(!gui.contains("Title"))
        {
            gui.set("Title", "&7List of Kits");
            gui.set("Size", 45);
            gui.set("Border", true);

            ArrayList<String> basickit = new ArrayList<String>();
            basickit.add("&7A kit worn by the basics");
            basickit.add("&7of this server.");
            basickit.add("");
            basickit.add("&aPurchase at: &cstore.vipermc.net");
            basickit.add("");
            basickit.add("&7Cooldown: &2%cooldown%");
            basickit.add("&7Available in: &2%available%");
            basickit.add("&7&l(&5&l!&7&l)&7 Right click to preview");
            gui.set("Item.Basic.Name", "&eBasic &7Kit");
            gui.set("Item.Basic.Type", "WHITE_GLASS");
            gui.set("Item.Basic.Slot", 10);
            gui.set("Item.Basic.Lore", basickit);

            ArrayList<String> goldkit = new ArrayList<String>();
            goldkit.add("&7A kit worn by the golds");
            goldkit.add("&7of this server.");
            goldkit.add("");
            goldkit.add("&aPurchase at: &cstore.vipermc.net");
            goldkit.add("");
            goldkit.add("&7Cooldown: &2%cooldown%");
            goldkit.add("&7Available in: &2%available%");
            goldkit.add("&7&l(&5&l!&7&l)&7 Right click to preview");
            gui.set("Item.Gold.Name", "&6Gold &7Kit");
            gui.set("Item.Gold.Type", "YELLOW_GLASS");
            gui.set("Item.Gold.Slot", 12);
            gui.set("Item.Gold.Lore", goldkit);

            ArrayList<String> platinumkit = new ArrayList<String>();
            platinumkit.add("&7A kit worn by the platinums");
            platinumkit.add("&7of this server.");
            platinumkit.add("");
            platinumkit.add("&aPurchase at: &cstore.vipermc.net");
            platinumkit.add("");
            platinumkit.add("&7Cooldown: &2%cooldown%");
            platinumkit.add("&7Available in: &2%available%");
            platinumkit.add("&7&l(&5&l!&7&l)&7 Right click to preview");
            gui.set("Item.Platinum.Name", "&dPlatinum &7Kit");
            gui.set("Item.Platinum.Type", "PINK_GLASS");
            gui.set("Item.Platinum.Slot", 14);
            gui.set("Item.Platinum.Lore", platinumkit);

            ArrayList<String> emeraldkit = new ArrayList<String>();
            emeraldkit.add("&7A kit worn by the emeralds");
            emeraldkit.add("&7of this server.");
            emeraldkit.add("");
            emeraldkit.add("&aPurchase at: &cstore.vipermc.net");
            emeraldkit.add("");
            emeraldkit.add("&7Cooldown: &2%cooldown%");
            emeraldkit.add("&7Available in: &2%available%");
            emeraldkit.add("&7&l(&5&l!&7&l)&7 Right click to preview");
            gui.set("Item.Emerald.Name", "&aEmerald &7Kit");
            gui.set("Item.Emerald.Type", "GREEN_GLASS");
            gui.set("Item.Emerald.Slot", 16);
            gui.set("Item.Emerald.Lore", emeraldkit);


            ArrayList<String> diamondkit = new ArrayList<String>();
            diamondkit.add("&7A kit worn by the diamonds");
            diamondkit.add("&7of this server.");
            diamondkit.add("");
            diamondkit.add("&aPurchase at: &cstore.vipermc.net");
            diamondkit.add("");
            diamondkit.add("&7Cooldown: &2%cooldown%");
            diamondkit.add("&7Available in: &2%available%");
            diamondkit.add("&7&l(&5&l!&7&l)&7 Right click to preview");
            gui.set("Item.Diamond.Name", "&bDiamond &7Kit");
            gui.set("Item.Diamond.Type", "BLUE_GLASS");
            gui.set("Item.Diamond.Slot", 28);
            gui.set("Item.Diamond.Lore", diamondkit);

            ArrayList<String> venomkit = new ArrayList<String>();
            venomkit.add("&7A kit worn by the venoms");
            venomkit.add("&7of this server.");
            venomkit.add("");
            venomkit.add("&aPurchase at: &cstore.vipermc.net");
            venomkit.add("");
            venomkit.add("&7Cooldown: &2%cooldown%");
            venomkit.add("&7Available in: &2%available%");
            venomkit.add("&7&l(&5&l!&7&l)&7 Right click to preview");
            gui.set("Item.Venom.Name", "&5Venom &7Kit");
            gui.set("Item.Venom.Type", "PURPLE_GLASS");
            gui.set("Item.Venom.Slot", 30);
            gui.set("Item.Venom.Lore", venomkit);

            gui.saveConfig();
        }
    }

    public static void setupKits()
    {
        if(!kits.contains("Basic.Cooldown")){
            kits.set("Basic.Cooldown", 259200);
            kits.set("Gold.Cooldown", 259200);
            kits.set("Platinum.Cooldown", 259200);
            kits.set("Emerald.Cooldown", 259200);
            kits.set("Diamond.Cooldown", 259200);
            kits.set("Venom.Cooldown", 259200);
            kits.saveConfig();
        }

        for(String x : ConfigurationSetup.gui.getConfigurationSection("Item").getKeys(false))
        {
            if(!kits.contains(x + ".Cooldown"))
            {
                kits.set(x + ".Cooldown", 259200);
                kits.saveConfig();
            }
        }
    }

    public static void setupMessages(){
        if(!messages.contains("no_permission")){
            messages.set("no_permission", "&7(&aViper&7) You don't have permission.");
            messages.set("invalid_usage", "&7(&aViper&7) Did you mean: %usage%");
            messages.set("invalid_kit", "&7(&aViper&7) That kit does not exist.");
            messages.set("invalid_player", "&7(&aViper&7) Cannot find the specified player.");
            messages.set("invalid_duration", "&7(&aViper&7) You entered an invalid duration.");
            messages.set("kit_saved", "&7(&aViper&7) %kit% has successfully been saved.");
            messages.set("kit_given", "&7(&aViper&7) You have given %player% a %kit% kit for %duration%.");
            messages.set("unavailable_kit", "&7(&aViper&7) You don't have that kit purchased.");
            messages.set("on_cooldown", "&7(&aViper&7) You are still on cooldown for %duration%");
            messages.set("redeemed_kit", "&7(&aViper&7) Successfully redeemed %kit% kit.");
            messages.saveConfig();
        }
    }

    public static void setupPlayerData(Player p)
    {
        if(!playerdata.contains(p.getName().toLowerCase() + ".basic_cooldown"))
        {
            ArrayList<String> available_kits = new ArrayList<String>();
            String name = p.getName();
            playerdata.set(name.toLowerCase() + ".basic_cooldown", 0);
            playerdata.set(name.toLowerCase() + ".gold_cooldown", 0);
            playerdata.set(name.toLowerCase() + ".platinum_cooldown", 0);
            playerdata.set(name.toLowerCase() + ".emerald_cooldown", 0);
            playerdata.set(name.toLowerCase() + ".diamond_cooldown", 0);
            playerdata.set(name.toLowerCase() + ".venom_cooldown", 0);
            playerdata.set(name.toLowerCase() + ".available_kits", available_kits);
            playerdata.saveConfig();
        }
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent e){
        final Player p =  e.getPlayer();
        new BukkitRunnable()
        {
            public void run()
            {
                setupPlayerData(p);
                return;
            }
        }.runTaskAsynchronously(ViperKits.getInstance());
    }
}
