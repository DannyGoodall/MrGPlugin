package org.bletchcraft.mrg.listeners;

import org.bletchcraft.mrg.MrG;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplodeListener extends MrGBaseListener implements Listener {
    public EntityExplodeListener(MrG plugin){
        super(plugin);
    }
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e){
        // Player player = e.getPlayer();
        Entity entity = e.getEntity();
        World world = entity.getWorld();
        Location explosionLocation = e.getLocation();
        // plugin.bletchLogger.logWarning("Got EntityExplodeEvent");
        world.createExplosion(explosionLocation, 4F, false, false);
        if (plugin.configManager.worldProtected(world.getName())) {
            e.setCancelled(true);
        }
    }

}
