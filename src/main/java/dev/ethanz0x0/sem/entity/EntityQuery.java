package dev.ethanz0x0.sem.entity;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityQuery {

    public static int countByType(EntityType type) {
        int amount = 0;
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                amount += chunk.getEntities().length;
            }
        }
        return amount;
    }

    public static int countByWorld(World world) {
        return world.getEntities().size();
    }

    public static int countByChunk(Chunk chunk) {
        return chunk.getEntities().length;
    }

    public static List<Chunk> topChunksWithMostEntities() {
        List<Chunk> allLoadedChunks = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            allLoadedChunks.addAll(Arrays.asList(world.getLoadedChunks()));
        }

        allLoadedChunks.sort((c1, c2) ->
                Integer.compare(c2.getEntities().length, c1.getEntities().length)
        );

        return allLoadedChunks.stream().limit(5).collect(Collectors.toList());
    }

}
