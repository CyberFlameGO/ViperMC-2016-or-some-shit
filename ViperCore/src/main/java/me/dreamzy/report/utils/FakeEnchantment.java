package me.dreamzy.report.utils;

import java.lang.reflect.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;

public final class FakeEnchantment extends Enchantment
{
    static {
        try {
            final Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);
        }
        catch (Exception e) {
            throw new RuntimeException("Can't register enchantment", e);
        }
        if (Enchantment.getByName("CA_FAKE") == null) {
            Enchantment.registerEnchantment((Enchantment)new FakeEnchantment());
        }
    }
    
    public FakeEnchantment() {
        super(100);
    }
    
    public String getName() {
        return "CA_FAKE";
    }
    
    public int getMaxLevel() {
        return 0;
    }
    
    public int getStartLevel() {
        return 0;
    }
    
    public EnchantmentTarget getItemTarget() {
        return null;
    }
    
    public boolean conflictsWith(final Enchantment enchantment) {
        return false;
    }
    
    public boolean canEnchantItem(final ItemStack itemStack) {
        return false;
    }
}
