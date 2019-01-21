package com.cloth.viperores;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Brennan on 6/29/2017.
 */
public class OreCommand implements CommandExecutor
{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {

        if(!sender.isOp())
        {
            sender.sendMessage("§7(§aViper§7)§f You don't have permission.");
            return true;
        }

        if(args.length != 3){
            sender.sendMessage("§7(§aViper§7)§f Correct usage: §a/ore <player> <multiplier> <duration>");
            return true;
        }

        if(!ViperOres.getInstance().playerdata.contains(args[0].toLowerCase())){
            sender.sendMessage("§7(§aViper§7)§f That player has never joined the server.");
            return true;
        }

        String target = args[0].toLowerCase();
        String stringduration = args[2];
        int multiplier = -1;
        int duration = -1;

        try{
            multiplier = Integer.parseInt(args[1]);
        } catch (NumberFormatException e){
            sender.sendMessage("§7(§aViper§7)§f You entered an invalid multiplier.");
            return true;
        }

        if(!stringduration.toLowerCase().endsWith("m") && !stringduration.toLowerCase().endsWith("h") && !stringduration.toLowerCase().endsWith("d"))
        {
            sender.sendMessage("§7(§aViper§7)§f You entered an invalid duration.");
            return true;
        }

        try{
            if(stringduration.endsWith("m")){
                duration = Integer.parseInt(stringduration.replaceAll("m", ""));
            }

            if(stringduration.endsWith("h")){
                duration = Integer.parseInt(stringduration.replaceAll("h", "")) * 60;
            }

            if(stringduration.endsWith("d")){
                duration = Integer.parseInt(stringduration.replaceAll("d", "")) * 1440;
            }
        } catch(NumberFormatException e){
            sender.sendMessage("§7(§aViper§7)§f You entered an invalid duration.");
            return true;
        }

        if(multiplier < 0){
            sender.sendMessage("§7(§aViper§7)§f You entered an invalid multiplier.");
            return true;
        }

        if(duration < 0){
            sender.sendMessage("§7(§aViper§7)§f You entered an invalid duration.");
            return true;
        }

        ViperOres.getInstance().playerdata.set(target + ".multiplier", multiplier);
        ViperOres.getInstance().playerdata.set(target + ".duration", duration);
        ViperOres.getInstance().playerdata.saveConfig();

        sender.sendMessage("§7(§aViper§7)§f You have given §a" + target.toUpperCase() + "§f a §a" + multiplier + "x §fore multiplier for §a" + stringduration + "§f.");

        if(Bukkit.getPlayer(target) != null){
            Bukkit.getPlayer(target).sendMessage("§7(§aViper§7)§f You have received a §a" + multiplier + "x §fore multiplier!");
        }

        return true;
    }
}
