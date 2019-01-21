package cloth.pearls;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.manager.EnderPearlManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Brennan on 6/27/2017.
 */
public class PearlInteractEvent implements Listener
{

    HCFactionPlugin plugin;
    public PearlInteractEvent(HCFactionPlugin instance){
        this.plugin = instance;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        if(p.getItemInHand() != null)
        {
            if(p.getItemInHand().getType() == Material.ENDER_PEARL)
            {
                if(e.getAction() == Action.RIGHT_CLICK_AIR)
                {
                    if(!EnderPearlManager.isCooldownActive(e.getPlayer()))
                    {
                        e.setCancelled(true);

                        if(p.getItemInHand().getAmount() == 1)
                        {
                            p.setItemInHand(new ItemStack(Material.AIR));
                        } else {
                            p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
                        }

                        EnderPearl pearl = e.getPlayer().launchProjectile(EnderPearl.class);
                        return;
                    }
                }
            }
        }
    }
}
