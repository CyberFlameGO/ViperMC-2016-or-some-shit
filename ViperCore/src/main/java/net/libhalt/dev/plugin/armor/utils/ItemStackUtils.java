package net.libhalt.dev.plugin.armor.utils;

import org.bukkit.inventory.*;
import java.util.*;
import com.google.common.base.*;
import org.bukkit.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.entity.*;

public class ItemStackUtils
{
    private static int[] $SWITCH_TABLE$net$libhalt$dev$plugin$armor$utils$Armor;
    private static int[] $SWITCH_TABLE$org$bukkit$Material;
    
    public static ItemStack setItemLore(final ItemStack item, final List<String> lore) {
        Preconditions.checkNotNull((Object)item);
        Preconditions.checkNotNull((Object)lore);
        Preconditions.checkState(item.getType() != Material.AIR);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore((List)lore);
        item.setItemMeta(itemMeta);
        return item;
    }
    
    public static ItemStack setItemTitle(final ItemStack item, final String title) {
        Preconditions.checkNotNull((Object)item);
        Preconditions.checkNotNull((Object)title);
        Preconditions.checkState(item.getType() != Material.AIR);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(title);
        item.setItemMeta(itemMeta);
        return item;
    }
    
    public static boolean isEmpty(final ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }
    
    public static boolean isWearingFull(final Player player, final Armor armor) {
        for (int i = 0; i < 4; ++i) {
            final ItemStack item = player.getInventory().getArmorContents()[i];
            if (item == null) {
                return false;
            }
            switch ($SWITCH_TABLE$net$libhalt$dev$plugin$armor$utils$Armor()[armor.ordinal()]) {
                case 2: {
                    if (!isChainArmor(item.getType())) {
                        return false;
                    }
                    break;
                }
                case 3: {
                    if (!isGoldenArmor(item.getType())) {
                        return false;
                    }
                    break;
                }
                case 5: {
                    if (!isDiamondArmor(item.getType())) {
                        return false;
                    }
                    break;
                }
                case 4: {
                    if (!isIronArmor(item.getType())) {
                        return false;
                    }
                    break;
                }
                case 1: {
                    if (!isLeatherArmor(item.getType())) {
                        return false;
                    }
                    break;
                }
            }
        }
        return true;
    }
    
    public static int getIndexOfArmor(final Material material) {
        Preconditions.checkState(isArmor(material));
        if (isHelmet(material)) {
            return 3;
        }
        if (isChestPlate(material)) {
            return 2;
        }
        if (isLegging(material)) {
            return 1;
        }
        return 0;
    }
    
    public static Armor getArmor(final Material material) {
        Preconditions.checkState(isArmor(material));
        if (isLeatherArmor(material)) {
            return Armor.LEATHER;
        }
        if (isChainArmor(material)) {
            return Armor.CHAIN_MAIL;
        }
        if (isGoldenArmor(material)) {
            return Armor.GOLD;
        }
        if (isIronArmor(material)) {
            return Armor.IRON;
        }
        return Armor.DIAMOND;
    }
    
