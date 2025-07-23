package dev.ethanz0x0.sem;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Config {

    private static final SuperEntityManager plugin = SuperEntityManager.getInstance();
    private static final File dataFolder = plugin.getDataFolder();

    private static File configFile;
    private static FileConfiguration config;

    public static boolean initConfig() {
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        if (!configFile.exists()) {
            try (InputStream in = Config.class.getResourceAsStream("/config.yml")) {
                if (in == null) {
                    plugin.getLogger().severe("Internal error: input stream is null while creating configuration, " +
                            "please contact the plugin developer.");
                    return false;
                }
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create config.yml.");
                return false;
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        return true;
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save config.yml.");
        }
    }

    public static void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}
