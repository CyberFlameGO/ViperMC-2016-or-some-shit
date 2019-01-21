package com.viperhcf.minssentials.command;

import com.google.common.base.Preconditions;
import com.viperhcf.minssentials.command.annotation.SimpleCommand;
import com.viperhcf.minssentials.command.annotation.SimpleCommandWraper;
import com.viperhcf.minssentials.command.condition.Condition;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRegistry {

    private static CommandRegistry INSTANCE = new CommandRegistry();
    private CommandMap commandMap = getCommandMap();
    private Map<String, CommandExecutable> commands = new HashMap<String, CommandExecutable>();
    private CommandWraper commandWrapper = new CommandWraper();

    public static CommandRegistry getInstance() {
        return INSTANCE;
    }

    public void provideCondition(String command, Condition condition) {
        if (command.contains(" ")) {
            String[] split = command.split(" ");
            Preconditions.checkArgument(commands.containsKey(split[0]), "Could not find command " + split[0] + "whilist provideing condition " + condition.getClass().getName() + "available commands: " + commands.keySet());
            CommandExecutable commandObj = commands.get(split[0]);
            Preconditions.checkState(commandObj instanceof Command, "The registered command type could not provide condition");
            ((Command) commandObj).addCondition(condition, Arrays.copyOfRange(split, 1, split.length));
        } else {
            Preconditions.checkArgument(commands.containsKey(command), "Could not find command " + command + " whilst providing condition for it");
            CommandExecutable commandObj = commands.get(command);
            if (commandObj instanceof Command) {
                ((Command) commandObj).addCondition(condition);
            } else if (commandObj instanceof SimpleCommandWraper) {
                ((SimpleCommandWraper) commandObj).addCondition(condition);
            } else {
                Preconditions.checkState(false, "The registered command type could not provide condition");
            }
        }
    }

    @SneakyThrows
    private CommandMap getCommandMap()  {
        Field commandmap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        commandmap.setAccessible(true);
        return (CommandMap) commandmap.get(Bukkit.getServer());
    }

    public void registerCommand(Command object) {
        this.registerCommand(object, null);
    }

    public void registerCommand(Command object, String provider) {
        Plugin plugin = JavaPlugin.getProvidingPlugin(object.getClass());
        Preconditions.checkNotNull(plugin, "Class must be loaded from JavaPlugin");
        if (provider == null || provider.isEmpty()) {
            provider = plugin.getName();
        }
        commands.put(object.getName(), object);
        CustomCommand pluginCommand = newCustomCommand(object.getName(), plugin, object.getAllias().toArray(new String[object.getAllias().size()]));
        commandMap.register(provider, pluginCommand);
    }

    public void registerSimpleCommand(Object object) {
        this.registerSimpleCommand(object, null);
    }

    public void registerSimpleCommand(Object object, String provider) {
        Plugin plugin = JavaPlugin.getProvidingPlugin(object.getClass());
        Preconditions.checkNotNull(plugin, "Class must be loaded from JavaPlugin");
        if (provider == null || provider.isEmpty()) {
            provider = plugin.getName();
        }
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(SimpleCommand.class)) {
                SimpleCommand simple = method.getAnnotation(SimpleCommand.class);
                SimpleCommandWraper wrap = new SimpleCommandWraper(object, method, simple.requireop());
                commands.put(simple.name(), wrap);
                CustomCommand pluginCommand = newCustomCommand(simple.name(), plugin, simple.allias());
                commandMap.register(provider, pluginCommand);
            }
        }
    }

    private CustomCommand newCustomCommand(String name, Plugin plugin, String[] allias) {
        CustomCommand pluginCommand = new CustomCommand(name, plugin);
        if (allias.length != 0) {
            pluginCommand.setAliases(Arrays.asList(allias));
        }
        pluginCommand.setExecutor(commandWrapper);
        return pluginCommand;

    }

    public class CommandWraper implements org.bukkit.command.TabCompleter, CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            /*if(!(sender instanceof Player)){
                return true;
			}*/
            commands.get(command.getName()).execute(sender, args);
            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                return null;
            }
            CommandExecutable executable = commands.get(command.getName());
            if (executable instanceof Command) {
                return ((Command) executable).suggest(((Player) sender), args);
            }
            return null;
        }
    }

}
