package cloth.xp;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Brennan on 6/16/2017.
 */
public class XpBottleEvent implements Listener
{

    HCFactionPlugin plugin;
    public XpBottleEvent(HCFactionPlugin instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if(e.getPlayer().getItemInHand() != null)
            {
                if(e.getPlayer().getItemInHand().getType() == Material.EXP_BOTTLE)
                {
                    if(e.getPlayer().getItemInHand().hasItemMeta())
                    {
                        if(e.getPlayer().getItemInHand().getItemMeta().hasDisplayName())
                        {
                            String xp_bottle_name = plugin.configdata.getConfigMessage("xp-bottle-name").replaceAll("&","§");
                            if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(xp_bottle_name))
                            {
                                e.setCancelled(true);

                                if(e.getPlayer().getItemInHand().getAmount() > 1) {
                                    e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
                                } else {
                                    e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                                }

                                String message = plugin.configdata.getConfigMessage("xp-bottle-received").replaceAll("&", "§");
                                e.getPlayer().sendMessage(message);
                                e.getPlayer().setLevel(e.getPlayer().getLevel() + 30);
                                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
