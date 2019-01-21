package net.libhalt.bukkit.kaede.manager.kits;

import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.support.Support;
import net.libhalt.bukkit.kaede.utils.Armor;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.ItemStackUtils;
import net.libhalt.bukkit.kaede.utils.Manager;
import net.libhalt.bukkit.kaede.utils.PotionUtils;

public class KitManager extends Manager{
	public KitManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	private WeakHashMap<Player , Armor> activeKits = new WeakHashMap<Player , Armor>();
	private WeakHashMap<Player , Armor> pendingActivate = new WeakHashMap<Player , Armor>();
	private ConfigurationWrapper wrap;
	private BardConfiguration bard;
	private PassiveConfugiration passive;
	private long rougeCooldownMillisecond;
	private Set<Armor> disabledArmors = new HashSet<Armor>();
	private boolean minerInvisibility;
	private String archerFormula;
	private int warmup = 10;
	@Override
	public void init(){
		wrap = new ConfigurationWrapper("armor-class.yml", getPlugin());
		this.rougeCooldownMillisecond = wrap.getConfig().getLong("rouge.backstab-cooldown" , 1000L);
		this.warmup = wrap.getConfig().getInt("class-warmup" , 10);
		bard = new BardConfiguration(getPlugin(), wrap.getConfig());
		bard.init();
		passive = new PassiveConfugiration(getPlugin(), wrap.getConfig());
		passive.init();
		if(!wrap.getConfig().getBoolean("rouge.enabled" , true)){
			disabledArmors.add(Armor.CHAIN_MAIL);
		}
		if(!wrap.getConfig().getBoolean("miner.enabled" , true)){
			disabledArmors.add(Armor.IRON);
		}
		if(!wrap.getConfig().getBoolean("archer.enabled" , true)){
			disabledArmors.add(Armor.LEATHER);
		}
		if(!wrap.getConfig().getBoolean("bard.enabled" , true)){
			disabledArmors.add(Armor.GOLD);
		}
		minerInvisibility = wrap.getConfig().getBoolean("miner.invisibility-under-twenty" , true);
		archerFormula = wrap.getConfig().getString("archer.archer-damage-formula" , "(damage * 2.5) + (distance/12.5) ");
		getPlugin().getServer().getPluginManager().registerEvents(new RougeListener(this), getPlugin());
		getPlugin().getServer().getPluginManager().registerEvents(new BardListener(this), getPlugin());
		getPlugin().getServer().getPluginManager().registerEvents(new ArcherListener(this), getPlugin());
		(new BukkitRunnable() {
			@Override
			public void run() {
				for(final Player player : Bukkit.getOnlinePlayers()){
					Armor armor;
					if (KitManager.this.activeKits.containsKey(player)) {
						armor = KitManager.this.activeKits.get(player);
						if (ItemStackUtils.isWearingFull(player, armor)) {
							reapply(player);
							if(armor == Armor.GOLD){
								applyBard(player, player.getItemInHand());
							}
							if(armor == Armor.IRON &&minerInvisibility && player.getLocation().getBlockY() <= 20){
								player.removePotionEffect(PotionEffectType.INVISIBILITY);
								player.addPotionEffect(PotionUtils.MINER_INVISIBILITY_1);
							}
						} else {
							KitManager.this.activeKits.remove(player);
							if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
								player.removePotionEffect(PotionEffectType.NIGHT_VISION);
							}
							HCFactionPlugin.getInstance().sendLocalized(player, "CLASS_REMOVED",  KitManager.this.armorToKitString(armor));
						}
					} else if (ItemStackUtils.isWearingFull(player, Armor.CHAIN_MAIL) || ItemStackUtils.isWearingFull(player, Armor.GOLD) || ItemStackUtils.isWearingFull(player, Armor.LEATHER) || ItemStackUtils.isWearingFull(player, Armor.IRON)) {
						armor = ItemStackUtils.getArmor(player.getInventory().getArmorContents()[0].getType());
						if (!KitManager.this.pendingActivate.containsKey(player)) {
							if(disabledArmors.contains(armor)){
								continue;
							}
							if(warmup > 0){
								HCFactionPlugin.getInstance().sendLocalized(player, "CLASS_WARMUP",  KitManager.this.armorToKitString(armor));
								KitManager.this.pendingActivate.put(player, armor);
								(new BukkitRunnable() {
									@Override
									public void run() {
										if (KitManager.this.pendingActivate.containsKey(player)) {
											Armor armor = KitManager.this.pendingActivate.get(player);
											if (ItemStackUtils.isWearingFull(player, armor)) {
												HCFactionPlugin.getInstance().sendLocalized(player, "CLASS_ACTIVE",  KitManager.this.armorToKitString(armor));
												KitManager.this.activeKits.put(player, armor);
											}

											KitManager.this.pendingActivate.remove(player);
										}

									}
								}).runTaskLater(getPlugin(), warmup * 20);
							}else{
								HCFactionPlugin.getInstance().sendLocalized(player, "CLASS_ACTIVE",  KitManager.this.armorToKitString(armor));
								KitManager.this.activeKits.put(player, armor);
							}
						}
					}
				}

			}
		}).runTaskTimer(getPlugin(), 60, 60L);
	}

	public Armor getActiveKit(Player player) {
		return this.activeKits.get(player);
	}

	public String armorToKitString(Armor armor) {
		switch (armor) {
		case LEATHER:
			return "Archer";
		case CHAIN_MAIL:
			return "Rouge";
		case GOLD:
			return "Bard";
		case IRON:
			return "Miner";
		case DIAMOND:
			return "Diamond";
		default:
			return "";
		}
	}

	public void reapply(Player player){
		Armor armor = KitManager.this.activeKits.get(player);
		if(armor != null){
			if (ItemStackUtils.isWearingFull(player, armor)) {
				for(PotionEffect effect : passive.getEffects(armor)){
					if(effect.getType().equals(PotionEffectType.NIGHT_VISION)){
						if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
							continue;
						}
					}
					PotionUtils.addConcideringLevel(player, effect);
				}
			}
		}
	}

	public boolean applyInstantBard(final Player player, ItemStack item) {
		if (item != null) {
			Material type = item.getType();
			PotionEffect effect = bard.getClickPotionEffect(type);
			if (effect != null) {
				final Set<Player> team = Support.getInstance().getEveryoneInTeamNearByForBard(player);
				if (PotionUtils.isNegative(effect.getType())) {
					for(Entity entity :player.getNearbyEntities(12.0D, 12.0D, 12.0D) ){
						if (entity instanceof Player) {
							Player other = (Player) entity;
							if (!team.contains(other)) {
								PotionUtils.addConcideringLevel(other, effect);
							}
						}
					}
					PotionUtils.addConcideringLevel(player, effect);
				} else {
					for(final Player players1 : team){
						addEffectWithReturn(players1, effect);
					}
					addEffectWithReturn(player, effect);


				}
				return true;
			}
		}
		return false;

	}

	class ReturnEffectTask extends BukkitRunnable{
		private Player player;
		private PotionEffect pre;
		public ReturnEffectTask(Player player, PotionEffect pre) {
			super();
			this.player = player;
			this.pre = pre;
		}
		@Override
		public void run() {
			reapply(player);
			if(player.hasPotionEffect(pre.getType())){
				PotionEffect effect = PotionUtils.getPotionEffect(player, pre.getType());
				if(effect.getAmplifier() == pre.getAmplifier()){
					if(pre.getDuration() > effect.getDuration()){
						player.removePotionEffect(pre.getType());
						player.addPotionEffect(pre);
					}
				}else{
					player.removePotionEffect(pre.getType());
					player.addPotionEffect(pre);
				}
			}else{
				player.addPotionEffect(pre);
			}
			Armor armor = getActiveKit(player);
			if(armor != null){
				applyBard(player, player.getItemInHand());
			}
		}
	}
	public boolean canBardInstantApply(Player player, ItemStack item) {
		if (item != null) {
			return bard.getClickPotionEffect(item.getType()) != null;
		} else {
			return false;
		}
	}

	public void addEffectWithReturn(Player player , PotionEffect effect){
		if(PotionUtils.canAddConcideringLevel(player, effect) && player.hasPotionEffect(effect.getType())){
			PotionEffect temp = PotionUtils.getPotionEffect(player, effect.getType());
			final PotionEffect pre = new PotionEffect(temp.getType() , temp.getDuration() , temp.getAmplifier()  , temp.isAmbient());
			new ReturnEffectTask(player, pre).runTaskLater(getPlugin() , effect.getDuration());
		}
		PotionUtils.addConcideringLevel(player, effect);
	}

	public void applyBard(final Player player, ItemStack item) {
		if (item != null) {
			Material type = item.getType();
			final PotionEffect effect = bard.getHeldPotionEffect(type);
			if (effect != null) {
				final Set<Player> team = Support.getInstance().getEveryoneInTeamNearByForBard(player);
				for(Player players1 : team){
					if(bard.isNoSelfHeldEffect() && players1 == player){
						continue;
					}
					addEffectWithReturn(players1, effect);
				}
				if(!bard.isNoSelfHeldEffect()){
					addEffectWithReturn(player, effect);
				}
			}
		}

	}

	public String getArcherFormula() {
		return archerFormula;
	}

	public long getRougeCooldownMillisecond() {
		return rougeCooldownMillisecond;
	}


}
