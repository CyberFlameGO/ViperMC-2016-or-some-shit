package com.viperhcf.minssentials.command.condition;

import org.bukkit.entity.Player;

public interface Condition {

    boolean canProcess(Player player, String[] args);
}
