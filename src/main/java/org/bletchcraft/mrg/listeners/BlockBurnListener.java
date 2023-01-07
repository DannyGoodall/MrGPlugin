package org.bletchcraft.mrg.listeners;

import org.bletchcraft.mrg.MrG;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

public class BlockBurnListener extends MrGBaseListener implements Listener {
    public BlockBurnListener(MrG plugin){
        super(plugin);
    }
    @EventHandler
    public void onBurnBlock(BlockBurnEvent e){
        Block block = e.getBlock();
        World world = block.getWorld();

        if (plugin.configManager.worldProtected(world.getName())) {
            plugin.bletchLogger.logWarning("Cancelling a BlockBurnEvent","BlockBurnListener");

            e.setCancelled(true);
        }
    }
}
