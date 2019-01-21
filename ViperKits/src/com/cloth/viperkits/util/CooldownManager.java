package com.cloth.viperkits.util;

import com.cloth.viperkits.ViperKits;
import com.cloth.viperkits.config.ConfigurationSetup;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by Brennan on 6/22/2017.
 */
public class CooldownManager
{
    public static void startSubtracting(){

        new BukkitRunnable(){
            public void run()
            {
                for(String players : ConfigurationSetup.playerdata.getConfigurationSection("").getKeys(false))
                {
                    double basic_cooldown = ConfigurationSetup.playerdata.getDouble(players + ".basic_cooldown");
                    double gold_cooldown = ConfigurationSetup.playerdata.getDouble(players + ".gold_cooldown");
                    double platinum_cooldown = ConfigurationSetup.playerdata.getDouble(players + ".platinum_cooldown");
                    double emerald_cooldown = ConfigurationSetup.playerdata.getDouble(players + ".emerald_cooldown");
                    double diamond_cooldown = ConfigurationSetup.playerdata.getDouble(players + ".diamond_cooldown");
                    double venom_cooldown = ConfigurationSetup.playerdata.getDouble(players + ".venom_cooldown");

                    if(basic_cooldown > 0){ ConfigurationSetup.playerdata.set(players + ".basic_cooldown", basic_cooldown - 60); }
                    if(gold_cooldown > 0){ ConfigurationSetup.playerdata.set(players + ".gold_cooldown", gold_cooldown - 60); }
                    if(platinum_cooldown > 0){ ConfigurationSetup.playerdata.set(players + ".platinum_cooldown", platinum_cooldown - 60); }
                    if(emerald_cooldown > 0){ ConfigurationSetup.playerdata.set(players + ".emerald_cooldown", emerald_cooldown - 60); }
                    if(diamond_cooldown > 0){ ConfigurationSetup.playerdata.set(players + ".diamond_cooldown", diamond_cooldown - 60); }
                    if(venom_cooldown > 0){ ConfigurationSetup.playerdata.set(players + ".venom_cooldown", venom_cooldown - 60); }

                    try{
                        for(String available : ConfigurationSetup.playerdata.getConfigurationSection(players + ".available_kits").getKeys(false))
                        {
                            if(ConfigurationSetup.playerdata.getDouble(players + ".available_kits." + available) > 0)
                            {
                                double new_amount = ConfigurationSetup.playerdata.getDouble(players + ".available_kits." + available) - 60;
                                if(new_amount <= 0){
                                    ConfigurationSetup.playerdata.set(players + ".available_kits." + available, null);
                                    ConfigurationSetup.playerdata.set(players + "." + available.toLowerCase() + "_cooldown", 0);
                                    ConfigurationSetup.playerdata.saveConfig();
                                } else {
                                    ConfigurationSetup.playerdata.set(players + ".available_kits." + available, ConfigurationSetup.playerdata.getDouble(players + ".available_kits." + available) - 60);
                                }
                            }
                        }
                    } catch (NullPointerException error){}
                }

                ConfigurationSetup.playerdata.saveConfig();

            }
        }.runTaskTimerAsynchronously(ViperKits.getInstance(), 0, 1200);
    }
}
