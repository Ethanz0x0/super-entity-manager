package dev.ethanz0x0.sem.commands;

import dev.ethanz0x0.sem.Config;
import dev.ethanz0x0.sem.SuperEntityManager;
import dev.ethanz0x0.sem.entity.EntityBlacklist;
import dev.ethanz0x0.sem.entity.EntityQuery;
import dev.ethanz0x0.sem.translations.Translation;
import dev.ethanz0x0.sem.translations.TranslationLibrary;
import dev.ethanz0x0.sem.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
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
                case "query": {
                    queryHelp(sender);
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
                case "query": {
                    if (args[1].equalsIgnoreCase("most")) {
                        queryMost(sender);
                        return true;
                    }
                    queryHelp(sender);
                    return true;
                }
            }
        }

        if (args.length == 3) {
            switch (args[0].toLowerCase()) {
                case "query": {
                    if (args[1].equalsIgnoreCase("world")) {
                        queryWorld(sender, args[2]);
                        return true;
                    }
                    queryHelp(sender);
                    return true;
                }
            }
        }

        if (args.length > 2) {
            switch (args[0].toLowerCase()) {
                case "blacklist": {
                    EntityType entityType = getEntityTypeFromArguments(sender, args, 2);
                    if (entityType == null) {
                        return true;
                    }
                    switch (args[1].toLowerCase()) {
                        case "add": {
                            blacklistAdd(sender, entityType);
                            return true;
                        }
                        case "remove": {
                            blacklistRemove(sender, entityType);
                            return true;
                        }
                    }
                    blacklistHelp(sender);
                    return true;
                }
                case "query": {
                    switch (args[1].toLowerCase()) {
                        case "type": {
                            EntityType entityType = getEntityTypeFromArguments(sender, args, 2);
                            if (entityType == null) {
                                return true;
                            }
                            queryType(sender, entityType);
                            return true;
                        }
                        case "chunk": {
                            Chunk chunk = getChunkFromArguments(sender, args, 2);
                            if (chunk == null) {
                                return true;
                            }
                            queryChunk(sender, chunk);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private EntityType getEntityTypeFromArguments(CommandSender sender, String[] args, int startIndex) {
        String entityTypeString = String.join("_",
                new ArrayList<>(Arrays.asList(args).subList(startIndex, args.length))).toUpperCase();
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(entityTypeString);
        } catch (IllegalArgumentException e) {
            Translation.sendMessage(sender, Level.WARNING, "command.unknown-entity-type", entityTypeString);
            return null;
        }
        return entityType;
    }

    private Chunk getChunkFromArguments(CommandSender sender, String[] args, int startIndex) {
        if (args.length == startIndex + 1) {
            return getChunkFromString(sender, args[startIndex]);
        }
        if (args.length != startIndex + 3) {
            Translation.sendMessage(sender, Level.WARNING, "command.illegal-chunk", String.join(" ",
                    new ArrayList<>(Arrays.asList(args).subList(startIndex, args.length))));
            return null;
        }
        return getChunkFromString(sender, String.join(",",
                new ArrayList<>(Arrays.asList(args).subList(startIndex, args.length))));
    }

    private Chunk getChunkFromString(CommandSender sender, String chunkLoc) {
        if (!chunkLoc.contains(",") || StringUtil.countOccurrences(chunkLoc, ",") != 2) {
            Translation.sendMessage(sender, Level.WARNING, "command.illegal-chunk", chunkLoc);
            return null;
        }
        String[] locs = chunkLoc.split(",");
        World world = Bukkit.getWorld(locs[0]);
        if (world == null) {
            Translation.sendMessage(sender, Level.WARNING, "command.unknown-world", locs[0]);
            return null;
        }
        int x;
        int y;
        try {
            x = Integer.parseInt(locs[1]);
            y = Integer.parseInt(locs[2]);
        } catch (NumberFormatException e) {
            Translation.sendMessage(sender, Level.WARNING, "command.illegal-chunk", chunkLoc);
            return null;
        }
        Chunk chunk = world.getChunkAt(x, y);
        if (chunk == null) {
            Translation.sendMessage(sender, Level.WARNING, "command.unknown-chunk", chunkLoc);
        }
        return chunk;
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
                (EntityBlacklist.isIgnoreSpawnEgg() ? "on" : "off"));
    }

    private void blacklistAdd(CommandSender sender, EntityType entityType) {
        if (EntityBlacklist.getBlacklisted().contains(entityType)) {
            Translation.sendMessage(sender, Level.INFO, "command.entity-blacklist.added-already",
                    entityType.name());
            return;
        }
        EntityBlacklist.add(entityType);
        Translation.sendMessage(sender, Level.INFO, "command.entity-blacklist.added",
                entityType.name());
    }

    private void blacklistRemove(CommandSender sender, EntityType entityType) {
        if (!EntityBlacklist.getBlacklisted().contains(entityType)) {
            Translation.sendMessage(sender, Level.INFO, "command.entity-blacklist.removed-already",
                    entityType.name());
            return;
        }
        EntityBlacklist.remove(entityType);
        Translation.sendMessage(sender, Level.INFO, "command.entity-blacklist.removed",
                entityType.name());
    }

    private void queryHelp(CommandSender sender) {
        Translation.sendMessage(sender, Level.INFO, "command.query.help");
    }

    private void queryMost(CommandSender sender) {
        List<Chunk> most = EntityQuery.topChunksWithMostEntities();
        Translation.sendMessage(sender, Level.INFO, "command.query.most.title");
        for (Chunk chunk : most) {
            Translation.sendMessage(sender, Level.INFO, "command.query.most.entry",
                    chunk.getX() + ", " + chunk.getZ(),
                    EntityQuery.countByChunk(chunk), chunk.getWorld().getName());
        }
    }

    private void queryType(CommandSender sender, EntityType entityType) {
        Translation.sendMessage(sender, Level.INFO, "command.query.type-query",
                entityType.name(), EntityQuery.countByType(entityType));
    }

    private void queryWorld(CommandSender sender, String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            Translation.sendMessage(sender, Level.WARNING, "command.unknown-world", worldName);
            return;
        }
        Translation.sendMessage(sender, Level.INFO, "command.query.world-query",
                world.getName(), EntityQuery.countByWorld(world));
    }

    private void queryChunk(CommandSender sender, Chunk chunk) {
        Translation.sendMessage(sender, Level.INFO, "command.query.chunk-query",
                chunk.getX() + ", " + chunk.getZ(), EntityQuery.countByChunk(chunk));
    }

}
