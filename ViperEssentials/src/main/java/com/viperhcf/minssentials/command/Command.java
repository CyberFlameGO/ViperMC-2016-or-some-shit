package com.viperhcf.minssentials.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.viperhcf.minssentials.command.condition.Condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command implements CommandExecutable {

    private List<String> knownNames = new ArrayList<String>();
    private List<Condition> conditions = new ArrayList<Condition>();
    private List<Command> subCommands = new ArrayList<Command>();

    public Command(String name) {
        addAllias(name);
    }

    public void addAllias(String command) {
        String lowercase = command.toLowerCase();
        if (knownNames.contains(lowercase)) {
            throw new IllegalArgumentException("Allias can not duplicate");
        }
        knownNames.add(lowercase);
    }

    public List<String> getAllias() {
        return knownNames;
    }

    public String getName() {
        return knownNames.get(0);
    }


    public void addCondition(Condition condition, String... args) {
        if (args.length == 0) {
            conditions.add(condition);
            return;
        }
        String lowercase = args[0].toLowerCase();

        Command sub = null;
        for (Command commands : subCommands) {
            if (commands.knownNames.contains(lowercase)) {
                sub = commands;
            }
        }
        if (sub != null) {
            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            sub.addCondition(condition, newArgs);
            return;
        }
        conditions.add(condition);
    }

    public void addSubCommand(Command command) {
        subCommands.add(command);
    }

    @Override
	public final void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;

        for (Condition condition : conditions) {
            if (!condition.canProcess(player, args)) {
                return;
            }
        }
        if (args.length == 0) {
            if (args.length >= minargs()) {
                proccess(player, args);
            } else {
                player.sendMessage(describeUsage(player));
            }
            return;
        }
        String lowercase = args[0].toLowerCase();

        Command sub = null;
        for (Command commands : subCommands) {
            if (commands.knownNames.contains(lowercase)) {
                sub = commands;
            }
        }
        if (sub != null) {
            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            sub.execute(player, newArgs);
            return;
        }
        if (args.length >= minargs()) {
            proccess(player, args);
        } else {
            player.sendMessage(describeUsage(player));
        }
    }


    public void proccess(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(describeUsage(player));
            player.sendMessage(ChatColor.RED + describeSubCommands());
        } else {
            player.sendMessage(ChatColor.RED + "No Such Command");
            player.sendMessage(ChatColor.RED + describeSubCommands());

        }

    }

    private String describeSubCommands() {
        if (subCommands.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("/");
        builder.append(knownNames.get(0));
        builder.append(" <");
        for (Command commands : subCommands) {
            builder.append(commands.knownNames.get(0));
            builder.append(", ");
        }
        builder.delete(builder.length() - 2, builder.length());
        builder.append(">");
        return builder.toString();
    }

    public int minargs() {
        return 0;
    }

    public List<String> suggest(Player player, String[] args) {
        if (subCommands.isEmpty()) {
            return null;
        }
        String lowercase = args[0].toLowerCase();

        Command sub = null;
        for (Command commands : subCommands) {
            if (commands.knownNames.contains(lowercase)) {
                sub = commands;
            }
        }
        if (sub != null) {
            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            return sub.suggest(player, newArgs);
        }
        List<String> list = new ArrayList<String>();
        for (Command commands : subCommands) {
            list.add(commands.knownNames.get(0));
        }
        return list;
    }

    public String describeUsage(Player player) {
        return ChatColor.RED + "Command Argument Too Short!";
    }
}
