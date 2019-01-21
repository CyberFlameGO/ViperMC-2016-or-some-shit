package net.libhalt.dev.plugin.armor.kit;

import org.bukkit.potion.*;
import org.bukkit.configuration.*;
import net.libhalt.dev.plugin.armor.utils.*;
import org.bukkit.entity.*;
import java.util.*;

public class Miner extends AbstractClass
{
    private int undergroundY;
    private List<PotionEffect> undergroundPassives;
    
    public Miner(final ConfigurationSection section) {
        super(section);
        this.undergroundPassives = PotionUtils.paraseList(section.getConfigurationSection("passive-underground"));
        this.undergroundY = section.getInt("underground-y");
    }
    
    @Override
    public void applyPassive(final Player player) {
        if (player.getLocation().getBlockY() <= this.undergroundY) {
            for (final PotionEffect effect : this.undergroundPassives) {
                PotionUtils.addConcideringLevel(player, effect);
            }
        }
        super.applyPassive(player);
    }
}
