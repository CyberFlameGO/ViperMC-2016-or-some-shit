package net.libhalt.dev.plugin.armor.kit;

import org.bukkit.configuration.*;

public class Rouge extends AbstractClass
{
    private double backstabDamage;
    private double backstabDegree;
    private long backstabCoolDown;
    
    public Rouge(final ConfigurationSection section) {
        super(section);
        this.backstabDamage = section.getDouble("backstab-damage");
        this.backstabDegree = section.getDouble("degree");
        this.backstabCoolDown = section.getLong("cooldown");
    }
    
    public double getBackstabDamage() {
        return this.backstabDamage;
    }
    
    public double getBackstabDegree() {
        return this.backstabDegree;
    }
    
    public long getBackstabCoolDown() {
        return this.backstabCoolDown;
    }
}
