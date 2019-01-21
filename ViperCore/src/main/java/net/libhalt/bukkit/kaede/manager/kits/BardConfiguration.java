package net.libhalt.bukkit.kaede.manager.kits;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.PotionUtils;

public class BardConfiguration {

	private Map<Material ,PotionEffect> heldPotionEffect= new HashMap<Material , PotionEffect>();
	private Map<Material ,PotionEffect> clickPotionEffect= new HashMap<Material , PotionEffect>();
	private boolean noSelfHeldEffect;
	private ConfigurationSection config;
	private HCFactionPlugin plugin;
	public BardConfiguration(HCFactionPlugin plugin , Configuration config){
		this.config = config.getConfigurationSection("bard");
		this.plugin = plugin;
	}

	public void init(){
		fill("held", heldPotionEffect);
		fill("click", clickPotionEffect);
		this.noSelfHeldEffect = config.getBoolean("dont-give-held-effect-to-self" , false);
	}
	
	
	private void fill(String sectionName , Map<Material , PotionEffect> map){
		ConfigurationSection heldItemSection = config.getConfigurationSection(sectionName);
		map.clear();
		for(String input : heldItemSection.getKeys(false)){
			Material material = null;
			try{
				material = Material.valueOf(input.toUpperCase());
			}catch(IllegalArgumentException e){
				plugin.getLogger().severe("Can not parase " + input + " as material in bard configuration.");
				continue;
			}
			PotionEffect effect = PotionUtils.parase(heldItemSection.getConfigurationSection(input));
			if(effect == null){
				plugin.getLogger().severe("Potion effect could not be parased for " + input + " this config will be ignored.");
				continue;
			}
			map.put(material, effect);
		}
	}

	
	public PotionEffect getHeldPotionEffect(Material material){
		return heldPotionEffect.get(material);
	}
	public PotionEffect getClickPotionEffect(Material material){
		return clickPotionEffect.get(material);

	}

	public boolean isNoSelfHeldEffect() {
		return noSelfHeldEffect;
	}

	
}
