package com.viperhcf.minssentials.command;


import org.bukkit.command.CommandSender;

public interface CommandExecutable {

    void execute(CommandSender sender, String[] args);

}
