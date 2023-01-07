package org.bletchcraft.mrg.listeners;

import org.bletchcraft.mrg.MrG;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends MrGBaseListener implements Listener {
    public PlayerJoinListener(MrG thePlugin) {
        super(thePlugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // Logic here to get the protect status for this world

        // Let's re-read the configuration
        plugin.reloadConfig();

    }
}
