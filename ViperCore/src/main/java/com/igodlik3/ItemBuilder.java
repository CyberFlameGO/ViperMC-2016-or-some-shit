package com.igodlik3;

import org.bukkit.inventory.*;
import org.bukkit.material.*;
import org.bukkit.enchantments.*;
import java.util.*;
import org.apache.commons.lang.*;
import org.bukkit.configuration.file.*;
import org.bukkit.inventory.meta.*;
import net.minecraft.util.com.google.gson.*;
import org.bukkit.*;
import java.lang.reflect.*;

public class ItemBuilder
{
    private ItemStack item;
    private ItemMeta meta;
    private Material material;
    private int amount;
    private MaterialData data;
    private short damage;
    private Map<Enchantment, Integer> enchantments;
    private String displayname;
    private List<String> lore;
    private boolean andSymbol;
    private boolean unsafeStackSize;
    
    public ItemBuilder(Material material) {
        this.material = Material.STONE;
        this.amount = 1;
        this.damage = 0;
        this.enchantments = new HashMap<Enchantment, Integer>();
        this.lore = new ArrayList<String>();
        this.andSymbol = true;
        this.unsafeStackSize = false;
        if (material == null) {
            material = Material.AIR;
        }
        this.item = new ItemStack(material);
        this.material = material;
    }
    
    public ItemBuilder(Material material, int amount) {
        this.material = Material.STONE;
        this.amount = 1;
        this.damage = 0;
        this.enchantments = new HashMap<Enchantment, Integer>();
        this.lore = new ArrayList<String>();
        this.andSymbol = true;
        this.unsafeStackSize = false;
        if (material == null) {
            material = Material.AIR;
        }
        if ((amount > material.getMaxStackSize() || amount <= 0) && !this.unsafeStackSize) {
            amount = 1;
        }
        this.amount = amount;
        this.item = new ItemStack(material, amount);
        this.material = material;
    }
    
    public ItemBuilder(Material material, int amount, final String displayname) {
        this.material = Material.STONE;
        this.amount = 1;
        this.damage = 0;
        this.enchantments = new HashMap<Enchantment, Integer>();
        this.lore = new ArrayList<String>();
        this.andSymbol = true;
        this.unsafeStackSize = false;
        if (material == null) {
            material = Material.AIR;
        }
        Validate.notNull((Object)displayname, "The Displayname is null.");
        this.item = new ItemStack(material, amount);
        this.material = material;
        if ((amount > material.getMaxStackSize() || amount <= 0) && !this.unsafeStackSize) {
            amount = 1;
        }
        this.amount = amount;
        this.displayname = displayname;
    }
    
    public ItemBuilder(Material material, final String displayname) {
        this.material = Material.STONE;
        this.amount = 1;
        this.damage = 0;
        this.enchantments = new HashMap<Enchantment, Integer>();
        this.lore = new ArrayList<String>();
        this.andSymbol = true;
        this.unsafeStackSize = false;
        if (material == null) {
            material = Material.AIR;
        }
        Validate.notNull((Object)displayname, "The Displayname is null.");
        this.item = new ItemStack(material);
        this.material = material;
        this.displayname = displayname;
    }
    
    public ItemBuilder(final ItemStack item) {
        this.material = Material.STONE;
        this.amount = 1;
        this.damage = 0;
        this.enchantments = new HashMap<Enchantment, Integer>();
        this.lore = new ArrayList<String>();
        this.andSymbol = true;
        this.unsafeStackSize = false;
        Validate.notNull((Object)item, "The Item is null.");
        this.item = item;
        if (item.hasItemMeta()) {
            this.meta = item.getItemMeta();
        }
        this.material = item.getType();
        this.amount = item.getAmount();
        this.data = item.getData();
        this.damage = item.getDurability();
        this.enchantments = (Map<Enchantment, Integer>)item.getEnchantments();
        if (item.hasItemMeta()) {
            this.displayname = item.getItemMeta().getDisplayName();
        }
        if (item.hasItemMeta()) {
            this.lore = (List<String>)item.getItemMeta().getLore();
        }
    }
    
    public ItemBuilder(final FileConfiguration cfg, final String path) {
        this(cfg.getItemStack(path));
    }
    
    public ItemBuilder(final ItemBuilder builder) {
        this.material = Material.STONE;
        this.amount = 1;
        this.damage = 0;
        this.enchantments = new HashMap<Enchantment, Integer>();
        this.lore = new ArrayList<String>();
        this.andSymbol = true;
        this.unsafeStackSize = false;
        Validate.notNull((Object)builder, "The ItemBuilder is null.");
        this.item = builder.item;
        this.meta = builder.meta;
        this.material = builder.material;
        this.amount = builder.amount;
        this.damage = builder.damage;
        this.data = builder.data;
        this.damage = builder.damage;
        this.enchantments = builder.enchantments;
        this.displayname = builder.displayname;
        this.lore = builder.lore;
    }
    
