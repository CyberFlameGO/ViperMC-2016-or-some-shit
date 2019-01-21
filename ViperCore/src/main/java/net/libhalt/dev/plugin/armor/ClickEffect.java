package net.libhalt.dev.plugin.armor;

import java.util.*;
import org.bukkit.potion.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import net.libhalt.dev.plugin.armor.utils.*;

public class ClickEffect
{
    private final List<PotionEffect> effects;
    private final Material item;
    private final int requiredPower;
    private final int cooldown;
    
    public ClickEffect(final ConfigurationSection section) {
        this.item = Material.valueOf(section.getName().toUpperCase());
        this.effects = PotionUtils.paraseList(section);
        this.requiredPower = section.getInt("power");
        this.cooldown = section.getInt("cooldown");
    }
    
    public List<PotionEffect> getEffects() {
        return this.effects;
    }
    
    public Material getItem() {
        return this.item;
    }
    
    public int getRequiredPower() {
        return this.requiredPower;
    }
    
    public int getCooldown() {
        return this.cooldown;
    }
}
