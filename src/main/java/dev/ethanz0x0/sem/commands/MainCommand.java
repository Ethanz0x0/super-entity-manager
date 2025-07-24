package dev.ethanz0x0.sem.commands;

import dev.ethanz0x0.sem.Config;
import dev.ethanz0x0.sem.SuperEntityManager;
import dev.ethanz0x0.sem.entity.EntityBlacklist;
import dev.ethanz0x0.sem.translations.Translation;
import dev.ethanz0x0.sem.translations.TranslationLibrary;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class MainCommand extends Command  {

    public static final MainCommand INSTANCE = new MainCommand();

    private static final SuperEntityManager plugin = SuperEntityManager.getInstance();

    private MainCommand() {
        super("superentitymanager");
        setAliases(Arrays.asList("entitymanager", "sem"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            info(sender);
            return true;
        }

        if (!sender.hasPermission("superentitymanager.command.reload")) {
            Translation.sendMessage(sender, Level.WARNING, "command.no-permission", label);
            return true;
        }

        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "info": {
                    info(sender);
                    return true;
                }
                case "reload": {
                    reload(sender);
                    return true;
                }
                case "blacklist": {
                    blacklistHelp(sender);
                    return true;
                }
            }
        }

        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "blacklist": {
                    switch (args[1].toLowerCase()) {
                        case "list": {
                            blacklistList(sender);
                            return true;
                        }
                        case "ignorespawner": {
                            blacklistIgnoreSpawner(sender);
                            return true;
                        }
                        case "ignorespawnegg": {
                            blacklistIgnoreSpawnEgg(sender);
                            return true;
                        }
                    }
                    blacklistHelp(sender);
                    return true;
                }
            }
        }
        return false;
    }

    private void info(CommandSender sender) {
        Translation.sendMessage(sender, Level.INFO, "command.plugin-info",
                plugin.getDescription().getVersion(), "Ethanz0x0");
    }

    private void reload(CommandSender sender) {
        Translation.sendMessage(sender, Level.INFO, "command.plugin-reloading");
        long start = System.currentTimeMillis();
        Config.reloadConfig();
        TranslationLibrary.loadTranslations();
        EntityBlacklist.load();
        Translation.sendMessage(sender, Level.INFO, "command.plugin-reloaded",
                (System.currentTimeMillis() - start));
    }

    private void blacklistHelp(CommandSender sender) {
        Translation.sendMessage(sender, Level.INFO, "command.entity-blacklist.help");
    }

    private void blacklistList(CommandSender sender) {
        List<String> blacklist = new ArrayList<>();
        for (EntityType entityType : EntityBlacklist.getBlacklisted()) {
            blacklist.add(entityType.name());
        }
        Translation.sendMessage(sender, Level.INFO, "command.entity-blacklist.list",
                String.join("&7, &f", blacklist));
    }

    private void blacklistIgnoreSpawner(CommandSender sender) {
        EntityBlacklist.toggleIgnoreSpawner();
        Translation.sendMessage(sender, Level.INFO, "command.entity-blacklist.ignore-spawner." +
                (EntityBlacklist.isIgnoreSpawner() ? "on" : "off"));
    }

    private void blacklistIgnoreSpawnEgg(CommandSender sender) {
        EntityBlacklist.toggleIgnoreSpawnEgg();
        Translation.sendMessage(sender, Level.INFO, "command.entity-blacklist.ignore-spawn-egg." +
                (EntityBlacklist.isIgnoreSpawner() ? "on" : "off"));
    }

}