    public ItemBuilder amount(int amount) {
        if ((amount > this.material.getMaxStackSize() || amount <= 0) && !this.unsafeStackSize) {
            amount = 1;
        }
        this.amount = amount;
        return this;
    }
    
    public ItemBuilder data(final MaterialData data) {
        Validate.notNull((Object)data, "The Data is null.");
        this.data = data;
        return this;
    }
    
    @Deprecated
    public ItemBuilder damage(final short damage) {
        this.damage = damage;
        return this;
    }
    
    public ItemBuilder durability(final short damage) {
        this.damage = damage;
        return this;
    }
    
    public ItemBuilder material(final Material material) {
        Validate.notNull((Object)material, "The Material is null.");
        this.material = material;
        return this;
    }
    
    public ItemBuilder meta(final ItemMeta meta) {
        Validate.notNull((Object)meta, "The Meta is null.");
        this.meta = meta;
        return this;
    }
    
    public ItemBuilder enchant(final Enchantment enchant, final int level) {
        Validate.notNull((Object)enchant, "The Enchantment is null.");
        this.enchantments.put(enchant, level);
        return this;
    }
    
    public ItemBuilder enchant(final Map<Enchantment, Integer> enchantments) {
        Validate.notNull((Object)enchantments, "The Enchantments are null.");
        this.enchantments = enchantments;
        return this;
    }
    
    public ItemBuilder displayname(final String displayname) {
        Validate.notNull((Object)displayname, "The Displayname is null.");
        this.displayname = (this.andSymbol ? ChatColor.translateAlternateColorCodes('&', displayname) : displayname);
        return this;
    }
    
    public ItemBuilder lore(final String line) {
        Validate.notNull((Object)line, "The Line is null.");
        this.lore.add(this.andSymbol ? ChatColor.translateAlternateColorCodes('&', line) : line);
        return this;
    }
    
    public ItemBuilder lore(final List<String> lore) {
        Validate.notNull((Object)lore, "The Lores are null.");
        this.lore = lore;
        return this;
    }
    
    @Deprecated
    public ItemBuilder lores(final String... lines) {
        Validate.notNull((Object)lines, "The Lines are null.");
        for (final String line : lines) {
            this.lore(this.andSymbol ? ChatColor.translateAlternateColorCodes('&', line) : line);
        }
        return this;
    }
    
    public ItemBuilder lore(final String... lines) {
        Validate.notNull((Object)lines, "The Lines are null.");
        for (final String line : lines) {
            this.lore(this.andSymbol ? ChatColor.translateAlternateColorCodes('&', line) : line);
        }
        return this;
    }
    
    public ItemBuilder lore(final String line, final int index) {
        Validate.notNull((Object)line, "The Line is null.");
        this.lore.set(index, this.andSymbol ? ChatColor.translateAlternateColorCodes('&', line) : line);
        return this;
    }
    
    public ItemBuilder unbreakable(final boolean unbreakable) {
        this.meta.spigot().setUnbreakable(unbreakable);
        return this;
    }
    
    @Deprecated
    public ItemBuilder owner(final String user) {
        Validate.notNull((Object)user, "The Username is null.");
        if (this.material == Material.SKULL_ITEM || this.material == Material.SKULL) {
            final SkullMeta smeta = (SkullMeta)this.meta;
            smeta.setOwner(user);
            this.meta = (ItemMeta)smeta;
        }
        return this;
    }
    
    public Unsafe unsafe() {
        return new Unsafe(this);
    }
    
    @Deprecated
    public ItemBuilder replaceAndSymbol() {
        this.replaceAndSymbol(!this.andSymbol);
        return this;
    }
    
    public ItemBuilder replaceAndSymbol(final boolean replace) {
        this.andSymbol = replace;
        return this;
    }
    
    public ItemBuilder toggleReplaceAndSymbol() {
        this.replaceAndSymbol(!this.andSymbol);
        return this;
    }
    
    public ItemBuilder unsafeStackSize(final boolean allow) {
        this.unsafeStackSize = allow;
        return this;
    }
    
    public ItemBuilder toggleUnsafeStackSize() {
        this.unsafeStackSize(!this.unsafeStackSize);
        return this;
    }
    
