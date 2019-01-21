package com.igodlik3.viperutils;

import net.syuu.common.command.*;
import net.syuu.common.command.condition.PermissionCondition;
import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.faction.FactionDataManager;
import net.syuu.popura.faction.bean.Faction;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;
import org.bukkit.command.*;
import org.bukkit.*;

public class CmdMuteFaction extends net.syuu.common.command.Command
{
    private Configuration config;
    
    public CmdMuteFaction() {
        super("mute");
        this.config = (Configuration)ViperUtils.getInstance().getConfig();
        addCondition(new PermissionCondition("factions.mute"));
    }

    public void proccess(final Player player, final String[] args) {
        final FactionDataManager factionDataManager = ((PopuraPlugin)PopuraPlugin.getPlugin(PopuraPlugin.class)).getPopura().getFactionDataManager();
        Faction faction = factionDataManager.getPlayer(player).getFaction();
        if(args.length > 0){
            faction = factionDataManager.getFaction(args[0]);
        }
        if (faction == null) {
            return;
        }
        final String time = args[1];
        final String reason = args[2];
        for (final Player bukkitPlayer : faction.getOnlineMembers()) {
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), this.config.getString("mute-cmd").replaceAll("%TIME%", time).replaceAll("%PLAYER%", bukkitPlayer.getName()).replaceAll("%REASON%", reason));
        }
        if (this.config.getBoolean("Broadcast.enabled")) {
            Bukkit.broadcastMessage(this.color(this.config.getString("Broadcast.message").replaceAll("%FACTION%", faction.getName()).replaceAll("%REASON%", reason)).replaceAll("%PLAYER%", player.getName()));
        }
    }
    

    private String color(final String stg) {
        return ChatColor.translateAlternateColorCodes('&', stg);
    }
}
