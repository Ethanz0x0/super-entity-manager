package dev.ethanz0x0.sem.entity;

import dev.ethanz0x0.sem.Config;
import dev.ethanz0x0.sem.SuperEntityManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class EntityBlacklist {

    private static final SuperEntityManager plugin = SuperEntityManager.getInstance();

    private static final List<EntityType> blacklisted = new ArrayList<>();
    private static boolean ignoreSpawner = false;
    private static boolean ignoreSpawnEgg = false;

    public static List<EntityType> getBlacklisted() {
        return blacklisted;
    }

    public static boolean isIgnoreSpawner() {
        return ignoreSpawner;
    }

    public static boolean isIgnoreSpawnEgg() {
        return ignoreSpawnEgg;
    }

    public static void load() {
        blacklisted.clear();
        List<String> blacklist = Config.getEntityBlacklist().getStringList("blacklist");
        if (blacklist.isEmpty()) {
            return;
        }
        for (String entity : blacklist) {
            try {
                blacklisted.add(EntityType.valueOf(entity.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Unknown entity type: " + entity.toLowerCase() +
                        ", please check entity_blacklist.yml.");
            }
        }
        ignoreSpawner = Config.getEntityBlacklist().getBoolean("ignore_spawner");
        ignoreSpawnEgg = Config.getEntityBlacklist().getBoolean("ignore_spawn_egg");
    }

    public static boolean isBlacklisted(Entity entity) {
        return blacklisted.contains(entity.getType());
    }

    public static boolean isBlacklisted(EntityType entity) {
        return blacklisted.contains(entity);
    }

    public static void add(EntityType entity) {
        List<String> blacklist = new ArrayList<>(Config.getEntityBlacklist().getStringList("blacklist"));
        blacklist.add(entity.name());
        Config.getEntityBlacklist().set("blacklist", blacklist);
        Config.saveEntityBlacklist();
        blacklisted.add(entity);
    }

    public static void remove(EntityType entity) {
        List<String> blacklist = new ArrayList<>(Config.getEntityBlacklist().getStringList("blacklist"));
        blacklist.remove(entity.name());
        Config.getEntityBlacklist().set("blacklist", blacklist);
        Config.saveEntityBlacklist();
        blacklisted.add(entity);
    }

    public static void toggleIgnoreSpawner() {
        ignoreSpawner = !ignoreSpawner;
        Config.getEntityBlacklist().set("ignore_spawner", ignoreSpawner);
        Config.saveEntityBlacklist();
    }

    public static void toggleIgnoreSpawnEgg() {
        ignoreSpawnEgg = !ignoreSpawnEgg;
        Config.getEntityBlacklist().set("ignore_spawn_egg", ignoreSpawnEgg);
        Config.saveEntityBlacklist();
    }
}