    public static boolean isArmorOfType(final Material material, final Armor armor) {
        Preconditions.checkState(isArmor(material));
        switch ($SWITCH_TABLE$net$libhalt$dev$plugin$armor$utils$Armor()[armor.ordinal()]) {
            case 2: {
                return isChainArmor(material);
            }
            case 5: {
                return isDiamondArmor(material);
            }
            case 3: {
                return isGoldenArmor(material);
            }
            case 4: {
                return isIronArmor(material);
            }
            case 1: {
                return isLeatherArmor(material);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    public static boolean isArmor(final Material material) {
        return isHelmet(material) || isChestPlate(material) || isLegging(material) || isBoot(material);
    }
    
    public static boolean isHelmet(final Material material) {
        switch ($SWITCH_TABLE$org$bukkit$Material()[material.ordinal()]) {
            case 215:
            case 219:
            case 223:
            case 227:
            case 231: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isChestPlate(final Material material) {
        switch ($SWITCH_TABLE$org$bukkit$Material()[material.ordinal()]) {
            case 216:
            case 220:
            case 224:
            case 228:
            case 232: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isLegging(final Material material) {
        switch ($SWITCH_TABLE$org$bukkit$Material()[material.ordinal()]) {
            case 217:
            case 221:
            case 225:
            case 229:
            case 233: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isBoot(final Material material) {
        switch ($SWITCH_TABLE$org$bukkit$Material()[material.ordinal()]) {
            case 218:
            case 222:
            case 226:
            case 230:
            case 234: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isGoldenArmor(final Material material) {
        switch ($SWITCH_TABLE$org$bukkit$Material()[material.ordinal()]) {
            case 231:
            case 232:
            case 233:
            case 234: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isChainArmor(final Material material) {
        switch ($SWITCH_TABLE$org$bukkit$Material()[material.ordinal()]) {
            case 219:
            case 220:
            case 221:
            case 222: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isIronArmor(final Material material) {
        switch ($SWITCH_TABLE$org$bukkit$Material()[material.ordinal()]) {
            case 223:
            case 224:
            case 225:
            case 226: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isDiamondArmor(final Material material) {
        switch ($SWITCH_TABLE$org$bukkit$Material()[material.ordinal()]) {
            case 227:
            case 228:
            case 229:
            case 230: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isLeatherArmor(final Material material) {
        switch ($SWITCH_TABLE$org$bukkit$Material()[material.ordinal()]) {
            case 215:
            case 216:
            case 217:
            case 218: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean canInteract(final Material material) {
        if (material == null || !material.isBlock()) {
            return false;
        }
        switch ($SWITCH_TABLE$org$bukkit$Material()[material.ordinal()]) {
            case 24:
            case 26:
            case 27:
            case 55:
            case 59:
            case 62:
            case 63:
            case 65:
            case 70:
            case 74:
            case 78:
            case 85:
            case 93:
            case 94:
            case 95:
            case 98:
            case 109:
            case 118:
            case 119:
            case 124:
            case 132:
            case 139:
            case 140:
            case 145:
            case 147:
            case 148:
            case 151:
            case 152:
            case 156:
            case 160: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    static int[] $SWITCH_TABLE$net$libhalt$dev$plugin$armor$utils$Armor() {
        final int[] $switch_TABLE$net$libhalt$dev$plugin$armor$utils$Armor = ItemStackUtils.$SWITCH_TABLE$net$libhalt$dev$plugin$armor$utils$Armor;
        if ($switch_TABLE$net$libhalt$dev$plugin$armor$utils$Armor != null) {
            return $switch_TABLE$net$libhalt$dev$plugin$armor$utils$Armor;
        }
        final int[] $switch_TABLE$net$libhalt$dev$plugin$armor$utils$Armor2 = new int[Armor.values().length];
        try {
            $switch_TABLE$net$libhalt$dev$plugin$armor$utils$Armor2[Armor.CHAIN_MAIL.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            $switch_TABLE$net$libhalt$dev$plugin$armor$utils$Armor2[Armor.DIAMOND.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError2) {}
        try {
            $switch_TABLE$net$libhalt$dev$plugin$armor$utils$Armor2[Armor.GOLD.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError3) {}
        try {
            $switch_TABLE$net$libhalt$dev$plugin$armor$utils$Armor2[Armor.IRON.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError4) {}
        try {
            $switch_TABLE$net$libhalt$dev$plugin$armor$utils$Armor2[Armor.LEATHER.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError5) {}
        return ItemStackUtils.$SWITCH_TABLE$net$libhalt$dev$plugin$armor$utils$Armor = $switch_TABLE$net$libhalt$dev$plugin$armor$utils$Armor2;
    }
    
    static int[] $SWITCH_TABLE$org$bukkit$Material() {
        final int[] $switch_TABLE$org$bukkit$Material = ItemStackUtils.$SWITCH_TABLE$org$bukkit$Material;
        if ($switch_TABLE$org$bukkit$Material != null) {
            return $switch_TABLE$org$bukkit$Material;
        }
        final int[] $switch_TABLE$org$bukkit$Material2 = new int[Material.values().length];
        try {
            $switch_TABLE$org$bukkit$Material2[Material.ACACIA_STAIRS.ordinal()] = 165;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.ACTIVATOR_RAIL.ordinal()] = 159;
        }
        catch (NoSuchFieldError noSuchFieldError2) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.AIR.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError3) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.ANVIL.ordinal()] = 147;
        }
        catch (NoSuchFieldError noSuchFieldError4) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.APPLE.ordinal()] = 177;
        }
        catch (NoSuchFieldError noSuchFieldError5) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.ARROW.ordinal()] = 179;
        }
        catch (NoSuchFieldError noSuchFieldError6) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BAKED_POTATO.ordinal()] = 310;
        }
        catch (NoSuchFieldError noSuchFieldError7) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BEACON.ordinal()] = 140;
        }
        catch (NoSuchFieldError noSuchFieldError8) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BED.ordinal()] = 272;
        }
        catch (NoSuchFieldError noSuchFieldError9) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BEDROCK.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError10) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BED_BLOCK.ordinal()] = 27;
        }
        catch (NoSuchFieldError noSuchFieldError11) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BIRCH_WOOD_STAIRS.ordinal()] = 137;
        }
        catch (NoSuchFieldError noSuchFieldError12) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BLAZE_POWDER.ordinal()] = 294;
        }
        catch (NoSuchFieldError noSuchFieldError13) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BLAZE_ROD.ordinal()] = 286;
        }
        catch (NoSuchFieldError noSuchFieldError14) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BOAT.ordinal()] = 250;
        }
        catch (NoSuchFieldError noSuchFieldError15) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BONE.ordinal()] = 269;
        }
        catch (NoSuchFieldError noSuchFieldError16) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BOOK.ordinal()] = 257;
        }
        catch (NoSuchFieldError noSuchFieldError17) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BOOKSHELF.ordinal()] = 48;
        }
        catch (NoSuchFieldError noSuchFieldError18) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BOOK_AND_QUILL.ordinal()] = 303;
        }
        catch (NoSuchFieldError noSuchFieldError19) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BOW.ordinal()] = 178;
        }
        catch (NoSuchFieldError noSuchFieldError20) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BOWL.ordinal()] = 198;
        }
        catch (NoSuchFieldError noSuchFieldError21) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BREAD.ordinal()] = 214;
        }
        catch (NoSuchFieldError noSuchFieldError22) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BREWING_STAND.ordinal()] = 119;
        }
        catch (NoSuchFieldError noSuchFieldError23) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BREWING_STAND_ITEM.ordinal()] = 296;
        }
        catch (NoSuchFieldError noSuchFieldError24) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BRICK.ordinal()] = 46;
        }
        catch (NoSuchFieldError noSuchFieldError25) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BRICK_STAIRS.ordinal()] = 110;
        }
        catch (NoSuchFieldError noSuchFieldError26) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BROWN_MUSHROOM.ordinal()] = 40;
        }
        catch (NoSuchFieldError noSuchFieldError27) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BUCKET.ordinal()] = 242;
        }
        catch (NoSuchFieldError noSuchFieldError28) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.BURNING_FURNACE.ordinal()] = 63;
        }
        catch (NoSuchFieldError noSuchFieldError29) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CACTUS.ordinal()] = 82;
        }
        catch (NoSuchFieldError noSuchFieldError30) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CAKE.ordinal()] = 271;
        }
        catch (NoSuchFieldError noSuchFieldError31) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CAKE_BLOCK.ordinal()] = 93;
        }
        catch (NoSuchFieldError noSuchFieldError32) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CARPET.ordinal()] = 168;
        }
        catch (NoSuchFieldError noSuchFieldError33) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CARROT.ordinal()] = 143;
        }
        catch (NoSuchFieldError noSuchFieldError34) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CARROT_ITEM.ordinal()] = 308;
        }
        catch (NoSuchFieldError noSuchFieldError35) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CARROT_STICK.ordinal()] = 315;
        }
        catch (NoSuchFieldError noSuchFieldError36) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CAULDRON.ordinal()] = 120;
        }
        catch (NoSuchFieldError noSuchFieldError37) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CAULDRON_ITEM.ordinal()] = 297;
        }
        catch (NoSuchFieldError noSuchFieldError38) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CHAINMAIL_BOOTS.ordinal()] = 222;
        }
        catch (NoSuchFieldError noSuchFieldError39) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CHAINMAIL_CHESTPLATE.ordinal()] = 220;
        }
        catch (NoSuchFieldError noSuchFieldError40) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CHAINMAIL_HELMET.ordinal()] = 219;
        }
        catch (NoSuchFieldError noSuchFieldError41) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CHAINMAIL_LEGGINGS.ordinal()] = 221;
        }
        catch (NoSuchFieldError noSuchFieldError42) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CHEST.ordinal()] = 55;
        }
        catch (NoSuchFieldError noSuchFieldError43) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CLAY.ordinal()] = 83;
        }
        catch (NoSuchFieldError noSuchFieldError44) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CLAY_BALL.ordinal()] = 254;
        }
        catch (NoSuchFieldError noSuchFieldError45) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CLAY_BRICK.ordinal()] = 253;
        }
        catch (NoSuchFieldError noSuchFieldError46) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.COAL.ordinal()] = 180;
        }
        catch (NoSuchFieldError noSuchFieldError47) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.COAL_BLOCK.ordinal()] = 170;
        }
        catch (NoSuchFieldError noSuchFieldError48) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.COAL_ORE.ordinal()] = 17;
        }
        catch (NoSuchFieldError noSuchFieldError49) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.COBBLESTONE.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError50) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.COBBLESTONE_STAIRS.ordinal()] = 68;
        }
        catch (NoSuchFieldError noSuchFieldError51) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.COBBLE_WALL.ordinal()] = 141;
        }
        catch (NoSuchFieldError noSuchFieldError52) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.COCOA.ordinal()] = 129;
        }
        catch (NoSuchFieldError noSuchFieldError53) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.COMMAND.ordinal()] = 139;
        }
        catch (NoSuchFieldError noSuchFieldError54) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.COMMAND_MINECART.ordinal()] = 331;
        }
        catch (NoSuchFieldError noSuchFieldError55) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.COMPASS.ordinal()] = 262;
        }
        catch (NoSuchFieldError noSuchFieldError56) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.COOKED_BEEF.ordinal()] = 281;
        }
        catch (NoSuchFieldError noSuchFieldError57) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.COOKED_CHICKEN.ordinal()] = 283;
        }
        catch (NoSuchFieldError noSuchFieldError58) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.COOKED_FISH.ordinal()] = 267;
        }
        catch (NoSuchFieldError noSuchFieldError59) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.COOKIE.ordinal()] = 274;
        }
        catch (NoSuchFieldError noSuchFieldError60) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.CROPS.ordinal()] = 60;
        }
        catch (NoSuchFieldError noSuchFieldError61) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DARK_OAK_STAIRS.ordinal()] = 166;
        }
        catch (NoSuchFieldError noSuchFieldError62) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DAYLIGHT_DETECTOR.ordinal()] = 153;
        }
        catch (NoSuchFieldError noSuchFieldError63) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DEAD_BUSH.ordinal()] = 33;
        }
        catch (NoSuchFieldError noSuchFieldError64) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DETECTOR_RAIL.ordinal()] = 29;
        }
        catch (NoSuchFieldError noSuchFieldError65) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIAMOND.ordinal()] = 181;
        }
        catch (NoSuchFieldError noSuchFieldError66) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIAMOND_AXE.ordinal()] = 196;
        }
        catch (NoSuchFieldError noSuchFieldError67) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIAMOND_BARDING.ordinal()] = 328;
        }
        catch (NoSuchFieldError noSuchFieldError68) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIAMOND_BLOCK.ordinal()] = 58;
        }
        catch (NoSuchFieldError noSuchFieldError69) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIAMOND_BOOTS.ordinal()] = 230;
        }
        catch (NoSuchFieldError noSuchFieldError70) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIAMOND_CHESTPLATE.ordinal()] = 228;
        }
        catch (NoSuchFieldError noSuchFieldError71) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIAMOND_HELMET.ordinal()] = 227;
        }
        catch (NoSuchFieldError noSuchFieldError72) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIAMOND_HOE.ordinal()] = 210;
        }
        catch (NoSuchFieldError noSuchFieldError73) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIAMOND_LEGGINGS.ordinal()] = 229;
        }
        catch (NoSuchFieldError noSuchFieldError74) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIAMOND_ORE.ordinal()] = 57;
        }
        catch (NoSuchFieldError noSuchFieldError75) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIAMOND_PICKAXE.ordinal()] = 195;
        }
        catch (NoSuchFieldError noSuchFieldError76) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIAMOND_SPADE.ordinal()] = 194;
        }
        catch (NoSuchFieldError noSuchFieldError77) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIAMOND_SWORD.ordinal()] = 193;
        }
        catch (NoSuchFieldError noSuchFieldError78) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIODE.ordinal()] = 273;
        }
        catch (NoSuchFieldError noSuchFieldError79) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIODE_BLOCK_OFF.ordinal()] = 94;
        }
        catch (NoSuchFieldError noSuchFieldError80) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIODE_BLOCK_ON.ordinal()] = 95;
        }
        catch (NoSuchFieldError noSuchFieldError81) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DIRT.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError82) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DISPENSER.ordinal()] = 24;
        }
        catch (NoSuchFieldError noSuchFieldError83) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DOUBLE_PLANT.ordinal()] = 172;
        }
        catch (NoSuchFieldError noSuchFieldError84) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DOUBLE_STEP.ordinal()] = 44;
        }
        catch (NoSuchFieldError noSuchFieldError85) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DRAGON_EGG.ordinal()] = 124;
        }
        catch (NoSuchFieldError noSuchFieldError86) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.DROPPER.ordinal()] = 160;
        }
        catch (NoSuchFieldError noSuchFieldError87) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.EGG.ordinal()] = 261;
        }
        catch (NoSuchFieldError noSuchFieldError88) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.EMERALD.ordinal()] = 305;
        }
        catch (NoSuchFieldError noSuchFieldError89) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.EMERALD_BLOCK.ordinal()] = 135;
        }
        catch (NoSuchFieldError noSuchFieldError90) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.EMERALD_ORE.ordinal()] = 131;
        }
        catch (NoSuchFieldError noSuchFieldError91) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.EMPTY_MAP.ordinal()] = 312;
        }
        catch (NoSuchFieldError noSuchFieldError92) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.ENCHANTED_BOOK.ordinal()] = 320;
        }
        catch (NoSuchFieldError noSuchFieldError93) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.ENCHANTMENT_TABLE.ordinal()] = 118;
        }
        catch (NoSuchFieldError noSuchFieldError94) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.ENDER_CHEST.ordinal()] = 132;
        }
        catch (NoSuchFieldError noSuchFieldError95) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.ENDER_PEARL.ordinal()] = 285;
        }
        catch (NoSuchFieldError noSuchFieldError96) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.ENDER_PORTAL.ordinal()] = 121;
        }
        catch (NoSuchFieldError noSuchFieldError97) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.ENDER_PORTAL_FRAME.ordinal()] = 122;
        }
        catch (NoSuchFieldError noSuchFieldError98) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.ENDER_STONE.ordinal()] = 123;
        }
        catch (NoSuchFieldError noSuchFieldError99) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.EXPLOSIVE_MINECART.ordinal()] = 324;
        }
        catch (NoSuchFieldError noSuchFieldError100) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.EXP_BOTTLE.ordinal()] = 301;
        }
        catch (NoSuchFieldError noSuchFieldError101) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.EYE_OF_ENDER.ordinal()] = 298;
        }
        catch (NoSuchFieldError noSuchFieldError102) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.FEATHER.ordinal()] = 205;
        }
        catch (NoSuchFieldError noSuchFieldError103) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.FENCE.ordinal()] = 86;
        }
        catch (NoSuchFieldError noSuchFieldError104) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.FENCE_GATE.ordinal()] = 109;
        }
        catch (NoSuchFieldError noSuchFieldError105) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.FERMENTED_SPIDER_EYE.ordinal()] = 293;
        }
        catch (NoSuchFieldError noSuchFieldError106) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.FIRE.ordinal()] = 52;
        }
        catch (NoSuchFieldError noSuchFieldError107) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.FIREBALL.ordinal()] = 302;
        }
        catch (NoSuchFieldError noSuchFieldError108) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.FIREWORK.ordinal()] = 318;
        }
        catch (NoSuchFieldError noSuchFieldError109) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.FIREWORK_CHARGE.ordinal()] = 319;
        }
        catch (NoSuchFieldError noSuchFieldError110) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.FISHING_ROD.ordinal()] = 263;
        }
        catch (NoSuchFieldError noSuchFieldError111) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.FLINT.ordinal()] = 235;
        }
        catch (NoSuchFieldError noSuchFieldError112) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.FLINT_AND_STEEL.ordinal()] = 176;
        }
        catch (NoSuchFieldError noSuchFieldError113) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.FLOWER_POT.ordinal()] = 142;
        }
        catch (NoSuchFieldError noSuchFieldError114) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.FLOWER_POT_ITEM.ordinal()] = 307;
        }
        catch (NoSuchFieldError noSuchFieldError115) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.FURNACE.ordinal()] = 62;
        }
        catch (NoSuchFieldError noSuchFieldError116) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GHAST_TEAR.ordinal()] = 287;
        }
        catch (NoSuchFieldError noSuchFieldError117) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GLASS.ordinal()] = 21;
        }
        catch (NoSuchFieldError noSuchFieldError118) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GLASS_BOTTLE.ordinal()] = 291;
        }
        catch (NoSuchFieldError noSuchFieldError119) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GLOWING_REDSTONE_ORE.ordinal()] = 75;
        }
        catch (NoSuchFieldError noSuchFieldError120) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GLOWSTONE.ordinal()] = 90;
        }
        catch (NoSuchFieldError noSuchFieldError121) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GLOWSTONE_DUST.ordinal()] = 265;
        }
        catch (NoSuchFieldError noSuchFieldError122) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLDEN_APPLE.ordinal()] = 239;
        }
        catch (NoSuchFieldError noSuchFieldError123) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLDEN_CARROT.ordinal()] = 313;
        }
        catch (NoSuchFieldError noSuchFieldError124) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_AXE.ordinal()] = 203;
        }
        catch (NoSuchFieldError noSuchFieldError125) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_BARDING.ordinal()] = 327;
        }
        catch (NoSuchFieldError noSuchFieldError126) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_BLOCK.ordinal()] = 42;
        }
        catch (NoSuchFieldError noSuchFieldError127) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_BOOTS.ordinal()] = 234;
        }
        catch (NoSuchFieldError noSuchFieldError128) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_CHESTPLATE.ordinal()] = 232;
        }
        catch (NoSuchFieldError noSuchFieldError129) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_HELMET.ordinal()] = 231;
        }
        catch (NoSuchFieldError noSuchFieldError130) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_HOE.ordinal()] = 211;
        }
        catch (NoSuchFieldError noSuchFieldError131) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_INGOT.ordinal()] = 183;
        }
        catch (NoSuchFieldError noSuchFieldError132) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_LEGGINGS.ordinal()] = 233;
        }
        catch (NoSuchFieldError noSuchFieldError133) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_NUGGET.ordinal()] = 288;
        }
        catch (NoSuchFieldError noSuchFieldError134) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_ORE.ordinal()] = 15;
        }
        catch (NoSuchFieldError noSuchFieldError135) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_PICKAXE.ordinal()] = 202;
        }
        catch (NoSuchFieldError noSuchFieldError136) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_PLATE.ordinal()] = 149;
        }
        catch (NoSuchFieldError noSuchFieldError137) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_RECORD.ordinal()] = 332;
        }
        catch (NoSuchFieldError noSuchFieldError138) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_SPADE.ordinal()] = 201;
        }
        catch (NoSuchFieldError noSuchFieldError139) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GOLD_SWORD.ordinal()] = 200;
        }
        catch (NoSuchFieldError noSuchFieldError140) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GRASS.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError141) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GRAVEL.ordinal()] = 14;
        }
        catch (NoSuchFieldError noSuchFieldError142) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GREEN_RECORD.ordinal()] = 333;
        }
        catch (NoSuchFieldError noSuchFieldError143) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.GRILLED_PORK.ordinal()] = 237;
        }
        catch (NoSuchFieldError noSuchFieldError144) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.HARD_CLAY.ordinal()] = 169;
        }
        catch (NoSuchFieldError noSuchFieldError145) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.HAY_BLOCK.ordinal()] = 167;
        }
        catch (NoSuchFieldError noSuchFieldError146) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.HOPPER.ordinal()] = 156;
        }
        catch (NoSuchFieldError noSuchFieldError147) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.HOPPER_MINECART.ordinal()] = 325;
        }
        catch (NoSuchFieldError noSuchFieldError148) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.HUGE_MUSHROOM_1.ordinal()] = 101;
        }
        catch (NoSuchFieldError noSuchFieldError149) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.HUGE_MUSHROOM_2.ordinal()] = 102;
        }
        catch (NoSuchFieldError noSuchFieldError150) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.ICE.ordinal()] = 80;
        }
        catch (NoSuchFieldError noSuchFieldError151) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.INK_SACK.ordinal()] = 268;
        }
        catch (NoSuchFieldError noSuchFieldError152) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_AXE.ordinal()] = 175;
        }
        catch (NoSuchFieldError noSuchFieldError153) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_BARDING.ordinal()] = 326;
        }
        catch (NoSuchFieldError noSuchFieldError154) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_BLOCK.ordinal()] = 43;
        }
        catch (NoSuchFieldError noSuchFieldError155) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_BOOTS.ordinal()] = 226;
        }
        catch (NoSuchFieldError noSuchFieldError156) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_CHESTPLATE.ordinal()] = 224;
        }
        catch (NoSuchFieldError noSuchFieldError157) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_DOOR.ordinal()] = 247;
        }
        catch (NoSuchFieldError noSuchFieldError158) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_DOOR_BLOCK.ordinal()] = 72;
        }
        catch (NoSuchFieldError noSuchFieldError159) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_FENCE.ordinal()] = 103;
        }
        catch (NoSuchFieldError noSuchFieldError160) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_HELMET.ordinal()] = 223;
        }
        catch (NoSuchFieldError noSuchFieldError161) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_HOE.ordinal()] = 209;
        }
        catch (NoSuchFieldError noSuchFieldError162) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_INGOT.ordinal()] = 182;
        }
        catch (NoSuchFieldError noSuchFieldError163) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_LEGGINGS.ordinal()] = 225;
        }
        catch (NoSuchFieldError noSuchFieldError164) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_ORE.ordinal()] = 16;
        }
        catch (NoSuchFieldError noSuchFieldError165) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_PICKAXE.ordinal()] = 174;
        }
        catch (NoSuchFieldError noSuchFieldError166) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_PLATE.ordinal()] = 150;
        }
        catch (NoSuchFieldError noSuchFieldError167) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_SPADE.ordinal()] = 173;
        }
        catch (NoSuchFieldError noSuchFieldError168) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.IRON_SWORD.ordinal()] = 184;
        }
        catch (NoSuchFieldError noSuchFieldError169) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.ITEM_FRAME.ordinal()] = 306;
        }
        catch (NoSuchFieldError noSuchFieldError170) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.JACK_O_LANTERN.ordinal()] = 92;
        }
        catch (NoSuchFieldError noSuchFieldError171) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.JUKEBOX.ordinal()] = 85;
        }
        catch (NoSuchFieldError noSuchFieldError172) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.JUNGLE_WOOD_STAIRS.ordinal()] = 138;
        }
        catch (NoSuchFieldError noSuchFieldError173) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LADDER.ordinal()] = 66;
        }
        catch (NoSuchFieldError noSuchFieldError174) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LAPIS_BLOCK.ordinal()] = 23;
        }
        catch (NoSuchFieldError noSuchFieldError175) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LAPIS_ORE.ordinal()] = 22;
        }
        catch (NoSuchFieldError noSuchFieldError176) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LAVA.ordinal()] = 11;
        }
        catch (NoSuchFieldError noSuchFieldError177) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LAVA_BUCKET.ordinal()] = 244;
        }
        catch (NoSuchFieldError noSuchFieldError178) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LEASH.ordinal()] = 329;
        }
        catch (NoSuchFieldError noSuchFieldError179) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LEATHER.ordinal()] = 251;
        }
        catch (NoSuchFieldError noSuchFieldError180) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LEATHER_BOOTS.ordinal()] = 218;
        }
        catch (NoSuchFieldError noSuchFieldError181) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LEATHER_CHESTPLATE.ordinal()] = 216;
        }
        catch (NoSuchFieldError noSuchFieldError182) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LEATHER_HELMET.ordinal()] = 215;
        }
        catch (NoSuchFieldError noSuchFieldError183) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LEATHER_LEGGINGS.ordinal()] = 217;
        }
        catch (NoSuchFieldError noSuchFieldError184) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LEAVES.ordinal()] = 19;
        }
        catch (NoSuchFieldError noSuchFieldError185) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LEAVES_2.ordinal()] = 163;
        }
        catch (NoSuchFieldError noSuchFieldError186) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LEVER.ordinal()] = 70;
        }
        catch (NoSuchFieldError noSuchFieldError187) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LOCKED_CHEST.ordinal()] = 96;
        }
        catch (NoSuchFieldError noSuchFieldError188) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LOG.ordinal()] = 18;
        }
        catch (NoSuchFieldError noSuchFieldError189) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LOG_2.ordinal()] = 164;
        }
        catch (NoSuchFieldError noSuchFieldError190) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.LONG_GRASS.ordinal()] = 32;
        }
        catch (NoSuchFieldError noSuchFieldError191) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.MAGMA_CREAM.ordinal()] = 295;
        }
        catch (NoSuchFieldError noSuchFieldError192) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.MAP.ordinal()] = 275;
        }
        catch (NoSuchFieldError noSuchFieldError193) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.MELON.ordinal()] = 277;
        }
        catch (NoSuchFieldError noSuchFieldError194) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.MELON_BLOCK.ordinal()] = 105;
        }
        catch (NoSuchFieldError noSuchFieldError195) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.MELON_SEEDS.ordinal()] = 279;
        }
        catch (NoSuchFieldError noSuchFieldError196) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.MELON_STEM.ordinal()] = 107;
        }
        catch (NoSuchFieldError noSuchFieldError197) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.MILK_BUCKET.ordinal()] = 252;
        }
        catch (NoSuchFieldError noSuchFieldError198) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.MINECART.ordinal()] = 245;
        }
        catch (NoSuchFieldError noSuchFieldError199) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.MOB_SPAWNER.ordinal()] = 53;
        }
        catch (NoSuchFieldError noSuchFieldError200) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.MONSTER_EGG.ordinal()] = 300;
        }
        catch (NoSuchFieldError noSuchFieldError201) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.MONSTER_EGGS.ordinal()] = 99;
        }
        catch (NoSuchFieldError noSuchFieldError202) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.MOSSY_COBBLESTONE.ordinal()] = 49;
        }
        catch (NoSuchFieldError noSuchFieldError203) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.MUSHROOM_SOUP.ordinal()] = 199;
        }
        catch (NoSuchFieldError noSuchFieldError204) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.MYCEL.ordinal()] = 112;
        }
        catch (NoSuchFieldError noSuchFieldError205) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.NAME_TAG.ordinal()] = 330;
        }
        catch (NoSuchFieldError noSuchFieldError206) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.NETHERRACK.ordinal()] = 88;
        }
        catch (NoSuchFieldError noSuchFieldError207) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.NETHER_BRICK.ordinal()] = 114;
        }
        catch (NoSuchFieldError noSuchFieldError208) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.NETHER_BRICK_ITEM.ordinal()] = 322;
        }
        catch (NoSuchFieldError noSuchFieldError209) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.NETHER_BRICK_STAIRS.ordinal()] = 116;
        }
        catch (NoSuchFieldError noSuchFieldError210) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.NETHER_FENCE.ordinal()] = 115;
        }
        catch (NoSuchFieldError noSuchFieldError211) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.NETHER_STALK.ordinal()] = 289;
        }
        catch (NoSuchFieldError noSuchFieldError212) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.NETHER_STAR.ordinal()] = 316;
        }
        catch (NoSuchFieldError noSuchFieldError213) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.NETHER_WARTS.ordinal()] = 117;
        }
        catch (NoSuchFieldError noSuchFieldError214) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.NOTE_BLOCK.ordinal()] = 26;
        }
        catch (NoSuchFieldError noSuchFieldError215) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.OBSIDIAN.ordinal()] = 50;
        }
        catch (NoSuchFieldError noSuchFieldError216) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.PACKED_ICE.ordinal()] = 171;
        }
        catch (NoSuchFieldError noSuchFieldError217) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.PAINTING.ordinal()] = 238;
        }
        catch (NoSuchFieldError noSuchFieldError218) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.PAPER.ordinal()] = 256;
        }
        catch (NoSuchFieldError noSuchFieldError219) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.PISTON_BASE.ordinal()] = 34;
        }
        catch (NoSuchFieldError noSuchFieldError220) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.PISTON_EXTENSION.ordinal()] = 35;
        }
        catch (NoSuchFieldError noSuchFieldError221) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.PISTON_MOVING_PIECE.ordinal()] = 37;
        }
        catch (NoSuchFieldError noSuchFieldError222) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.PISTON_STICKY_BASE.ordinal()] = 30;
        }
        catch (NoSuchFieldError noSuchFieldError223) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.POISONOUS_POTATO.ordinal()] = 311;
        }
        catch (NoSuchFieldError noSuchFieldError224) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.PORK.ordinal()] = 236;
        }
        catch (NoSuchFieldError noSuchFieldError225) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.PORTAL.ordinal()] = 91;
        }
        catch (NoSuchFieldError noSuchFieldError226) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.POTATO.ordinal()] = 144;
        }
        catch (NoSuchFieldError noSuchFieldError227) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.POTATO_ITEM.ordinal()] = 309;
        }
        catch (NoSuchFieldError noSuchFieldError228) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.POTION.ordinal()] = 290;
        }
        catch (NoSuchFieldError noSuchFieldError229) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.POWERED_MINECART.ordinal()] = 260;
        }
        catch (NoSuchFieldError noSuchFieldError230) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.POWERED_RAIL.ordinal()] = 28;
        }
        catch (NoSuchFieldError noSuchFieldError231) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.PUMPKIN.ordinal()] = 87;
        }
        catch (NoSuchFieldError noSuchFieldError232) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.PUMPKIN_PIE.ordinal()] = 317;
        }
        catch (NoSuchFieldError noSuchFieldError233) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.PUMPKIN_SEEDS.ordinal()] = 278;
        }
        catch (NoSuchFieldError noSuchFieldError234) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.PUMPKIN_STEM.ordinal()] = 106;
        }
        catch (NoSuchFieldError noSuchFieldError235) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.QUARTZ.ordinal()] = 323;
        }
        catch (NoSuchFieldError noSuchFieldError236) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.QUARTZ_BLOCK.ordinal()] = 157;
        }
        catch (NoSuchFieldError noSuchFieldError237) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.QUARTZ_ORE.ordinal()] = 155;
        }
        catch (NoSuchFieldError noSuchFieldError238) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.QUARTZ_STAIRS.ordinal()] = 158;
        }
        catch (NoSuchFieldError noSuchFieldError239) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RAILS.ordinal()] = 67;
        }
        catch (NoSuchFieldError noSuchFieldError240) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RAW_BEEF.ordinal()] = 280;
        }
        catch (NoSuchFieldError noSuchFieldError241) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RAW_CHICKEN.ordinal()] = 282;
        }
        catch (NoSuchFieldError noSuchFieldError242) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RAW_FISH.ordinal()] = 266;
        }
        catch (NoSuchFieldError noSuchFieldError243) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RECORD_10.ordinal()] = 341;
        }
        catch (NoSuchFieldError noSuchFieldError244) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RECORD_11.ordinal()] = 342;
        }
        catch (NoSuchFieldError noSuchFieldError245) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RECORD_12.ordinal()] = 343;
        }
        catch (NoSuchFieldError noSuchFieldError246) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RECORD_3.ordinal()] = 334;
        }
        catch (NoSuchFieldError noSuchFieldError247) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RECORD_4.ordinal()] = 335;
        }
        catch (NoSuchFieldError noSuchFieldError248) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RECORD_5.ordinal()] = 336;
        }
        catch (NoSuchFieldError noSuchFieldError249) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RECORD_6.ordinal()] = 337;
        }
        catch (NoSuchFieldError noSuchFieldError250) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RECORD_7.ordinal()] = 338;
        }
        catch (NoSuchFieldError noSuchFieldError251) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RECORD_8.ordinal()] = 339;
        }
        catch (NoSuchFieldError noSuchFieldError252) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RECORD_9.ordinal()] = 340;
        }
        catch (NoSuchFieldError noSuchFieldError253) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.REDSTONE.ordinal()] = 248;
        }
        catch (NoSuchFieldError noSuchFieldError254) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.REDSTONE_BLOCK.ordinal()] = 154;
        }
        catch (NoSuchFieldError noSuchFieldError255) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.REDSTONE_COMPARATOR.ordinal()] = 321;
        }
        catch (NoSuchFieldError noSuchFieldError256) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.REDSTONE_COMPARATOR_OFF.ordinal()] = 151;
        }
        catch (NoSuchFieldError noSuchFieldError257) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.REDSTONE_COMPARATOR_ON.ordinal()] = 152;
        }
        catch (NoSuchFieldError noSuchFieldError258) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.REDSTONE_LAMP_OFF.ordinal()] = 125;
        }
        catch (NoSuchFieldError noSuchFieldError259) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.REDSTONE_LAMP_ON.ordinal()] = 126;
        }
        catch (NoSuchFieldError noSuchFieldError260) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.REDSTONE_ORE.ordinal()] = 74;
        }
        catch (NoSuchFieldError noSuchFieldError261) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.REDSTONE_TORCH_OFF.ordinal()] = 76;
        }
        catch (NoSuchFieldError noSuchFieldError262) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.REDSTONE_TORCH_ON.ordinal()] = 77;
        }
        catch (NoSuchFieldError noSuchFieldError263) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.REDSTONE_WIRE.ordinal()] = 56;
        }
        catch (NoSuchFieldError noSuchFieldError264) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RED_MUSHROOM.ordinal()] = 41;
        }
        catch (NoSuchFieldError noSuchFieldError265) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.RED_ROSE.ordinal()] = 39;
        }
        catch (NoSuchFieldError noSuchFieldError266) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.ROTTEN_FLESH.ordinal()] = 284;
        }
        catch (NoSuchFieldError noSuchFieldError267) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SADDLE.ordinal()] = 246;
        }
        catch (NoSuchFieldError noSuchFieldError268) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SAND.ordinal()] = 13;
        }
        catch (NoSuchFieldError noSuchFieldError269) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SANDSTONE.ordinal()] = 25;
        }
        catch (NoSuchFieldError noSuchFieldError270) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SANDSTONE_STAIRS.ordinal()] = 130;
        }
        catch (NoSuchFieldError noSuchFieldError271) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SAPLING.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError272) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SEEDS.ordinal()] = 212;
        }
        catch (NoSuchFieldError noSuchFieldError273) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SHEARS.ordinal()] = 276;
        }
        catch (NoSuchFieldError noSuchFieldError274) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SIGN.ordinal()] = 240;
        }
        catch (NoSuchFieldError noSuchFieldError275) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SIGN_POST.ordinal()] = 64;
        }
        catch (NoSuchFieldError noSuchFieldError276) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SKULL.ordinal()] = 146;
        }
        catch (NoSuchFieldError noSuchFieldError277) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SKULL_ITEM.ordinal()] = 314;
        }
        catch (NoSuchFieldError noSuchFieldError278) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SLIME_BALL.ordinal()] = 258;
        }
        catch (NoSuchFieldError noSuchFieldError279) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SMOOTH_BRICK.ordinal()] = 100;
        }
        catch (NoSuchFieldError noSuchFieldError280) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SMOOTH_STAIRS.ordinal()] = 111;
        }
        catch (NoSuchFieldError noSuchFieldError281) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SNOW.ordinal()] = 79;
        }
        catch (NoSuchFieldError noSuchFieldError282) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SNOW_BALL.ordinal()] = 249;
        }
        catch (NoSuchFieldError noSuchFieldError283) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SNOW_BLOCK.ordinal()] = 81;
        }
        catch (NoSuchFieldError noSuchFieldError284) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SOIL.ordinal()] = 61;
        }
        catch (NoSuchFieldError noSuchFieldError285) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SOUL_SAND.ordinal()] = 89;
        }
        catch (NoSuchFieldError noSuchFieldError286) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SPECKLED_MELON.ordinal()] = 299;
        }
        catch (NoSuchFieldError noSuchFieldError287) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SPIDER_EYE.ordinal()] = 292;
        }
        catch (NoSuchFieldError noSuchFieldError288) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SPONGE.ordinal()] = 20;
        }
        catch (NoSuchFieldError noSuchFieldError289) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SPRUCE_WOOD_STAIRS.ordinal()] = 136;
        }
        catch (NoSuchFieldError noSuchFieldError290) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STAINED_CLAY.ordinal()] = 161;
        }
        catch (NoSuchFieldError noSuchFieldError291) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STAINED_GLASS.ordinal()] = 97;
        }
        catch (NoSuchFieldError noSuchFieldError292) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STAINED_GLASS_PANE.ordinal()] = 162;
        }
        catch (NoSuchFieldError noSuchFieldError293) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STATIONARY_LAVA.ordinal()] = 12;
        }
        catch (NoSuchFieldError noSuchFieldError294) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STATIONARY_WATER.ordinal()] = 10;
        }
        catch (NoSuchFieldError noSuchFieldError295) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STEP.ordinal()] = 45;
        }
        catch (NoSuchFieldError noSuchFieldError296) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STICK.ordinal()] = 197;
        }
        catch (NoSuchFieldError noSuchFieldError297) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STONE.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError298) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STONE_AXE.ordinal()] = 192;
        }
        catch (NoSuchFieldError noSuchFieldError299) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STONE_BUTTON.ordinal()] = 78;
        }
        catch (NoSuchFieldError noSuchFieldError300) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STONE_HOE.ordinal()] = 208;
        }
        catch (NoSuchFieldError noSuchFieldError301) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STONE_PICKAXE.ordinal()] = 191;
        }
        catch (NoSuchFieldError noSuchFieldError302) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STONE_PLATE.ordinal()] = 71;
        }
        catch (NoSuchFieldError noSuchFieldError303) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STONE_SPADE.ordinal()] = 190;
        }
        catch (NoSuchFieldError noSuchFieldError304) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STONE_SWORD.ordinal()] = 189;
        }
        catch (NoSuchFieldError noSuchFieldError305) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STORAGE_MINECART.ordinal()] = 259;
        }
        catch (NoSuchFieldError noSuchFieldError306) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.STRING.ordinal()] = 204;
        }
        catch (NoSuchFieldError noSuchFieldError307) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SUGAR.ordinal()] = 270;
        }
        catch (NoSuchFieldError noSuchFieldError308) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SUGAR_CANE.ordinal()] = 255;
        }
        catch (NoSuchFieldError noSuchFieldError309) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SUGAR_CANE_BLOCK.ordinal()] = 84;
        }
        catch (NoSuchFieldError noSuchFieldError310) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.SULPHUR.ordinal()] = 206;
        }
        catch (NoSuchFieldError noSuchFieldError311) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.THIN_GLASS.ordinal()] = 104;
        }
        catch (NoSuchFieldError noSuchFieldError312) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.TNT.ordinal()] = 47;
        }
        catch (NoSuchFieldError noSuchFieldError313) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.TORCH.ordinal()] = 51;
        }
        catch (NoSuchFieldError noSuchFieldError314) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.TRAPPED_CHEST.ordinal()] = 148;
        }
        catch (NoSuchFieldError noSuchFieldError315) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.TRAP_DOOR.ordinal()] = 98;
        }
        catch (NoSuchFieldError noSuchFieldError316) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.TRIPWIRE.ordinal()] = 134;
        }
        catch (NoSuchFieldError noSuchFieldError317) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.TRIPWIRE_HOOK.ordinal()] = 133;
        }
        catch (NoSuchFieldError noSuchFieldError318) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.VINE.ordinal()] = 108;
        }
        catch (NoSuchFieldError noSuchFieldError319) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WALL_SIGN.ordinal()] = 69;
        }
        catch (NoSuchFieldError noSuchFieldError320) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WATCH.ordinal()] = 264;
        }
        catch (NoSuchFieldError noSuchFieldError321) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WATER.ordinal()] = 9;
        }
        catch (NoSuchFieldError noSuchFieldError322) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WATER_BUCKET.ordinal()] = 243;
        }
        catch (NoSuchFieldError noSuchFieldError323) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WATER_LILY.ordinal()] = 113;
        }
        catch (NoSuchFieldError noSuchFieldError324) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WEB.ordinal()] = 31;
        }
        catch (NoSuchFieldError noSuchFieldError325) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WHEAT.ordinal()] = 213;
        }
        catch (NoSuchFieldError noSuchFieldError326) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WOOD.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError327) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WOODEN_DOOR.ordinal()] = 65;
        }
        catch (NoSuchFieldError noSuchFieldError328) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WOOD_AXE.ordinal()] = 188;
        }
        catch (NoSuchFieldError noSuchFieldError329) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WOOD_BUTTON.ordinal()] = 145;
        }
        catch (NoSuchFieldError noSuchFieldError330) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WOOD_DOOR.ordinal()] = 241;
        }
        catch (NoSuchFieldError noSuchFieldError331) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WOOD_DOUBLE_STEP.ordinal()] = 127;
        }
        catch (NoSuchFieldError noSuchFieldError332) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WOOD_HOE.ordinal()] = 207;
        }
        catch (NoSuchFieldError noSuchFieldError333) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WOOD_PICKAXE.ordinal()] = 187;
        }
        catch (NoSuchFieldError noSuchFieldError334) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WOOD_PLATE.ordinal()] = 73;
        }
        catch (NoSuchFieldError noSuchFieldError335) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WOOD_SPADE.ordinal()] = 186;
        }
        catch (NoSuchFieldError noSuchFieldError336) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WOOD_STAIRS.ordinal()] = 54;
        }
        catch (NoSuchFieldError noSuchFieldError337) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WOOD_STEP.ordinal()] = 128;
        }
        catch (NoSuchFieldError noSuchFieldError338) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WOOD_SWORD.ordinal()] = 185;
        }
        catch (NoSuchFieldError noSuchFieldError339) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WOOL.ordinal()] = 36;
        }
        catch (NoSuchFieldError noSuchFieldError340) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WORKBENCH.ordinal()] = 59;
        }
        catch (NoSuchFieldError noSuchFieldError341) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.WRITTEN_BOOK.ordinal()] = 304;
        }
        catch (NoSuchFieldError noSuchFieldError342) {}
        try {
            $switch_TABLE$org$bukkit$Material2[Material.YELLOW_FLOWER.ordinal()] = 38;
        }
        catch (NoSuchFieldError noSuchFieldError343) {}
        return ItemStackUtils.$SWITCH_TABLE$org$bukkit$Material = $switch_TABLE$org$bukkit$Material2;
    }
}
