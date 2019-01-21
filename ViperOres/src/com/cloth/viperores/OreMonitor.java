package com.cloth.viperores;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Brennan on 6/29/2017.
 */
public class OreMonitor
{
    public static void check()
    {
        new BukkitRunnable()
        {
            public void run()
            {
                if(ViperOres.getInstance().playerdata.getConfigurationSection("").getKeys(false) != null)
                {
                    if(!ViperOres.getInstance().playerdata.getConfigurationSection("").getKeys(false).isEmpty())
                    {
                        for(String x : ViperOres.getInstance().playerdata.getConfigurationSection("").getKeys(false))
                        {
                            if(ViperOres.getInstance().playerdata.getInt(x + ".duration") > 0)
                            {
                                ViperOres.getInstance().playerdata.set(x + ".duration", ViperOres.getInstance().playerdata.getInt(x + ".duration") - 5);
                                ViperOres.getInstance().playerdata.saveConfig();

                                int duration = ViperOres.getInstance().playerdata.getInt(x + ".duration");
                                String stringduration = "";

                                if(duration < 60){
                                    stringduration = duration + " minutes";
                                }

                                if(duration > 60 && duration < 1440){
                                    stringduration = duration + " hours";
                                }

                                if(duration >= 1440){
                                    stringduration = duration + " days";
                                }


                                if(duration > 0){
                                    if(Bukkit.getPlayer(x) != null)
                                    {
                                        Bukkit.getPlayer(x).sendMessage("§7§m-------------------------------------------");
                                        Bukkit.getPlayer(x).sendMessage("§eYou currently have §f" + stringduration + "§e of ore multiplier left.");
                                        Bukkit.getPlayer(x).sendMessage("§7§m-------------------------------------------");
                                    }
                                } else {
                                    ViperOres.getInstance().playerdata.set(x + ".multiplier", 0);
                                    ViperOres.getInstance().playerdata.saveConfig();
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(ViperOres.getInstance(), 0, 6000);
    }
}
