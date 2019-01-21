package net.libhalt.bukkit.kaede.manager.kits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.Armor;
import net.libhalt.bukkit.kaede.utils.PotionUtils;

public class PassiveConfugiration {
	@SuppressWarnings("unused")
	private HCFactionPlugin plugin;
	private ConfigurationSection config;
	private Map<Armor , List<PotionEffect>> passiveEffects = new HashMap<Armor , List<PotionEffect>>();
	public PassiveConfugiration(HCFactionPlugin plugin , Configuration config){
		this.config = config;
		this.plugin = plugin;
	}
	public void init(){
		ConfigurationSection bard = config.getConfigurationSection("bard");
		ConfigurationSection rouge = config.getConfigurationSection("rouge");
		ConfigurationSection miner = config.getConfigurationSection("miner");
		ConfigurationSection archer = config.getConfigurationSection("archer");
		parase(Armor.GOLD, bard);
		parase(Armor.CHAIN_MAIL, rouge);
		parase(Armor.IRON, miner);
		parase(Armor.LEATHER, archer);

	}

	public List<PotionEffect> getEffects(Armor armor){
		if(passiveEffects.containsKey(armor)){
			return passiveEffects.get(armor);
		}
		return Collections.<PotionEffect>emptyList();
	}
	private void parase(Armor armor , ConfigurationSection section){
		if(section.isConfigurationSection("passive")){
			ConfigurationSection passiveSection = section.getConfigurationSection("passive");
			List<PotionEffect> effects = new ArrayList<PotionEffect>();
			for(String str : passiveSection.getKeys(false)){
				effects.add(PotionUtils.parase(passiveSection.getConfigurationSection(str)));
			}
			passiveEffects.put(armor, effects);
		}
	}

}
