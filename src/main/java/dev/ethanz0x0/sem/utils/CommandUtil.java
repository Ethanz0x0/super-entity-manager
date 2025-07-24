package dev.ethanz0x0.sem.utils;

import dev.ethanz0x0.sem.SuperEntityManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

public class CommandUtil {

    public static CommandMap getCommandMap() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            SuperEntityManager.getInstance().getLogger().severe("Failed to reflect command map.");
            return null;
        }
    }

    public static void registerCommand(Plugin plugin, Command command) {
        CommandMap commandMap = getCommandMap();
        if (commandMap != null) {
            commandMap.register(plugin.getName(), command);
        }
    }
}
