package org.bletchcraft.mrg.listeners;

import org.bletchcraft.mrg.MrG;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class ChangedWorldListener  extends MrGBaseListener implements Listener {
    public ChangedWorldListener(MrG thePlugin){
        super(thePlugin);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e){
        // Logic here to get the protect status for this world

        // Let's re-read the configuration
        plugin.reloadConfig();

    }
}
