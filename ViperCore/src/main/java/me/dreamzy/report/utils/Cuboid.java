package me.dreamzy.report.utils;

import org.bukkit.block.*;
import org.bukkit.configuration.serialization.*;
import java.util.logging.*;
import java.util.*;
import org.bukkit.*;

public class Cuboid implements Iterable<Block>, Cloneable, ConfigurationSerializable
{
    protected final String worldName;
    protected final int x1;
    protected final int y1;
    protected final int z1;
    protected final int x2;
    protected final int y2;
    protected final int z2;
    
    public Cuboid(final Location location1, final Location location2) {
        if (!location1.getWorld().equals(location2.getWorld())) {
            Logger.getLogger("Les locations doivent \u00eatre dans un m\u00eame monde.");
        }
        this.worldName = location1.getWorld().getName();
        this.x1 = Math.min(location1.getBlockX(), location2.getBlockX());
        this.y1 = Math.min(location1.getBlockY(), location2.getBlockY());
        this.z1 = Math.min(location1.getBlockZ(), location2.getBlockZ());
        this.x2 = Math.max(location1.getBlockX(), location2.getBlockX());
        this.y2 = Math.max(location1.getBlockY(), location2.getBlockY());
        this.z2 = Math.max(location1.getBlockZ(), location2.getBlockZ());
    }
    
    public Cuboid(final Location location) {
        this(location, location);
    }
    
    public Cuboid(final Cuboid cuboid) {
        this(cuboid.getWorld().getName(), cuboid.x1, cuboid.y1, cuboid.z1, cuboid.x2, cuboid.y2, cuboid.z2);
    }
    
    private Cuboid(final String string, final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        this.worldName = string;
        this.x1 = Math.min(n, n4);
        this.x2 = Math.max(n, n4);
        this.y1 = Math.min(n2, n5);
        this.y2 = Math.max(n2, n5);
        this.z1 = Math.min(n3, n6);
        this.z2 = Math.max(n3, n6);
    }
    
    public Cuboid(final Map<String, Object> map) {
        this.worldName = (String) map.get("worldName");
        this.x1 = (int) map.get("x1");
        this.x2 = (int) map.get("x2");
        this.y1 = (int) map.get("y1");
        this.y2 = (int) map.get("y2");
        this.z1 = (int) map.get("z1");
        this.z2 = (int) map.get("z2");
    }
    
    public Map<String, Object> serialize() {
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("worldName", this.worldName);
        hashMap.put("x1", this.x1);
        hashMap.put("y1", this.y1);
        hashMap.put("z1", this.z1);
        hashMap.put("x2", this.x2);
        hashMap.put("y2", this.y2);
        hashMap.put("z2", this.z2);
        return hashMap;
    }
    
    public Location getLowerNE() {
        return new Location(this.getWorld(), (double)this.x1, (double)this.y1, (double)this.z1);
    }
    
    public Location getUpperSW() {
        return new Location(this.getWorld(), (double)this.x2, (double)this.y2, (double)this.z2);
    }
    
    public List<Block> getBlocks() {
        final Iterator<Block> iterator = this.iterator();
        final ArrayList<Block> arrayList = new ArrayList<Block>();
        while (iterator.hasNext()) {
            arrayList.add(iterator.next());
        }
        return arrayList;
    }
    
    public Location getCenter() {
        final int i = this.getUpperX() + 1;
        final int j = this.getUpperY() + 1;
        final int k = this.getUpperZ() + 1;
        return new Location(this.getWorld(), this.getLowerX() + (i - this.getLowerX()) / 2.0, this.getLowerY() + (j - this.getLowerY()) / 2.0, this.getLowerZ() + (k - this.getLowerZ()) / 2.0);
    }
    
    public World getWorld() {
        final World world = Bukkit.getWorld(this.worldName);
        if (world == null) {
            Logger.getLogger("Le monde " + this.worldName + "n'est pas charg\u00e9 !");
        }
        return world;
    }
    
    public int getSizeX() {
        return this.x2 - this.x1 + 1;
    }
    
    public int getSizeY() {
        return this.y2 - this.y1 + 1;
    }
    
