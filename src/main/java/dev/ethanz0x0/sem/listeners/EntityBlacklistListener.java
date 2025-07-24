package dev.ethanz0x0.sem.listeners;

import dev.ethanz0x0.sem.entity.EntityBlacklist;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntityBlacklistListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        EntityType type = event.getEntityType();

        if (!EntityBlacklist.getBlacklisted().contains(type)) return;

        switch (event.getSpawnReason()) {
            case SPAWNER_EGG:
                if (EntityBlacklist.isIgnoreSpawnEgg()) return;
                break;
            case SPAWNER:
                if (EntityBlacklist.isIgnoreSpawner()) return;
                break;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof LivingEntity) return;
        EntityType type = entity.getType();
        if (!EntityBlacklist.getBlacklisted().contains(type)) return;
        event.setCancelled(true);
    }
}
