package com.igodlik3.conquest.event;

import org.bukkit.*;

public class ConquestArea
{
    private ConquestGame game;
    private Type type;
    private Location loc1;
    private Location loc2;
    
    public ConquestArea(final ConquestGame game, final Type type, final Location loc1, final Location loc2) {
        this.game = game;
        this.type = type;
        this.loc1 = loc1;
        this.loc2 = loc2;
    }
    
    public ConquestGame getGame() {
        return this.game;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public Location getLoc1() {
        return this.loc1;
    }
    
    public Location getLoc2() {
        return this.loc2;
    }
    
    public enum Type
    {
        RED("RED", 0), 
        GREEN("GREEN", 1), 
        BLUE("BLUE", 2), 
        YELLOW("YELLOW", 3);
        
        private Type(final String s, final int n) {
        }
    }
}