    public int getSizeZ() {
        return this.z2 - this.z1 + 1;
    }
    
    public int getLowerX() {
        return this.x1;
    }
    
    public int getLowerY() {
        return this.y1;
    }
    
    public int getLowerZ() {
        return this.z1;
    }
    
    public int getUpperX() {
        return this.x2;
    }
    
    public int getUpperY() {
        return this.y2;
    }
    
    public int getUpperZ() {
        return this.z2;
    }
    
    public Block[] corners() {
        final Block[] arrayOfBlock = new Block[8];
        final World localWorld = this.getWorld();
        arrayOfBlock[0] = localWorld.getBlockAt(this.x1, this.y1, this.z1);
        arrayOfBlock[1] = localWorld.getBlockAt(this.x1, this.y1, this.z2);
        arrayOfBlock[2] = localWorld.getBlockAt(this.x1, this.y2, this.z1);
        arrayOfBlock[3] = localWorld.getBlockAt(this.x1, this.y2, this.z2);
        arrayOfBlock[4] = localWorld.getBlockAt(this.x2, this.y1, this.z1);
        arrayOfBlock[5] = localWorld.getBlockAt(this.x2, this.y1, this.z2);
        arrayOfBlock[6] = localWorld.getBlockAt(this.x2, this.y2, this.z1);
        arrayOfBlock[7] = localWorld.getBlockAt(this.x2, this.y2, this.z2);
        return arrayOfBlock;
    }
    
