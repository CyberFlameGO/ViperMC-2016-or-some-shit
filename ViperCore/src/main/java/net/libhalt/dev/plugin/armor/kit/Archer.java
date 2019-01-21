package net.libhalt.dev.plugin.armor.kit;

import org.bukkit.configuration.*;
import net.libhalt.dev.plugin.armor.utils.*;

public class Archer extends AbstractClass
{
    private String formula;
    
    public Archer(final ConfigurationSection section) {
        super(section);
        this.formula = section.getString("archer-damage-formula");
    }
    
    public double calculate(final double distance, final double damage) {
        return Eval.eval(this.formula.replace("distance", String.valueOf(distance)).replace("damage", String.valueOf(damage)));
    }
}
