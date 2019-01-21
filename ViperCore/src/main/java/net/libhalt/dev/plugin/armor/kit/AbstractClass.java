package net.libhalt.dev.plugin.armor.kit;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;
import net.libhalt.dev.plugin.armor.*;
import org.bukkit.configuration.*;
import net.libhalt.dev.plugin.armor.utils.*;
import java.util.*;
import org.bukkit.inventory.*;
import org.apache.commons.lang.time.*;

public abstract class AbstractClass
{
    protected Map<Material, ClickEffect> clickEffects;
    protected Map<Material, ClickEffect> heldEffects;
    protected WeakHashMap<Player, Long> cooldowns;
    protected List<PotionEffect> passives;
    protected ArmorPlugin plugin;
    private boolean enabled;

    public boolean isEnabled(){
        return enabled;
    }
    public AbstractClass(final ConfigurationSection section) {
        this.clickEffects = new HashMap<Material, ClickEffect>();
        this.heldEffects = new HashMap<Material, ClickEffect>();
        this.cooldowns = new WeakHashMap<Player, Long>();
        this.plugin = ArmorPlugin.getInstance();
        this.passives = new ArrayList<PotionEffect>();
        enabled = section.getBoolean("enabled");
        if(!enabled){
            return;
        }
        final ConfigurationSection clickSection = section.getConfigurationSection("click");
        if (clickSection != null) {
            for (final String keys : clickSection.getKeys(false)) {
                final ClickEffect effect = new ClickEffect(clickSection.getConfigurationSection(keys));
                this.clickEffects.put(effect.getItem(), effect);
            }
        }
        final ConfigurationSection heldSection = section.getConfigurationSection("held");
        if (heldSection != null) {
            for (final String keys2 : heldSection.getKeys(false)) {
                final ClickEffect effect2 = new ClickEffect(heldSection.getConfigurationSection(keys2));
                this.heldEffects.put(effect2.getItem(), effect2);
            }
        }
        if (section.getConfigurationSection("passive") != null) {
            this.passives = PotionUtils.paraseList(section.getConfigurationSection("passive"));
        }
    }
    
    public void applyPassive(final Player player) {
        for (final PotionEffect effect : this.passives) {
            PotionUtils.addConcideringLevel(player, effect);
        }
    }
    
    public void applyHeldEffect(final Player player, final ItemStack item) {
        if (item != null && this.heldEffects.containsKey(item.getType())) {
            for (final PotionEffect effect : this.heldEffects.get(item.getType()).getEffects()) {
                this.applyEffect(player, effect);
            }
        }
    }
    
    public boolean applyClickEffect(final Player player, final ItemStack item) {
        if (item == null || !this.clickEffects.containsKey(item.getType())) {
            return false;
        }
        if (this.cooldowns.containsKey(player)) {
            final long value = this.cooldowns.get(player);
            if (value > System.currentTimeMillis()) {
                player.sendMessage(this.plugin.getText("CLASS_COOLDOWN").replace("{duration}", DurationFormatUtils.formatDurationWords(value - System.currentTimeMillis(), true, true)));
                return false;
            }
        }
        final ClickEffect clickeffect = this.clickEffects.get(item.getType());
        final Bard bard = this.plugin.getBard();
        if (bard.getPower(player) < clickeffect.getRequiredPower()) {
            player.sendMessage(this.plugin.getText("CLASS_NO_ENERGY").replace("{required_power}", String.valueOf(clickeffect.getRequiredPower())).replace("{power}", String.valueOf(bard.getPower(player))));
            return false;
        }
        bard.setPower(player, bard.getPower(player) - clickeffect.getRequiredPower());
        for (final PotionEffect effect : clickeffect.getEffects()) {
            this.applyEffect(player, effect);
        }
        this.applyHeldEffect(player, player.getItemInHand());
        this.cooldowns.put(player, System.currentTimeMillis() + clickeffect.getCooldown() * 1000);
        return true;
    }
    
    public void applyEffect(final Player player, final PotionEffect effect) {
        ArmorPlugin.getInstance().addEffectWithReturn(player, effect);
    }
    
    public List<PotionEffect> getPassives() {
        return this.passives;
    }
}
