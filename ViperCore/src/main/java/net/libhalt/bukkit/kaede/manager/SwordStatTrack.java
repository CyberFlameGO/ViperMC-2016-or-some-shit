package net.libhalt.bukkit.kaede.manager;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.Manager;
import org.bukkit.plugin.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.event.*;
import java.util.*;

public class SwordStatTrack extends Manager implements Listener
{
    //private final String IDENTIFIER;
    private final String IDENTIFIER_2;

    public SwordStatTrack(final HCFactionPlugin plugin) {
        super(plugin);
        //this.IDENTIFIER = ChatColor.RESET.toString() + ChatColor.RESET + ChatColor.AQUA + ChatColor.RESET;
        this.IDENTIFIER_2 = ChatColor.RESET.toString() + ChatColor.AQUA + ChatColor.RESET + ChatColor.RESET;

    }
    
    @Override
    public void init() {
        this.getPlugin().getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.getPlugin());
    }
    
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final Player killer = event.getEntity().getKiller();
        final Player entity = event.getEntity();
        if (killer != null) {
            final ItemStack item = killer.getItemInHand();
            if (item != null && item.getType() != Material.AIR) {
                List<String> lore = null;
                if (item.hasItemMeta()) {
                    final ItemMeta meta = item.getItemMeta();
                    if (meta.hasLore()) {
                        lore = (List<String>)meta.getLore();
                    }
                }
                if (lore == null) {
                    lore = new ArrayList<String>();
                    lore.add(this.IDENTIFIER_2 + ChatColor.GRAY + "Total Kill(s): " + ChatColor.RED + 1);
                    //lore.add(this.IDENTIFIER + ChatColor.RED + killer.getName() + ChatColor.YELLOW + " killed " + ChatColor.RED + entity.getName());
                    final ItemMeta meta = item.getItemMeta();
                    meta.setLore((List)lore);
                    item.setItemMeta(meta);
                }
                else {
                    int kill = 0;
                    String input = ChatColor.stripColor(getTotalKillLore(lore));
                    try{
                        kill = Integer.valueOf(input.replace("Total Kill:" ,  "").replace("Total Kill(s):" ,  "").trim());//(s) and without for legacy reasons
                    }catch (NumberFormatException e){
                        System.err.println("Could not parse lore for: " + input);
                    }
                    kill++;
                    final List<String> otherLore = this.getOtherLore(lore);
                    //killLore.add(0 , this.IDENTIFIER + ChatColor.RED + killer.getName() + ChatColor.YELLOW + " killed " + ChatColor.RED + entity.getName());
                    final List<String> realLore = new ArrayList<String>();
                    realLore.addAll(otherLore);
                    realLore.add(this.IDENTIFIER_2 + ChatColor.GRAY + "Total Kill(s):" + ChatColor.RED + kill);
                    final ItemMeta meta2 = item.getItemMeta();
                    meta2.setLore((List)realLore);
                    item.setItemMeta(meta2);
                }
            }
        }
    }
    public String getTotalKillLore(List<String> lore){
        for (final String string : lore) {
            if (string.startsWith(this.IDENTIFIER_2)) {
                return string;
            }
        }
        return "";
    }
    public List<String> getOtherLore(final List<String> lore) {
        final List<String> newLore = new ArrayList<String>();
        for (final String string : lore) {
            if (!string.startsWith(this.IDENTIFIER_2)) {
                newLore.add(string);
            }
        }
        return newLore;
    }

}