    public String getDisplayname() {
        return this.displayname;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public Map<Enchantment, Integer> getEnchantments() {
        return this.enchantments;
    }
    
    @Deprecated
    public short getDamage() {
        return this.damage;
    }
    
    public short getDurability() {
        return this.damage;
    }
    
    public List<String> getLores() {
        return this.lore;
    }
    
    public boolean getAndSymbol() {
        return this.andSymbol;
    }
    
    public Material getMaterial() {
        return this.material;
    }
    
    public ItemMeta getMeta() {
        return this.meta;
    }
    
    public MaterialData getData() {
        return this.data;
    }
    
    @Deprecated
    public List<String> getLore() {
        return this.lore;
    }
    
    public ItemBuilder toConfig(final FileConfiguration cfg, final String path) {
        cfg.set(path, (Object)this.build());
        return this;
    }
    
    public ItemBuilder fromConfig(final FileConfiguration cfg, final String path) {
        return new ItemBuilder(cfg, path);
    }
    
    public static void toConfig(final FileConfiguration cfg, final String path, final ItemBuilder builder) {
        cfg.set(path, (Object)builder.build());
    }
    
    public String toJson() {
        return new Gson().toJson((Object)this);
    }
    
    public static String toJson(final ItemBuilder builder) {
        return new Gson().toJson((Object)builder);
    }
    
    public static ItemBuilder fromJson(final String json) {
        return (ItemBuilder)new Gson().fromJson(json, (Class)ItemBuilder.class);
    }
    
    public ItemBuilder applyJson(final String json, final boolean overwrite) {
        final ItemBuilder b = (ItemBuilder)new Gson().fromJson(json, (Class)ItemBuilder.class);
        if (overwrite) {
            return b;
        }
        if (b.displayname != null) {
            this.displayname = b.displayname;
        }
        if (b.data != null) {
            this.data = b.data;
        }
        if (b.material != null) {
            this.material = b.material;
        }
        if (b.lore != null) {
            this.lore = b.lore;
        }
        if (b.enchantments != null) {
            this.enchantments = b.enchantments;
        }
        if (b.item != null) {
            this.item = b.item;
        }
        this.damage = b.damage;
        this.amount = b.amount;
        return this;
    }
    
    public ItemStack build() {
        this.item.setType(this.material);
        this.item.setAmount(this.amount);
        this.item.setDurability(this.damage);
        this.meta = this.item.getItemMeta();
        if (this.data != null) {
            this.item.setData(this.data);
        }
        if (this.enchantments.size() > 0) {
            this.item.addUnsafeEnchantments((Map)this.enchantments);
        }
        if (this.displayname != null) {
            this.meta.setDisplayName(this.displayname);
        }
        if (this.lore.size() > 0) {
            this.meta.setLore((List)this.lore);
        }
        this.item.setItemMeta(this.meta);
        return this.item;
    }
    
    static /* synthetic */ void access$1(final ItemBuilder itemBuilder, final ItemStack item) {
        itemBuilder.item = item;
    }
    
    public class Unsafe
    {
        protected final ReflectionUtils utils;
        protected final ItemBuilder builder;
        
        public Unsafe(final ItemBuilder builder) {
            this.utils = new ReflectionUtils();
            this.builder = builder;
        }
        
        public Unsafe setString(final String key, final String value) {
            ItemBuilder.access$1(this.builder, this.utils.setString(this.builder.item, key, value));
            return this;
        }
        
        public String getString(final String key) {
            return this.utils.getString(this.builder.item, key);
        }
        
        public Unsafe setInt(final String key, final int value) {
            ItemBuilder.access$1(this.builder, this.utils.setInt(this.builder.item, key, value));
            return this;
        }
        
        public int getInt(final String key) {
            return this.utils.getInt(this.builder.item, key);
        }
        
        public Unsafe setDouble(final String key, final double value) {
            ItemBuilder.access$1(this.builder, this.utils.setDouble(this.builder.item, key, value));
            return this;
        }
        
        public double getDouble(final String key) {
            return this.utils.getDouble(this.builder.item, key);
        }
        
        public Unsafe setBoolean(final String key, final boolean value) {
            ItemBuilder.access$1(this.builder, this.utils.setBoolean(this.builder.item, key, value));
            return this;
        }
        
        public boolean getBoolean(final String key) {
            return this.utils.getBoolean(this.builder.item, key);
        }
        
        public boolean containsKey(final String key) {
            return this.utils.hasKey(this.builder.item, key);
        }
        
        public ItemBuilder builder() {
            return this.builder;
        }
        
        public class ReflectionUtils
        {
            public String getString(final ItemStack item, final String key) {
                Object compound = this.getNBTTagCompound(this.getItemAsNMSStack(item));
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    return (String)compound.getClass().getMethod("getString", String.class).invoke(compound, key);
                }
                catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                	ex.printStackTrace();
                    return null;
                }
            }
            
            public ItemStack setString(final ItemStack item, final String key, final String value) {
                Object nmsItem = this.getItemAsNMSStack(item);
                Object compound = this.getNBTTagCompound(nmsItem);
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    compound.getClass().getMethod("setString", String.class, String.class).invoke(compound, key, value);
                    nmsItem = this.setNBTTag(compound, nmsItem);
                }
                catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                	ex.printStackTrace();
                }
                return this.getItemAsBukkitStack(nmsItem);
            }
            
            public int getInt(final ItemStack item, final String key) {
                Object compound = this.getNBTTagCompound(this.getItemAsNMSStack(item));
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    return (int)compound.getClass().getMethod("getInt", String.class).invoke(compound, key);
                }
                catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                    return -1;
                }
            }
            
            public ItemStack setInt(final ItemStack item, final String key, final int value) {
                Object nmsItem = this.getItemAsNMSStack(item);
                Object compound = this.getNBTTagCompound(nmsItem);
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    compound.getClass().getMethod("setInt", String.class, Integer.class).invoke(compound, key, value);
                    nmsItem = this.setNBTTag(compound, nmsItem);
                }
                catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
                return this.getItemAsBukkitStack(nmsItem);
            }
            
            public double getDouble(final ItemStack item, final String key) {
                Object compound = this.getNBTTagCompound(this.getItemAsNMSStack(item));
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    return (double)compound.getClass().getMethod("getDouble", String.class).invoke(compound, key);
                }
                catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {

                    ex.printStackTrace();
                    return Double.NaN;
                }
            }
            
            public ItemStack setDouble(final ItemStack item, final String key, final double value) {
                Object nmsItem = this.getItemAsNMSStack(item);
                Object compound = this.getNBTTagCompound(nmsItem);
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    compound.getClass().getMethod("setDouble", String.class, Double.class).invoke(compound, key, value);
                    nmsItem = this.setNBTTag(compound, nmsItem);
                }
                catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {

                    ex.printStackTrace();
                }
                return this.getItemAsBukkitStack(nmsItem);
            }
            
            public boolean getBoolean(final ItemStack item, final String key) {
                Object compound = this.getNBTTagCompound(this.getItemAsNMSStack(item));
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    return (boolean)compound.getClass().getMethod("getBoolean", String.class).invoke(compound, key);
                }
                catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {

                    ex.printStackTrace();
                    return false;
                }
            }
            
            public ItemStack setBoolean(final ItemStack item, final String key, final boolean value) {
                Object nmsItem = this.getItemAsNMSStack(item);
                Object compound = this.getNBTTagCompound(nmsItem);
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    compound.getClass().getMethod("setBoolean", String.class, Boolean.class).invoke(compound, key, value);
                    nmsItem = this.setNBTTag(compound, nmsItem);
                }
                catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {

                    ex.printStackTrace();
                }
                return this.getItemAsBukkitStack(nmsItem);
            }
            
            public boolean hasKey(final ItemStack item, final String key) {
                Object compound = this.getNBTTagCompound(this.getItemAsNMSStack(item));
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    return (boolean)compound.getClass().getMethod("hasKey", String.class).invoke(compound, key);
                }
                catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {

                    e.printStackTrace();
                    return false;
                }
            }
            
            public Object getNewNBTTagCompound() {
                final String ver = Bukkit.getServer().getClass().getPackage().getName().split(".")[3];
                try {
                    return Class.forName("net.minecraft.server." + ver + ".NBTTagCompound").newInstance();
                }
                catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {

                    ex.printStackTrace();
                    return null;
                }
            }
            
            public Object setNBTTag(final Object tag, final Object item) {
                try {
                    item.getClass().getMethod("setTag", item.getClass()).invoke(item, tag);
                    return item;
                }
                catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {

                    ex.printStackTrace();
                    return null;
                }
            }
            
            public Object getNBTTagCompound(final Object nmsStack) {
                try {
                    return nmsStack.getClass().getMethod("getTag", (Class<?>[])new Class[0]).invoke(nmsStack, new Object[0]);
                }
                catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {

                    ex.printStackTrace();
                    return null;
                }
            }
            
            public Object getItemAsNMSStack(final ItemStack item) {
                try {
                    final Method m = this.getCraftItemStackClass().getMethod("asNMSCopy", ItemStack.class);
                    return m.invoke(this.getCraftItemStackClass(), item);
                }
                catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {

                    ex.printStackTrace();
                    return null;
                }
            }
            
            public ItemStack getItemAsBukkitStack(final Object nmsStack) {
                try {
                    final Method m = this.getCraftItemStackClass().getMethod("asCraftMirror", nmsStack.getClass());
                    return (ItemStack)m.invoke(this.getCraftItemStackClass(), nmsStack);
                }
                catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {

                    ex.printStackTrace();
                    return null;
                }
            }
            
            public Class<?> getCraftItemStackClass() {
                final String ver = Bukkit.getServer().getClass().getPackage().getName().split(".")[3];
                try {
                    return Class.forName("org.bukkit.craftbukkit." + ver + ".inventory.CraftItemStack");
                }
                catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
        }
    }
}
