package net.libhalt.dev.plugin.armor.kit;

import com.google.common.collect.Lists;
import org.bukkit.configuration.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import net.syuu.popura.*;
import org.bukkit.entity.*;
import net.libhalt.dev.plugin.armor.utils.*;
import net.syuu.popura.faction.bean.*;
import java.util.*;
import net.libhalt.dev.plugin.armor.*;

public class Bard extends AbstractClass
{
    private WeakHashMap<Player, Integer> powerMap;
    
    public Bard(final ConfigurationSection section) {
        super(section);
        this.powerMap = new WeakHashMap<Player, Integer>();
    }
    
    public void setPower(final Player player, final int power) {
        this.powerMap.put(player, power);
    }
    
    public int getPower(final Player player) {
        if (!this.powerMap.containsKey(player)) {
            return 0;
        }
        return this.powerMap.get(player);
    }
    
    @Override
    public void applyHeldEffect(final Player player, final ItemStack item) {
        if (item != null && this.heldEffects.containsKey(item.getType())) {
            for (final PotionEffect effect : this.heldEffects.get(item.getType()).getEffects()) {
                final Set<Player> apply = new HashSet<Player>();
                final FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
                final List<Player> onlineFactionMembers = (factionPlayer != null && factionPlayer.getFaction() != null) ? new ArrayList<Player>(factionPlayer.getFaction().getOnlineMembers()) : Collections.singletonList(player);
                final List<Player> allies = Lists.newArrayList();
                if(factionPlayer != null && factionPlayer.getFaction() != null){
                    for(Faction faction : factionPlayer.getFaction().getAllies()){
                        allies.addAll(faction.getOnlineMembers());
                    }
                }

                for (final Entity entity : player.getNearbyEntities(24, 24, 24)) {
                    if (entity instanceof Player) {
                        final Player near = (Player)entity;
                        boolean val = onlineFactionMembers.contains(near);
                        if (PotionUtils.isNegative(effect.getType())) {
                            val = !val;
                            if( allies.contains(player)){
                                val = false;
                            }

                        }
                        if (!val) {
                            continue;
                        }
                        apply.add(near);
                    }
                }
                apply.add(player);
                for (final Player target : apply) {
                    PotionUtils.addConcideringLevel(target, effect);
                }
            }
        }
    }
    
    @Override
    public void applyEffect(final Player player, final PotionEffect effect) {
        final Set<Player> apply = new HashSet<Player>();
        final FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
        final List<Player> onlineFactionMembers = (factionPlayer != null && factionPlayer.getFaction() != null) ? new ArrayList<Player>(factionPlayer.getFaction().getOnlineMembers()) : Collections.singletonList(player);
        final List<Player> allies = Lists.newArrayList();
        if(factionPlayer != null && factionPlayer.getFaction() != null){
            for(Faction faction : factionPlayer.getFaction().getAllies()){
                allies.addAll(faction.getOnlineMembers());
            }
        }
        for (final Entity entity : player.getNearbyEntities(24, 24, 24)) {
            if (entity instanceof Player) {
                final Player near = (Player)entity;
                boolean val = onlineFactionMembers.contains(near);
                if (PotionUtils.isNegative(effect.getType())) {
                    val = !val;
                    if( allies.contains(player)){
                        val = false;
                    }
                }
                if (!val) {
                    continue;
                }
                apply.add(near);
            }
        }
        apply.add(player);
        for (final Player target : apply) {
            ArmorPlugin.getInstance().addEffectWithReturn(target, effect);
        }
    }
}
