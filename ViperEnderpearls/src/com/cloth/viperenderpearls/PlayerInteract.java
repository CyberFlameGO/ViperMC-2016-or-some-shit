package com.cloth.viperenderpearls;

import com.cloth.viperenderpearls.manager.FilesCreator;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteract
        implements Listener
{
    FilesCreator cInstance = FilesCreator.getInstance();

    @EventHandler
    public void blockInteractEvent(PlayerInteractEvent e)
    {
        if ((this.cInstance.getConfig().getBoolean("anti-glitch")) &&
                (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) &&
                (e.getItem() != null) && (e.getItem().getType() == Material.ENDER_PEARL)) {
            e.setCancelled(true);
        }
    }
}
