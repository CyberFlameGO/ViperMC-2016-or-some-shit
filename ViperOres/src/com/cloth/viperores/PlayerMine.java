package com.cloth.viperores;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;


/**
 * Created by Brennan on 6/29/2017.
 */
public class PlayerMine implements Listener
{
    @EventHandler
    public void onMine(BlockBreakEvent event)
    {
        if(ViperOres.getInstance().playerdata.getInt(event.getPlayer().getName().toLowerCase() + ".multiplier") > 0)
        {
            if(event.getPlayer().getItemInHand() != null){
                if(event.getPlayer().getItemInHand().hasItemMeta()){
                    if(event.getPlayer().getItemInHand().getItemMeta().hasEnchants()){
                        if(event.getPlayer().getItemInHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)){
                            return;
                        }
                    }
                }
            }

            int multiplier = ViperOres.getInstance().playerdata.getInt(event.getPlayer().getName().toLowerCase() + ".multiplier");
            if(multiplier > 1){
                for(ItemStack i : event.getBlock().getDrops()){
                    if(i != null){
                        ItemStack tempItem = i;
                        tempItem.setAmount(tempItem.getAmount() * multiplier);
                        tempItem.setAmount(tempItem.getAmount() - 1);
                        event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), tempItem);
                    }
                }
            }
        }
    }
}
