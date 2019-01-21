package com.viperhcf.minssentials.command.condition;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class PermissionCondition implements Condition {

    private final String permission;

    @Override
    public boolean canProcess(Player player, String[] args) {
        if (!player.hasPermission(permission)) {
            player.sendMessage(ChatColor.RED + "NoPermission");
            return false;
        }
        return true;
    }
}
