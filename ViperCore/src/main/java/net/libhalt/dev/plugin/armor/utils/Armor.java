package net.libhalt.dev.plugin.armor.utils;

public enum Armor
{
    LEATHER, 
    CHAIN_MAIL, 
    GOLD, 
    IRON, 
    DIAMOND;
    
    private static int[] $SWITCH_TABLE$net$libhalt$dev$plugin$armor$utils$Armor;
    
    public String toKit() {
        switch ($SWITCH_TABLE$net$libhalt$dev$plugin$armor$utils$Armor()[this.ordinal()]) {
            case 1: {
                return "Archer";
            }
            case 2: {
                return "Rogue";
            }
            case 5: {
                return "Diamond";
            }
            case 3: {
                return "Bard";
            }
            case 4: {
                return "Miner";
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    public static Armor fromKit(String kit) {
        kit = kit.toLowerCase();
        if (kit.equals("archer")) {
            return Armor.LEATHER;
        }
        if (kit.equals("rogue")) {
            return Armor.CHAIN_MAIL;
        }
        if (kit.equals("diamond")) {
            return Armor.DIAMOND;
        }
        if (kit.equals("bard")) {
            return Armor.GOLD;
        }
        if (kit.equals("miner")) {
            return Armor.IRON;
        }
        return null;
    }
    
    static int[] $SWITCH_TABLE$net$libhalt$dev$plugin$armor$utils$Armor() {
        final int[] $switch_TABLE$net$libhalt$dev$plugin$armor$utils$Armor = Armor.$SWITCH_TABLE$net$libhalt$dev$plugin$armor$utils$Armor;
        if ($switch_TABLE$net$libhalt$dev$plugin$armor$utils$Armor != null) {
            return $switch_TABLE$net$libhalt$dev$plugin$armor$utils$Armor;
        }
        final int[] $switch_TABLE$net$libhalt$dev$plugin$armor$utils$Armor2 = new int[values().length];
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
        return Armor.$SWITCH_TABLE$net$libhalt$dev$plugin$armor$utils$Armor = $switch_TABLE$net$libhalt$dev$plugin$armor$utils$Armor2;
    }
}
