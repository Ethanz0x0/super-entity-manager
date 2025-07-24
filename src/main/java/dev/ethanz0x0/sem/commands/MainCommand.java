package dev.ethanz0x0.sem.commands;

import dev.ethanz0x0.sem.Config;
import dev.ethanz0x0.sem.SuperEntityManager;
import dev.ethanz0x0.sem.translations.Translation;
import dev.ethanz0x0.sem.translations.TranslationLibrary;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
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
        if (!sender.hasPermission("superentitymanager.command.reload")) {
            Translation.sendMessage(sender, Level.WARNING, "command.no-permission", label);
            return true;
        }

        if (args.length == 0) {
            info(sender);
            return true;
        }

        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "info": {
                    info(sender);
                    return true;
                }
                case "reload": {
                    reloadPlugin(sender);
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

    private void reloadPlugin(CommandSender sender) {
        long start = System.currentTimeMillis();
        Config.reloadConfig();
        TranslationLibrary.loadTranslations();
        Translation.sendMessage(sender, Level.INFO, "command.plugin-reload",
                (System.currentTimeMillis() - start));
    }
}
