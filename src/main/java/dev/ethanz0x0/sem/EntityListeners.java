package dev.ethanz0x0.sem;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntityListeners implements Listener {

    private static final SuperEntityManager plugin = SuperEntityManager.getInstance();

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {

    }
}
