package cloth.portals;

import com.igodlik3.subclaims.FactionsUtils;
import net.libhalt.bukkit.kaede.HCFactionPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Created by Brennan on 6/15/2017.
 */
public class NetherPortalEvent implements Listener
{

    HCFactionPlugin plugin;
    public NetherPortalEvent(HCFactionPlugin instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onTeleport(PlayerPortalEvent e)
    {
        if(FactionsUtils.getFactionAt(e.getPlayer().getLocation()) != null)
        {
            String name = FactionsUtils.getFactionAt(e.getPlayer().getLocation()).getName();
            if(name.equals("Spawn"))
            {
                if(e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL){
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(plugin.configdata.getConfigMessage("nether-portal-in-spawn").replaceAll("&", "§"));
                    return;
                }
            }
        }
    }
}
