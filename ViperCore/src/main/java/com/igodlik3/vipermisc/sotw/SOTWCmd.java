package com.igodlik3.vipermisc.sotw;

import com.igodlik3.vipermisc.*;
import net.libhalt.bukkit.kaede.HCFactionPlugin;
import org.bukkit.command.*;
import com.igodlik3.modmode.utils.*;
import org.bukkit.*;

public class SOTWCmd implements CommandExecutor
{
    private SOTWManager sotwManager;
    
    public SOTWCmd() {
        this.sotwManager = Misc.getInstance().getSotwManager();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("Viper.SOTW")) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(Utils.color("&e/sotw start &7- &6Start the SOTW"));
            sender.sendMessage(Utils.color("&e/sotw stop &7- &6Stop the SOTW"));
        }
        else if (args[0].equalsIgnoreCase("start")) {
            if (this.sotwManager.isSOTWActive()) {
                String active = HCFactionPlugin.getInstance().messages.getString("sotw-active").replaceAll("&", "§");
                sender.sendMessage(active);
                return true;
            }
            if(args.length >= 2) {
                this.sotwManager.activeSOTW(Integer.valueOf(args[1]));

            }else{
                this.sotwManager.activeSOTW();
            }
        }
        else if (args[0].equalsIgnoreCase("stop")) {
            if (!this.sotwManager.isSOTWActive()) {
                String inactive = HCFactionPlugin.getInstance().messages.getString("sotw-inactive").replaceAll("&", "§");
                sender.sendMessage(inactive);
                return true;
            }

            String stopped = HCFactionPlugin.getInstance().messages.getString("sotw-stopped").replaceAll("&", "§");
            this.sotwManager.getTimer().setTimerEnd(0L);
            Bukkit.broadcastMessage(stopped);
        }
        return true;
    }
}
