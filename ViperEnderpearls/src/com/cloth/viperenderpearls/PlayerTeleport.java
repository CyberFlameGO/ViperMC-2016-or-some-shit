package com.cloth.viperenderpearls;

import com.cloth.viperenderpearls.manager.FilesCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

public class PlayerTeleport
        implements Listener
{
    FilesCreator cInstance = FilesCreator.getInstance();

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e)
    {
        Player p = e.getPlayer();
        if (this.cInstance.getConfig().getBoolean("anti-glitch"))
        {
            if ((e.isCancelled()) || (e.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
                return;
            }

            if (e.getTo().getBlock().getType() == Material.FENCE_GATE)
            {
                e.setCancelled(true);
                p.sendMessage(ViperEnderpearls.toColor("&7(&aViper&7) You cannot teleport directly into a fencegate."));
                p.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                return;
            }

            Location target = e.getTo();
            target.setX(target.getBlockX() + 0.5D);
            target.setZ(target.getBlockZ() + 0.5D);
            e.setTo(target);
        }
    }
}
