package com.igodlik3.conquest.event;

public class ConquestGame
{
    private ConquestArea[] areas;
    private String name;
    
    public ConquestGame(final String name) {
        this.areas = new ConquestArea[ConquestArea.Type.values().length];
        this.name = name;
    }
    
    public void setArea(final ConquestArea.Type type, final ConquestArea area) {
        this.areas[type.ordinal()] = area;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ConquestArea getArea(final ConquestArea.Type type) {
        return this.areas[type.ordinal()];
    }
    
    public ConquestArea[] getAreas() {
        return this.areas;
    }
    
    public boolean isComplete() {
        ConquestArea[] areas;
        for (int length = (areas = this.areas).length, i = 0; i < length; ++i) {
            final ConquestArea area = areas[i];
            if (area == null) {
                return false;
            }
        }
        return true;
    }
}
