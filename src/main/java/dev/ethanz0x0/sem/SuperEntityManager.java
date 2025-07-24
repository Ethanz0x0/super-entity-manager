package dev.ethanz0x0.sem;

import dev.ethanz0x0.sem.commands.MainCommand;
import dev.ethanz0x0.sem.translations.TranslationLibrary;
import dev.ethanz0x0.sem.utils.CommandUtil;
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

        if (TranslationLibrary.loadTranslations()) {
            getLogger().severe("Error while loading translations, plugin will shutdown...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (CommandUtil.getCommandMap() == null) {
            getLogger().severe("Error while reflecting command map, plugin will shutdown...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        CommandUtil.registerCommand(this, MainCommand.INSTANCE);

        getLogger().info("Plugin fully loaded! Enjoy!");
    }

    @Override
    public void onDisable() {

    }
}
