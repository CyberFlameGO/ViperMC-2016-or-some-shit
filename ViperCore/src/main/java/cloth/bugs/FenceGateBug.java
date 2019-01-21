package cloth.bugs;

import com.igodlik3.subclaims.FactionsUtils;
import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.syuu.popura.faction.FactionType;
import net.syuu.popura.faction.bean.Faction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Brennan on 6/18/2017.
 */
public class FenceGateBug implements Listener
{

    HCFactionPlugin plugin;
    public FenceGateBug(HCFactionPlugin instance){
        this.plugin = instance;
    }

    @EventHandler
    public void openDoor(PlayerInteractEvent e)
    {
        final Player p = e.getPlayer();
        if(e.getClickedBlock() != null)
        {
            if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            {
                if(e.getClickedBlock().getType() == Material.FENCE_GATE)
                {
                    if(FactionsUtils.getFactionAt(e.getClickedBlock().getLocation()) != null)
                    {
                        if(FactionsUtils.getFactionAt(e.getClickedBlock().getLocation()).getFactionType() != FactionType.WARZONE)
                        {
                            Faction pFaction = FactionsUtils.getFaction(p);
                            Faction lFaction = FactionsUtils.getFactionAt(e.getClickedBlock().getLocation());

                            if(pFaction == null)
                            {
                                if(p.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.AIR || p.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.FENCE_GATE)
                                {
                                    return;
                                }

                                p.setVelocity(p.getLocation().getDirection().multiply(-1).setY(0));
                                return;
                            }

                            if(!pFaction.equals(lFaction))
                            {
                                if(p.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.AIR || p.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.FENCE_GATE)
                                {
                                    return;
                                }

                                p.setVelocity(p.getLocation().getDirection().multiply(-1).setY(0));
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
