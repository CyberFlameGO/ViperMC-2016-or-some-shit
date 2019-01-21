package net.libhalt.bukkit.kaede.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.base.Preconditions;

public class PotionUtils {
	public static final PotionEffect ROUGE_SPEED_5 = new PotionEffect(PotionEffectType.SPEED, 300, 4);
	public static final PotionEffect MINER_INVISIBILITY_1 = new PotionEffect(PotionEffectType.INVISIBILITY, 120, 0);

	private static List<PotionEffectType> negatives;
	static{
		List<PotionEffectType> negatives = new ArrayList<PotionEffectType>();
		negatives.add(PotionEffectType.POISON);
		negatives.add(PotionEffectType.WEAKNESS);
		negatives.add(PotionEffectType.BLINDNESS);
		negatives.add(PotionEffectType.WITHER );
		negatives.add(PotionEffectType.SLOW);
		negatives.add(PotionEffectType.WEAKNESS);
		PotionUtils.negatives = negatives;

	}
	public static boolean isNegative(PotionEffectType type) {
		return negatives.contains(type);
	}

	public static boolean canAddConcideringLevel(Player player, PotionEffect effect) {
		if (player.hasPotionEffect(effect.getType())) {
			PotionEffect before = getPotionEffect(player, effect.getType());
			if (before.getAmplifier() < effect.getAmplifier()) {
				return true;
			} else if (before.getAmplifier() == effect.getAmplifier() && before.getDuration() < effect.getDuration()) {
				return true;
			}
		} else {
			return true;
		}
		return false;


	}
	public static void addConcideringLevel(Player player, PotionEffect effect) {
		if(canAddConcideringLevel(player, effect)){
			player.addPotionEffect(effect, true);
		}
	}

	@SuppressWarnings("deprecation")
	public static PotionEffect getPotionEffect(Player player, PotionEffectType type) {
		Preconditions.checkState(player.hasPotionEffect(type));
		for(PotionEffect effect : player.getActivePotionEffects()){
			if (effect.getType().getId() == type.getId()) {
				return effect;
			}
		}
		throw new AssertionError();
	}
	
	public static PotionEffect parase(ConfigurationSection section){
		String value = section.getString("type");
		int amplifier = section.getInt("amplifier");
		int duration = section.getInt("duration");
		PotionEffectType type  = PotionEffectType.getByName(value);
		if(type == null){
			JavaPlugin.getProvidingPlugin(PotionUtils.class).getLogger().severe("Can not parase " + value + " as potion effect in bard configuration");
			return null;
		}
		return new PotionEffect(type , duration , amplifier);
	}
}
