package net.libhalt.bukkit.kaede.manager.crowbar;

import org.bukkit.Material;

public enum CrowbarType{
	SPAWNER(Material.MOB_SPAWNER  , "Spawners"),
	END_PORTAL(Material.ENDER_PORTAL_FRAME , "End Portals");
	private Material salvagable;
	private String readable;
	CrowbarType(Material salvagable , String readable){
		this.salvagable = salvagable;
		this.readable = readable;
	}
	public Material getSalvagable(){
		return salvagable;
	}
	
	public String toReadable(){
		return readable;
	}
	
	public static CrowbarType fromMaterial(Material material){
		switch(material){
		case MOB_SPAWNER:
			return CrowbarType.SPAWNER;
		case ENDER_PORTAL_FRAME:
			return END_PORTAL;
		default:
			return null;
		}
	}
}