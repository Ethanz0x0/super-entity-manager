package dev.ethanz0x0.sem;

import org.bukkit.plugin.java.JavaPlugin;

public class SuperEntityManager extends JavaPlugin {

    private static SuperEntityManager instance;

    public static SuperEntityManager getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("----------------------------------------");
        getLogger().info("Welcome to use Super Entity Manager!");
        getLogger().info("Created by: Ethanz0x0");
        getLogger().info("Current version: " + getDescription().getVersion());
        getLogger().info("----------------------------------------");

        getLogger().info("Initializing plugin, please wait...");

        if (Config.initConfig()) {
            getLogger().info("Configuration file loaded.");
        } else {
            getLogger().severe("Error while loading configuration, plugin will shutdown...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


    }

    @Override
    public void onDisable() {

    }
}
