package net.libhalt.bukkit.kaede.utils;

import org.bukkit.configuration.file.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public class Calculations
{
    private static String version;
    
    static {
        Calculations.version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
    
    public static String LangConfig(final FileConfiguration fileConfig, final String configValue, String text, final boolean colorize) {
        fileConfig.addDefault(configValue, (Object)text);
        final String langVariable = fileConfig.getString(configValue);
        if (colorize) {
            text = ChatColor.translateAlternateColorCodes('&', langVariable);
        }
        fileConfig.set(configValue, (Object)langVariable);
        return text;
    }
    
    public static int levelToExp(final int level) {
        if (Calculations.version.contains("1_7")) {
            if (level <= 15) {
                return 17 * level;
            }
            if (level <= 30) {
                return 3 * level * level / 2 - 59 * level / 2 + 360;
            }
            return 7 * level * level / 2 - 303 * level / 2 + 2220;
        }
        else {
            if (level <= 15) {
                return level * level + 6 * level;
            }
            if (level <= 30) {
                return (int)(2.5 * level * level - 40.5 * level + 360.0);
            }
            return (int)(4.5 * level * level - 162.5 * level + 2220.0);
        }
    }
    
    public static int deltaLevelToExp(final int level) {
        if (Calculations.version.contains("1_7")) {
            if (level <= 15) {
                return 17;
            }
            if (level <= 30) {
                return 3 * level - 31;
            }
            return 7 * level - 155;
        }
        else {
            if (level <= 15) {
                return 2 * level + 7;
            }
            if (level <= 30) {
                return 5 * level - 38;
            }
            return 9 * level - 158;
        }
    }
    
    public static int currentlevelxpdelta(final Player player) {
        final int levelxp = deltaLevelToExp(player.getLevel()) - (levelToExp(player.getLevel()) + Math.round(deltaLevelToExp(player.getLevel()) * player.getExp()) - levelToExp(player.getLevel()));
        return levelxp;
    }

    public static String variablereplace(String text, final String Replace, final String ReplaceWhat) {
        text = ReplaceWhat.replace(Replace, text);
        return text;
    }
    
    public static String toSentenceCase(final String inputString) {
        String result = "";
        if (inputString.length() == 0) {
            return result;
        }
        final char firstChar = inputString.charAt(0);
        final char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = String.valueOf(result) + firstCharToUpperCase;
        boolean terminalCharacterEncountered = false;
        final char[] terminalCharacters = { '.', '?', '!' };
        for (int i = 1; i < inputString.length(); ++i) {
            final char currentChar = inputString.charAt(i);
            if (terminalCharacterEncountered) {
                if (currentChar == ' ') {
                    result = String.valueOf(result) + currentChar;
                }
                else {
                    final char currentCharToUpperCase = Character.toUpperCase(currentChar);
                    result = String.valueOf(result) + currentCharToUpperCase;
                    terminalCharacterEncountered = false;
                }
            }
            else {
                final char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = String.valueOf(result) + currentCharToLowerCase;
            }
            for (int j = 0; j < terminalCharacters.length; ++j) {
                if (currentChar == terminalCharacters[j]) {
                    terminalCharacterEncountered = true;
                    break;
                }
            }
        }
        return result;
    }
    
    public static int countItems(final Player player, final int itemID) {
        final PlayerInventory inventory = player.getInventory();
        int amount = 0;
        for (int slot = 0; slot < inventory.getSize(); ++slot) {
            final ItemStack curItem = inventory.getItem(slot);
            if (curItem != null && curItem.getTypeId() == itemID) {
                amount += curItem.getAmount();
            }
        }
        return amount;
    }
    
    public static int getPlayerExperience(final Player player) {
        final int bukkitExp = levelToExp(player.getLevel()) + Math.round(deltaLevelToExp(player.getLevel()) * player.getExp());
        return bukkitExp;
    }
    
    public static boolean checkInventory(final Player player, final int itemID, final int amount) {
        final PlayerInventory inventory = player.getInventory();
        return inventory.contains(itemID, amount);
    }
    
    public static boolean consumeItem(final Player player, final int itemID, int amount) {
        final PlayerInventory inventory = player.getInventory();
        int i = -1;
        ItemStack[] contents;
        for (int length = (contents = inventory.getContents()).length, j = 0; j < length; ++j) {
            final ItemStack one = contents[j];
            ++i;
            if (one != null) {
                if (one.getTypeId() == itemID && amount > 0) {
                    if (one.getAmount() > amount) {
                        one.setAmount(one.getAmount() - amount);
                        break;
                    }
                    amount -= one.getAmount();
                    inventory.setItem(i, (ItemStack)null);
                    if (amount > 0) {}
                }
            }
        }
        return true;
    }
}
