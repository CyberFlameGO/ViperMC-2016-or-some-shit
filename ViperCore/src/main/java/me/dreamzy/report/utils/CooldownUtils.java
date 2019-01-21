package me.dreamzy.report.utils;

import java.util.*;
import org.bukkit.entity.*;

public class CooldownUtils
{
    private static HashMap<String, HashMap<UUID, Long>> cooldown;
    
    static {
        CooldownUtils.cooldown = new HashMap<String, HashMap<UUID, Long>>();
    }
    
    public static void clearCooldowns() {
        CooldownUtils.cooldown.clear();
    }
    
    public static void createCooldown(final String k) {
        if (CooldownUtils.cooldown.containsKey(k)) {
            throw new IllegalArgumentException("Ce cooldown existe d\u00e9j\u00e0");
        }
        CooldownUtils.cooldown.put(k, new HashMap<UUID, Long>());
    }
    
    public static HashMap<UUID, Long> getCooldownMap(final String k) {
        if (CooldownUtils.cooldown.containsKey(k)) {
            return CooldownUtils.cooldown.get(k);
        }
        return null;
    }
    
    public static void addCooldown(final String k, final Player p, final int seconds) {
        if (!CooldownUtils.cooldown.containsKey(k)) {
            createCooldown(k);
        }
        final long next = System.currentTimeMillis() + seconds * 1000L;
        CooldownUtils.cooldown.get(k).put(p.getUniqueId(), next);
    }
    
    public static boolean isOnCooldown(final String k, final Player p) {
        return CooldownUtils.cooldown.containsKey(k) && CooldownUtils.cooldown.get(k).containsKey(p.getUniqueId()) && System.currentTimeMillis() <= CooldownUtils.cooldown.get(k).get(p.getUniqueId());
    }
    
    public static int getCooldownForPlayerInt(final String k, final Player p) {
        return (int)((CooldownUtils.cooldown.get(k).get(p.getUniqueId()) - System.currentTimeMillis()) / 1000L);
    }
    
    public static long getCooldownForPlayerLong(final String k, final Player p) {
        return CooldownUtils.cooldown.get(k).get(p.getUniqueId()) - System.currentTimeMillis();
    }
    
    public static void removeCooldown(final String k, final Player p) {
        if (!CooldownUtils.cooldown.containsKey(k)) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf(k)) + " n'existe pas");
        }
        CooldownUtils.cooldown.get(k).remove(p.getUniqueId());
    }
}