    public Cuboid expand(final CuboidDirection paramCuboidDirection, final int paramInt) {
        switch (paramCuboidDirection) {
            case Both: {
                return new Cuboid(this.worldName, this.x1 - paramInt, this.y1, this.z1, this.x2, this.y2, this.z2);
            }
            case East: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2 + paramInt, this.y2, this.z2);
            }
            case Down: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1 - paramInt, this.x2, this.y2, this.z2);
            }
            case Horizontal: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2 + paramInt);
            }
            case South: {
                return new Cuboid(this.worldName, this.x1, this.y1 - paramInt, this.z1, this.x2, this.y2, this.z2);
            }
            case North: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2 + paramInt, this.z2);
            }
            default: {
                throw new IllegalArgumentException("Invalid direction " + paramCuboidDirection);
            }
        }
    }
    
    public Cuboid shift(final CuboidDirection paramCuboidDirection, final int paramInt) {
        return this.expand(paramCuboidDirection, paramInt).expand(paramCuboidDirection.opposite(), -paramInt);
    }
    
    public Cuboid outset(final CuboidDirection paramCuboidDirection, final int paramInt) {
        Cuboid localCuboid = null;
        switch (paramCuboidDirection) {
            case Unknown: {
                localCuboid = this.expand(CuboidDirection.North, paramInt).expand(CuboidDirection.South, paramInt).expand(CuboidDirection.East, paramInt).expand(CuboidDirection.West, paramInt);
                break;
            }
            case Up: {
                localCuboid = this.expand(CuboidDirection.Down, paramInt).expand(CuboidDirection.Up, paramInt);
                break;
            }
            case Vertical: {
                localCuboid = this.outset(CuboidDirection.Horizontal, paramInt).outset(CuboidDirection.Vertical, paramInt);
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid direction " + paramCuboidDirection);
            }
        }
        return localCuboid;
    }
    
    public Cuboid inset(final CuboidDirection cuboidDirection, final int n) {
        return this.outset(cuboidDirection, -n);
    }
    
    public boolean contains(final int n, final int n2, final int n3) {
        return n >= this.x1 && n <= this.x2 && n2 >= this.y1 && n2 <= this.y2 && n3 >= this.z1 && n3 <= this.z2;
    }
    
    public boolean contains(final Block block) {
        return this.contains(block.getLocation());
    }
    
    public boolean contains(final Location location) {
        return this.worldName.equals(location.getWorld().getName()) && this.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    
    public int getVolume() {
        return this.getSizeX() * this.getSizeY() * this.getSizeZ();
    }
    
    public byte getAverageLightLevel() {
        long l = 0L;
        int i = 0;
        for (final Block localBlock : this) {
            if (localBlock.isEmpty()) {
                l += localBlock.getLightLevel();
                ++i;
            }
        }
        return (byte)((i > 0) ? ((byte)(l / i)) : 0);
    }
    
    public Cuboid contract() {
        return this.contract(CuboidDirection.Down).contract(CuboidDirection.South).contract(CuboidDirection.East).contract(CuboidDirection.Up).contract(CuboidDirection.North).contract(CuboidDirection.West);
    }
    
    public Cuboid contract(final CuboidDirection paramCuboidDirection) {
        Cuboid localCuboid = this.getFace(paramCuboidDirection.opposite());
        switch (paramCuboidDirection) {
            case South: {
                while (localCuboid.containsOnly(0) && localCuboid.getLowerY() > this.getLowerY()) {
                    localCuboid = localCuboid.shift(CuboidDirection.Down, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, localCuboid.getUpperY(), this.z2);
            }
            case North: {
                while (localCuboid.containsOnly(0) && localCuboid.getUpperY() < this.getUpperY()) {
                    localCuboid = localCuboid.shift(CuboidDirection.Up, 1);
                }
                return new Cuboid(this.worldName, this.x1, localCuboid.getLowerY(), this.z1, this.x2, this.y2, this.z2);
            }
            case Both: {
                while (localCuboid.containsOnly(0) && localCuboid.getLowerX() > this.getLowerX()) {
                    localCuboid = localCuboid.shift(CuboidDirection.North, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, localCuboid.getUpperX(), this.y2, this.z2);
            }
            case East: {
                while (localCuboid.containsOnly(0) && localCuboid.getUpperX() < this.getUpperX()) {
                    localCuboid = localCuboid.shift(CuboidDirection.South, 1);
                }
                return new Cuboid(this.worldName, localCuboid.getLowerX(), this.y1, this.z1, this.x2, this.y2, this.z2);
            }
            case Down: {
                while (localCuboid.containsOnly(0) && localCuboid.getLowerZ() > this.getLowerZ()) {
                    localCuboid = localCuboid.shift(CuboidDirection.East, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, localCuboid.getUpperZ());
            }
            case Horizontal: {
                while (localCuboid.containsOnly(0) && localCuboid.getUpperZ() < this.getUpperZ()) {
                    localCuboid = localCuboid.shift(CuboidDirection.West, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, localCuboid.getLowerZ(), this.x2, this.y2, this.z2);
            }
            default: {
                throw new IllegalArgumentException("Invalid direction " + paramCuboidDirection);
            }
        }
    }
    
    public Cuboid getFace(final CuboidDirection paramCuboidDirection) {
        switch (paramCuboidDirection) {
            case South: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y1, this.z2);
            }
            case North: {
                return new Cuboid(this.worldName, this.x1, this.y2, this.z1, this.x2, this.y2, this.z2);
            }
            case Both: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x1, this.y2, this.z2);
            }
            case East: {
                return new Cuboid(this.worldName, this.x2, this.y1, this.z1, this.x2, this.y2, this.z2);
            }
            case Down: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z1);
            }
            case Horizontal: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z2, this.x2, this.y2, this.z2);
            }
            default: {
                throw new IllegalArgumentException("Invalid direction " + paramCuboidDirection);
            }
        }
    }
    
    public boolean containsOnly(final int paramInt) {
        for (final Block block : this) {
            if (block.getTypeId() != paramInt) {
                return false;
            }
        }
        return true;
    }
    
    public Cuboid getBoundingCuboid(final Cuboid paramCuboid) {
        if (paramCuboid == null) {
            return this;
        }
        final int i = Math.min(this.getLowerX(), paramCuboid.getLowerX());
        final int j = Math.min(this.getLowerY(), paramCuboid.getLowerY());
        final int k = Math.min(this.getLowerZ(), paramCuboid.getLowerZ());
        final int m = Math.max(this.getUpperX(), paramCuboid.getUpperX());
        final int n = Math.max(this.getUpperY(), paramCuboid.getUpperY());
        final int i2 = Math.max(this.getUpperZ(), paramCuboid.getUpperZ());
        return new Cuboid(this.worldName, i, j, k, m, n, i2);
    }
    
    public Block getRelativeBlock(final int n, final int n2, final int n3) {
        return this.getWorld().getBlockAt(this.x1 + n, this.y1 + n2, this.z1 + n3);
    }
    
    public Block getRelativeBlock(final World world, final int n, final int n2, final int n3) {
        return world.getBlockAt(this.x1 + n, this.y1 + n2, this.z1 + n3);
    }
    
    public List<Chunk> getChunks() {
        final ArrayList<Chunk> arrayList = new ArrayList<Chunk>();
        final World world = this.getWorld();
        final int i = this.getLowerX() & 0xFFFFFFF0;
        final int j = this.getUpperX() & 0xFFFFFFF0;
        final int k = this.getLowerZ() & 0xFFFFFFF0;
        final int m = this.getUpperZ() & 0xFFFFFFF0;
        for (int n = i; n <= j; n += 16) {
            for (int i2 = k; i2 <= m; i2 += 16) {
                arrayList.add(world.getChunkAt(n >> 4, i2 >> 4));
            }
        }
        return arrayList;
    }
    
    @Override
    public Iterator<Block> iterator() {
        return new CuboidIterator(this.getWorld(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
    }
    
    public Cuboid clone() {
        return new Cuboid(this);
    }
    
    @Override
    public String toString() {
        return new String("Cuboid: " + this.worldName + "," + this.x1 + "," + this.y1 + "," + this.z1 + "=>" + this.x2 + "," + this.y2 + "," + this.z2);
    }
    
    public enum CuboidDirection
    {
        North("North", 0), 
        East("East", 1), 
        South("South", 2), 
        West("West", 3), 
        Up("Up", 4), 
        Down("Down", 5), 
        Horizontal("Horizontal", 6), 
        Vertical("Vertical", 7), 
        Both("Both", 8), 
        Unknown("Unknown", 9);
        
        private CuboidDirection(final String s, final int n) {
        }
        
        public CuboidDirection opposite() {
            switch (this) {
                case Both: {
                    return CuboidDirection.South;
                }
                case Down: {
                    return CuboidDirection.West;
                }
                case East: {
                    return CuboidDirection.North;
                }
                case Horizontal: {
                    return CuboidDirection.East;
                }
                case Unknown: {
                    return CuboidDirection.Vertical;
                }
                case Up: {
                    return CuboidDirection.Horizontal;
                }
                case North: {
                    return CuboidDirection.Down;
                }
                case South: {
                    return CuboidDirection.Up;
                }
                case Vertical: {
                    return CuboidDirection.Both;
                }
                default: {
                    return CuboidDirection.Unknown;
                }
            }
        }
    }
    
    public class CuboidIterator implements Iterator<Block>
    {
        private World w;
        private int baseX;
        private int baseY;
        private int baseZ;
        private int x;
        private int y;
        private int z;
        private int sizeX;
        private int sizeY;
        private int sizeZ;
        
        public CuboidIterator(final World paramWorld, final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4, final int paramInt5, final int paramInt6) {
            this.w = paramWorld;
            this.baseX = paramInt1;
            this.baseY = paramInt2;
            this.baseZ = paramInt3;
            this.sizeX = Math.abs(paramInt4 - paramInt1) + 1;
            this.sizeY = Math.abs(paramInt5 - paramInt2) + 1;
            this.sizeZ = Math.abs(paramInt6 - paramInt3) + 1;
            final boolean x = false;
            this.z = (x ? 1 : 0);
            this.y = (x ? 1 : 0);
            this.x = (x ? 1 : 0);
        }
        
        @Override
        public boolean hasNext() {
            return this.x < this.sizeX && this.y < this.sizeY && this.z < this.sizeZ;
        }
        
        @Override
        public Block next() {
            final Block block = this.w.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
            if (++this.x >= this.sizeX) {
                this.x = 0;
                if (++this.y >= this.sizeY) {
                    this.y = 0;
                    ++this.z;
                }
            }
            return block;
        }
        
        @Override
        public void remove() {
        }
    }
}
