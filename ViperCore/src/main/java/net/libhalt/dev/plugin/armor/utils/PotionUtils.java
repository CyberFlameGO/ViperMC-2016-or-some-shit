package net.libhalt.dev.plugin.armor.utils;

import org.bukkit.entity.*;
import org.bukkit.potion.*;
import com.google.common.base.*;
import org.bukkit.configuration.*;
import java.util.*;

public class PotionUtils
{
    private static List<PotionEffectType> negatives;
    
    public static boolean isNegative(final PotionEffectType type) {
        return PotionUtils.negatives.contains(type);
    }
    
    public static boolean canAddConcideringLevel(final Player player, final PotionEffect effect) {
        if (player.hasPotionEffect(effect.getType())) {
            final PotionEffect before = getPotionEffect(player, effect.getType());
            return before.getAmplifier() < effect.getAmplifier() || (before.getAmplifier() == effect.getAmplifier() && before.getDuration() < effect.getDuration());
        }
        return true;
    }
    
    public static void addConcideringLevel(final Player player, final PotionEffect effect) {
        if (canAddConcideringLevel(player, effect)) {
            player.addPotionEffect(effect, true);
        }
    }
    
    public static int getLevel(final Player player, final PotionEffectType type) {
        Preconditions.checkState(player.hasPotionEffect(type));
        for (final PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().getId() == type.getId()) {
                return effect.getAmplifier() + 1;
            }
        }
        return 0;
    }
    
    public static PotionEffect getPotionEffect(final Player player, final PotionEffectType type) {
        Preconditions.checkState(player.hasPotionEffect(type));
        for (final PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().getId() == type.getId()) {
                return effect;
            }
        }
        throw new AssertionError();
    }
    
    public static List<PotionEffect> paraseList(final ConfigurationSection section) {
        final List<PotionEffect> effects = new ArrayList<PotionEffect>();
        for (final String key : section.getKeys(false)) {
            final ConfigurationSection effectSection = section.getConfigurationSection(key);
            if (effectSection == null) {
                continue;
            }
            final PotionEffectType type = PotionEffectType.getByName(effectSection.getName());
            if (type != null) {
                final int amplifier = effectSection.getInt("level") - 1;
                final int duration = effectSection.getInt("duration") * 20 + 20;
                final PotionEffect effect = new PotionEffect(type, duration, amplifier);
                effects.add(effect);
            }
            else {
                System.out.println(String.valueOf(effectSection.getName()) + " not parasable");
            }
        }
        return effects;
    }
    
    static {
        final List<PotionEffectType> negatives = new ArrayList<PotionEffectType>();
        negatives.add(PotionEffectType.POISON);
        negatives.add(PotionEffectType.WEAKNESS);
        negatives.add(PotionEffectType.BLINDNESS);
        negatives.add(PotionEffectType.WITHER);
        negatives.add(PotionEffectType.SLOW);
        negatives.add(PotionEffectType.WEAKNESS);
        PotionUtils.negatives = negatives;
    }
}
