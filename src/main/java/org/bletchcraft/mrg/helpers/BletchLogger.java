package org.bletchcraft.mrg.helpers;

import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class BletchLogger {
    Plugin plugin;

    public BletchLogger(Plugin thePlugin) {
        plugin = thePlugin;
    }

    public void logInfo(String message) {
        plugin.getLogger().log(Level.INFO, String.format("%s", message));
    }

    public void logInfo(String message, String context) {
        logInfo(String.format("%s: %s", context, message));
    }

    public void logError(String message) {
        plugin.getLogger().log(Level.SEVERE, String.format("%s", message));
    }

    public void logError(String message, String context) {
        logError(String.format("%s: %s", context, message));
    }

    public void logWarning(String message) {
        plugin.getLogger().log(Level.WARNING, String.format("%s", message));
    }

    public void logWarning(String message, String context) {
        logWarning(String.format("%s: %s", context, message));
    }

}
