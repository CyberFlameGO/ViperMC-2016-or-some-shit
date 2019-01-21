package com.igodlik3.conquest.event;

import com.igodlik3.conquest.*;
import com.igodlik3.conquest.utils.*;
import org.bukkit.configuration.*;
import org.bukkit.*;
import java.util.*;

public class ConquestManager
{
    private ConquestData runningGameData;
    private Configuration config;
    private Configuration data;
    private Map<ConquestArea, Cuboid> cuboCache;
    
    public ConquestManager() {
        this.config = (Configuration)Conquest.getInstance().getConfig();
        this.data = (Configuration)Conquest.getInstance().getStorage().getConfig();
        this.cuboCache = new HashMap<ConquestArea, Cuboid>();
    }
    
    public boolean gameExists(final String name) {
        return this.data.contains("conquest." + name);
    }
    
    public void saveOrUpdateGame(final ConquestGame game) {
        final ConfigurationSection section = this.gameExists("conquest." + game.getName()) ? this.data.getConfigurationSection("conquest." + game.getName()) : this.data.createSection("conquest." + game.getName());
        for (int i = 0; i < ConquestArea.Type.values().length; ++i) {
            final ConquestArea area = game.getArea(ConquestArea.Type.values()[i]);
            if (area == null) {
                section.set("area." + i, (Object)null);
            }
            else {
                section.set("area." + i + ".loc1", (Object)Utils.stringifyLocation(area.getLoc1()));
                section.set("area." + i + ".loc2", (Object)Utils.stringifyLocation(area.getLoc2()));
            }
            Conquest.getInstance().getStorage().saveConfig();
        }
    }
    
    public ConquestGame getGame(final String name) {
        if (!this.gameExists(name)) {
            return null;
        }
        final ConfigurationSection section = this.data.getConfigurationSection("conquest." + name);
        final ConquestGame game = new ConquestGame(name);
        for (int i = 0; i < ConquestArea.Type.values().length; ++i) {
            if (section.contains("area." + i)) {
                game.setArea(ConquestArea.Type.values()[i], new ConquestArea(game, ConquestArea.Type.values()[i], Utils.destringifyLocation(section.getString("area." + i + ".loc1")), Utils.destringifyLocation(section.getString("area." + i + ".loc2"))));
            }
        }
        return game;
    }
    
    public void startConquest(final ConquestGame game) {
        this.setRunningGame(game);
        Bukkit.broadcastMessage(Utils.color(this.config.getString("Messages.CONQUEST-STARTED").replaceAll("%NAME%", game.getName()).replaceAll("%POINTS%", String.valueOf(this.config.getInt("CONQUEST.Required-Points")))));
    }
    
    public Cuboid getConquestCubo(final ConquestArea area) {
        if (this.cuboCache.containsKey(area)) {
            return this.cuboCache.get(area);
        }
        final Cuboid cubo = new Cuboid(area.getLoc1(), area.getLoc2());
        this.cuboCache.put(area, cubo);
        return cubo;
    }
    
    public void deleteGame(final ConquestGame game) {
        this.deleteGame(game.getName());
    }
    
    public void deleteGame(final String name) {
        if (!this.gameExists(name)) {
            return;
        }
        this.data.set("conquest." + name, (Object)null);
        Conquest.getInstance().getStorage().saveConfig();
    }
    
    public Set<String> listGames() {
        if (this.data.isConfigurationSection("conquest")) {
            return (Set<String>)this.data.getConfigurationSection("conquest").getKeys(false);
        }
        return null;
    }
    
    public ConquestGame getRunningGame() {
        if (!this.data.contains("conquest.running")) {
            return null;
        }
        return this.getGame(this.data.getString("conquest.running"));
    }
    
    public boolean setRunningGame(final ConquestGame game) {
        this.data.set("conquest.running", (Object)((game != null) ? game.getName() : null));
        Conquest.getInstance().getStorage().saveConfig();
        if (game == null) {
            this.runningGameData = null;
        }
        else {
            this.runningGameData = new ConquestData();
        }
        return true;
    }
    
    public void resetRunningGameData() {
        this.data.set("rdata", (Object)null);
        Conquest.getInstance().getStorage().saveConfig();
    }
    
    public ConquestData getRunningGameData() {
        return this.runningGameData;
    }
}
