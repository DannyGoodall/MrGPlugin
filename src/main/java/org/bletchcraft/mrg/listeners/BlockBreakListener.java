package org.bletchcraft.mrg.listeners;

import org.bletchcraft.mrg.MrG;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener extends MrGBaseListener implements Listener {
    final int blockBreakWarningEvery;
    long brokenBlockCounter;

    public BlockBreakListener(MrG plugin) {
        super(plugin);
        brokenBlockCounter = 0;
        blockBreakWarningEvery = plugin.getConfig().getInt("BlockBreakWarningEvery");
        plugin.bletchLogger.logWarning("blockBreakWarningEvery: " + Integer.toString(blockBreakWarningEvery), "BlockBreakListener");
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        brokenBlockCounter++;

        if (plugin.configManager.worldProtected(world.getName())) {
            if (this.brokenBlockCounter % this.blockBreakWarningEvery == 0) {
                Block block = e.getBlock();
                String blockName = block.getType().toString();
                player.sendMessage(
                        String.format(
                                "You are not allowed to break blocks of type %s in the world '%s'.",
                                mrGUtils.wrapBlock(blockName),
                                mrGUtils.wrapWorld(world.getName())
                        )
                );
            }
            e.setCancelled(true);
        }
    }
}
