package cloth.xp;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Brennan on 6/16/2017.
 */
public class XpBottleCommand implements CommandExecutor
{

    HCFactionPlugin plugin;
    public XpBottleCommand(HCFactionPlugin instance)
    {
        this.plugin = instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player))
        {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player p = (Player) sender;

        if(p.getLevel() < 30)
        {
            String message = plugin.configdata.getConfigMessage("xp-bottle-insufficient").replaceAll("&", "§");
            p.sendMessage(message);
            return true;
        }

        if(p.getInventory().firstEmpty() == -1)
        {
            String message = plugin.configdata.getConfigMessage("xp-bottle-inventory").replaceAll("&", "§");
            p.sendMessage(message);
            return true;
        }

        String message = plugin.configdata.getConfigMessage("xp-bottle-created").replaceAll("&", "§");
        p.sendMessage(message);
        p.setLevel(p.getLevel() - 30);
        giveBottle(p);
        return true;
    }

    public void giveBottle(Player p){
        String bottlename = plugin.configdata.getConfigMessage("xp-bottle-name").replaceAll("&", "§");
        ItemStack bottle = new ItemStack(Material.EXP_BOTTLE);
        ItemMeta meta = bottle.getItemMeta();
        meta.setDisplayName(bottlename);
        bottle.setItemMeta(meta);
        p.getInventory().addItem(bottle);
        return;
    }
}
