package dev.ethanz0x0.sem.util;

import org.bukkit.ChatColor;

public class MessageUtil {

    public static String parseColors(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
