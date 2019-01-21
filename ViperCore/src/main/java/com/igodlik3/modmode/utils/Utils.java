package com.igodlik3.modmode.utils;

import org.bukkit.*;

public class Utils
{
    public static String color(final String stg) {
        return ChatColor.translateAlternateColorCodes('&', stg);
    }
    
    public static String stringifyLocation(final Location location) {
        return "[" + location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "]";
    }
    
    public static Location destringifyLocation(final String string) {
        final String[] split = string.substring(1, string.length() - 2).split(",");
        final World world = Bukkit.getWorld(split[0]);
        if (world == null) {
            return null;
        }
        final double x = Double.parseDouble(split[1]);
        final double y = Double.parseDouble(split[2]);
        final double z = Double.parseDouble(split[3]);
        return new Location(world, x, y, z);
    }
}
