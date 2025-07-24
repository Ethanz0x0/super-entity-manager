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

    private static File entityBlacklistFile;
    private static FileConfiguration entityBlacklist;

    public static boolean initConfig() {
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        // config.yml
        configFile = new File(dataFolder, "config.yml");
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

        // entity_blacklist.yml
        entityBlacklistFile = new File(dataFolder, "entity_blacklist.yml");
        if (!entityBlacklistFile.exists()) {
            try {
                entityBlacklistFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create entity_blacklist.yml.");
                return false;
            }
        }
        entityBlacklist = YamlConfiguration.loadConfiguration(entityBlacklistFile);
        return true;
    }


    public static FileConfiguration getConfig() {
        return config;
    }

    public static FileConfiguration getEntityBlacklist() {
        return entityBlacklist;
    }

    public static void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save config.yml.");
        }
    }

    public static void saveEntityBlacklist() {
        try {
            entityBlacklist.save(entityBlacklistFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save entity_blacklist.yml.");
        }
    }

    public static void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static void reloadEntityBlacklist() {
        entityBlacklist = YamlConfiguration.loadConfiguration(entityBlacklistFile);
    }
}
