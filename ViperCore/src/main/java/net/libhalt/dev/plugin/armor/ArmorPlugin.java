package net.libhalt.dev.plugin.armor;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.event.*;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.*;
import org.bukkit.entity.*;
import net.libhalt.dev.plugin.armor.kit.*;
import net.syuu.common.event.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import net.libhalt.dev.plugin.armor.listener.*;
import net.libhalt.dev.plugin.armor.task.*;
import org.bukkit.scheduler.*;
import java.util.*;
import net.libhalt.dev.plugin.armor.utils.*;
import org.bukkit.potion.*;
import org.bukkit.*;

public class ArmorPlugin extends Manager
{

    public static ArmorPlugin getInstance(){
        return INSTANCE;
    }
    private static ArmorPlugin INSTANCE;
    private WeakHashMap<Player, Armor> activeKit = new WeakHashMap<Player, Armor>();;
    private Map<Armor, AbstractClass> classes = new HashMap<Armor, AbstractClass>();
    private Bard bard;
    private Miner miner;
    private Archer archer;
    private Rouge rouge;
    private int classCooldown;

    public ArmorPlugin(HCFactionPlugin plugin) {
        super(plugin);
    }

    private ConfigurationWrapper config;
    public Configuration getConfig(){
        return config.getConfig();
    }
    public void init() {
        INSTANCE = this;
        getPlugin().getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onScoreboard(net.libhalt.bukkit.kaede.event.ScoreboardTextAboutToUpdateEvent event){
                if(bard.isEnabled() && event.getText() != null && event.getText().contains("%energy%")) {
                    int power = bard.getPower(event.getPlayer());
                    if(power >= 1){
                        event.setText(event.getText().replace("%energy%" , String.valueOf(power - 1)));
                    }else{
                        event.setText(null);
                    }
                }
                if(bard.isEnabled() && event.getText() != null && event.getText().contains("%class%")){
                    Armor active = getActiveArmor(event.getPlayer());
                    if(active != null){
                        event.setText(event.getText().replace("%class%" , active.toKit()));
                    }else{
                        event.setText(null);
                    }
                }
            }
        } , this.getPlugin());
        config = new ConfigurationWrapper("armor-class.yml", this.getPlugin());
        this.bard = new Bard(this.getConfig().getConfigurationSection("bard"));
        this.miner = new Miner(this.getConfig().getConfigurationSection("miner"));
        this.archer = new Archer(this.getConfig().getConfigurationSection("archer"));
        this.rouge = new Rouge(this.getConfig().getConfigurationSection("rouge"));
        this.classes.put(Armor.GOLD, this.bard);
        this.classes.put(Armor.IRON, this.miner);
        this.classes.put(Armor.LEATHER, this.archer);
        this.classes.put(Armor.CHAIN_MAIL, this.rouge);
        getPlugin().getServer().getPluginManager().registerEvents((Listener)new ClassListener(this), getPlugin());
        getPlugin().getServer().getPluginManager().registerEvents((Listener)new ArcherListener(this), getPlugin());
        getPlugin().getServer().getPluginManager().registerEvents((Listener)new RougeListener(this), getPlugin());
        new TaskClassChecker(this).runTaskTimer(this.getPlugin(), 60L, 60L);
        new BukkitRunnable() {
            public void run() {
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    if (ArmorPlugin.this.activeKit.get(player) == Armor.GOLD) {
                        ArmorPlugin.this.bard.setPower(player, Math.min(101, ArmorPlugin.this.bard.getPower(player) + 1));
                        if (ArmorPlugin.this.bard.getPower(player) % 10 != 0) {
                            continue;
                        }
                        player.sendMessage(ArmorPlugin.this.getText("BARD_POWEER").replace("{power}", String.valueOf(ArmorPlugin.this.bard.getPower(player))));
                    }
                    else {
                        ArmorPlugin.this.bard.setPower(player, 0);
                    }
                }
            }
        }.runTaskTimer(this.getPlugin(), 20L, 20L);
    }
    
    public boolean hasActiveKit(final Player player) {
        return this.activeKit.containsKey(player);
    }
    
    public void setActive(final Player player, final Armor armor) {
        if (armor == null) {
            this.activeKit.remove(player);
        }
        else {
            this.activeKit.put(player, armor);
        }
    }
    
    public void addEffectWithReturn(final Player player,  PotionEffect effect) {
        if (PotionUtils.canAddConcideringLevel(player, effect) && player.hasPotionEffect(effect.getType())) {
            final PotionEffect temp = PotionUtils.getPotionEffect(player, effect.getType());
            final PotionEffect pre = new PotionEffect(temp.getType(), temp.getDuration(), temp.getAmplifier(), temp.isAmbient());
            new ReturnEffectTask(player, pre).runTaskLater(getPlugin(), (long)effect.getDuration() - 20);
            effect = new PotionEffect(effect.getType() , effect.getDuration() + 20, effect.getAmplifier() , effect.isAmbient());
        }
        PotionUtils.addConcideringLevel(player, effect);
    }
    
    public void reapply(final Player player) {
        final Armor armor = this.activeKit.get(player);
        if (armor != null && ItemStackUtils.isWearingFull(player, armor)) {
            for (final PotionEffect effect : this.getHandler(armor).getPassives()) {
                if (effect.getType().equals((Object)PotionEffectType.NIGHT_VISION) && player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                    continue;
                }
                PotionUtils.addConcideringLevel(player, effect);
            }
        }
    }
    
    public AbstractClass getHandler(final Armor armor) {
        return this.classes.get(armor);
    }
    
    public Armor getActiveArmor(final Player player) {
        return this.activeKit.get(player);
    }
    
    public Bard getBard() {
        return this.bard;
    }
    
    public Archer getArcher() {
        return this.archer;
    }
    
    public Rouge getRouge() {
        return this.rouge;
    }
    
    public String getText(final String input) {
        return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages." + input));
    }
    
    public int getClassCooldown() {
        return this.classCooldown;
    }
    
    class ReturnEffectTask extends BukkitRunnable
    {
        private Player player;
        private PotionEffect pre;
        
        public ReturnEffectTask(final Player player, final PotionEffect pre) {
            this.player = player;
            this.pre = pre;
        }
        
        public void run() {
            ArmorPlugin.this.reapply(this.player);
            if (this.player.hasPotionEffect(this.pre.getType())) {
                final PotionEffect effect = PotionUtils.getPotionEffect(this.player, this.pre.getType());
                if (effect.getAmplifier() == this.pre.getAmplifier()) {
                    if (this.pre.getDuration() > effect.getDuration()) {
                        //this.player.removePotionEffect(this.pre.getType());
                        this.player.addPotionEffect(this.pre, true);
                    }
                }
                else {
                    //this.player.removePotionEffect(this.pre.getType());
                    this.player.addPotionEffect(this.pre, true);
                }
            }
            else {
                this.player.addPotionEffect(this.pre , true);
            }
            final Armor armor = ArmorPlugin.this.getActiveArmor(this.player);
            if (armor != null) {
                final AbstractClass kit = ArmorPlugin.this.getHandler(armor);
                kit.applyPassive(this.player);
            }
        }
    }
}
