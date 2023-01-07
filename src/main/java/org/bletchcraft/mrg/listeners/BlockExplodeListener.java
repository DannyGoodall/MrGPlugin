package org.bletchcraft.mrg.listeners;

import org.bletchcraft.mrg.MrG;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class BlockExplodeListener  extends MrGBaseListener  implements Listener {
    public BlockExplodeListener(MrG plugin){
        super(plugin);
    }
    @EventHandler
    public void onExplodeBlock(BlockExplodeEvent e){
        // Player player = e.getPlayer();
        Block block = e.getBlock();
        World world = block.getWorld();
        String reason = "";

        // plugin.bletchLogger.logWarning("Got BlockExplodeEvent");

//        if (plugin.worldProtected(world)) {
//            e.setCancelled(true);
//        }
    }

}
