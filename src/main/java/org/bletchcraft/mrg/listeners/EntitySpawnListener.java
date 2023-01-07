package org.bletchcraft.mrg.listeners;

import org.bletchcraft.mrg.MrG;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawnListener extends MrGBaseListener implements Listener {
    public EntitySpawnListener(MrG thePlugin){
        super(thePlugin);
    }
    @EventHandler
    private void onEntitySpawn(EntitySpawnEvent e){
        Entity entity = e.getEntity();
        String entityType = entity.getType().toString();
        if (plugin.configManager.cannotSpawn(entityType)){
            e.setCancelled(true);
        }
    }
}
