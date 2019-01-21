package com.cloth.viperkits.util;

import com.cloth.viperkits.config.ConfigurationSetup;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Brennan on 6/22/2017.
 */
public class Util
{
    public static ArrayList<String> kitAliases = new ArrayList<String>();

    public static void setupAliases()
    {
        kitAliases.add("kits");
    }

    public static String getKit(String args)
    {
        for(String x : ConfigurationSetup.gui.getConfigurationSection("Item").getKeys(false))
        {
            if(args.equalsIgnoreCase(x))
            {
                return x;
            }
        }
        return null;
    }

    public static double convertStringToSeconds(String args){

        try{
            if(args.endsWith("s")){
                double var1 = Double.parseDouble(args.replace("s", ""));
                return var1;
            }
            if(args.endsWith("m")){
                double var1 = Double.parseDouble(args.replace("m", ""));
                double var2 = var1 * 60;
                return var2;
            }
            if(args.endsWith("h")){
                double var1 = Double.parseDouble(args.replace("h", ""));
                double var2 = var1 * 3600;
                return var2;
            }
            if(args.endsWith("d")){
                double var1 = Double.parseDouble(args.replace("d", ""));
                double var2 = var1 * 86400;
                return var2;
            }
        } catch (NumberFormatException error) { return -1; }

        return -1;
    }

    public static boolean isKitAvailable(Player p, String kit){

        if(ConfigurationSetup.playerdata.contains(p.getName().toLowerCase() + ".available_kits." + kit))
        {
            return true;
        }

        if(kit.equalsIgnoreCase("basic")){
            if(p.hasPermission("basic.rank")){
                return true;
            }
        }

        if(kit.equalsIgnoreCase("gold")){
            if(p.hasPermission("gold.rank")){
                return true;
            }
        }

        if(kit.equalsIgnoreCase("platinum")){
            if(p.hasPermission("platinum.rank")){
                return true;
            }
        }

        if(kit.equalsIgnoreCase("emerald")){
            if(p.hasPermission("emerald.rank")){
                return true;
            }
        }

        if(kit.equalsIgnoreCase("diamond")){
            if (p.hasPermission("diamond.rank")) {
                return true;
            }
        }

        if(kit.equalsIgnoreCase("venom")){
            if(p.hasPermission("venom.rank")){
                return true;
            }
        }

        if(kit.equalsIgnoreCase("starter")){
            if(p.hasPermission("starter.rank")){
                return true;
            }
        }

        return false;
    }

    public static String getKitFromSlot(int slot){
        for(String x : ConfigurationSetup.gui.getConfigurationSection("Item").getKeys(false))
        {
            if(slot == ConfigurationSetup.gui.getInt("Item." + x + ".Slot"))
            {
                return x;
            }
        }
        return null;
    }


    public static String cooldownToString(double cooldown){

        if(cooldown <= 0){
            return "Now";
        }

        if(cooldown <= 60){
            return cooldown + " seconds.";
        }

        if(cooldown >= 60 && cooldown <= 3600)
        {
            double x = Math.round((cooldown / 60)*100.0)/100.0;
            return x + " minutes.";
        }

        if(cooldown >= 3600 && cooldown <= 86400)
        {
            double x = Math.round((cooldown / 3600)*100.0)/100.0;
            return x + " hours.";
        }

        if(cooldown >= 86400){
            double z = cooldown / 3600 / 24;
            double x = Math.round((z*100.0)/100.0);
            return x + " days.";
        }

        return "";
    }

    public static boolean kitExists(String kit){
        try{
            for(String x : ConfigurationSetup.gui.getConfigurationSection("Item").getKeys(false))
            {
                if(kit.equalsIgnoreCase(x))
                {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException error){ return false; }
    }
}
