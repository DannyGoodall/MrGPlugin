package org.bletchcraft.mrg;

import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.PaperCommandManager;
import org.bletchcraft.mrg.commands.MrGCommand;
import org.bletchcraft.mrg.helpers.BletchLogger;
import org.bletchcraft.mrg.helpers.ConfigManager;
import org.bletchcraft.mrg.listeners.*;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MrG extends JavaPlugin {
    private static MrG plugin;
    private static PaperCommandManager commandManager;
    public final ConfigManager configManager = new ConfigManager(this);
    public BletchLogger bletchLogger;

    @Override
    public void onEnable() {
        plugin = this;
        bletchLogger = new BletchLogger(this);
        // Plugin startup logic
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        registerCommands();

        registerListeners();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.registerCommand(new MrGCommand(this));

        commandManager.getCommandCompletions().registerAsyncCompletion("worldwarps", (c) -> {
            BukkitCommandIssuer issuer = (BukkitCommandIssuer) c.getIssuer();
            CommandSender sender = c.getSender();
            if (sender instanceof Player player) {
                World world = player.getWorld();
                return configManager.warpsInWorld(world);
            } else {
                return List.of();
            }
        });
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new BlockBurnListener(this), this);
        getServer().getPluginManager().registerEvents(new EntitySpawnListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockExplodeListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityExplodeListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new ChangedWorldListener(this), this);
    }

    // Typical Bukkit Plugin Scaffolding
    public static MrG getPlugin() {
        return plugin;
    }

    // A way to access your command manager from other files if you do not use a Dependency Injection approach
    public static PaperCommandManager getCommandManager() {
        return commandManager;
    }
}
