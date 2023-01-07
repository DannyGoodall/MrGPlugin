package org.bletchcraft.mrg.commands;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bletchcraft.mrg.MrG;
import org.bletchcraft.mrg.helpers.MrGUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("mrg")
public class MrGCommand extends MrGBaseCommand {

    public MrGCommand(MrG thePlugin){
        super(thePlugin);
    }
    @Subcommand("Protect")
    @Description("Protects blocks in the world from being broken, exploded or burned.")
    @CommandCompletion("@worlds")
    public void protect(Player player, World world){
        final String worldName = world.getName();
        final boolean success;

        plugin.bletchLogger.logWarning(String.format("protect got player of: %s and a world of: %s",player.getName(), world.getName()));
        if (mrGUtils.getServerWorldNames().stream().anyMatch(e ->e.equals(worldName))){
            success = plugin.configManager.protectWorld(worldName, player);
            if (success){
                player.sendMessage("The world " + ChatColor.GREEN + worldName + ChatColor.WHITE + " was protected.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "The world '" + worldName + "' was not found on the server.");
            mrGUtils.sayValidWorlds(player);
            plugin.bletchLogger.logWarning(String.format("World name: '%s' was not found on the server.", worldName));
        }
    }

    @Subcommand("ProtectStatus")
    @Description("Shows if a world is protected or not.")
    public void protectStatus(Player player, World world){
        StringBuilder output = new StringBuilder();
        String protectedWorldList;
        String excludedWorldList;

        plugin.reloadConfig();
        protectedWorldList = plugin.configManager.protectedWorldList();
        excludedWorldList = plugin.configManager.excludedWorldList();

        output.append("The following worlds are protected: ");
        output.append(ChatColor.GREEN);
        output.append( protectedWorldList.isEmpty() ? "(No worlds are protected)" : protectedWorldList);
        output.append(ChatColor.WHITE);
        output.append(". The following worlds are excluded: ");
        output.append(ChatColor.GREEN);
        output.append( excludedWorldList.isEmpty() ? "(No worlds are excluded)" : excludedWorldList);
        output.append(ChatColor.WHITE);
        output.append(".");

        player.sendMessage(output.toString());
        mrGUtils.sayValidWorlds(player);
        plugin.bletchLogger.logInfo("ProtectStatusCommand called:");
    }

    @Subcommand("Exclude")
    @CommandCompletion("@worlds")
    @Description("Excluded worlds cannot be protected.")
    public void exclude(Player player, World world){
        String worldName = world.getName();
        boolean success;

        success = plugin.configManager.excludeWorld(worldName, player);
        if (success) {
            player.sendMessage("The world '" + mrGUtils.wrapWorld(worldName) + "' was marked as excluded.");
        }
    }

    @Subcommand("UnProtect")
    @CommandCompletion("@worlds")
    @Description("If a world has been protected, this command will unprotected it.")
    public void unProtect(Player player, World world){
        final String worldName = world.getName();
        final boolean success;

        success = plugin.configManager.unprotectWorld(worldName, player);
        if (success) {
            player.sendMessage("The world " + ChatColor.GREEN + worldName + ChatColor.WHITE + " was unprotected.");
        }
    }

    @Subcommand("UnExclude")
    @CommandCompletion("@worlds")
    @Description("If a world has been excluded, this command will unexcluded it and it can be protected again.")
    public void unExclude(Player player, World world){
        final String worldName = world.getName();
        final boolean success;

        success = plugin.configManager.unexcludeWorld(worldName, player);
        if (success) {
            player.sendMessage("The world " + ChatColor.GREEN + worldName + ChatColor.WHITE + " was unexcluded - removed from the excluded list.");
        }
    }

    @Subcommand("SpawnStatus")
    @Description("Shows a list of mobs that are not allowed to spawn in any world.")
    public void spawnStatus(Player player, World world){
        final String doNotSpawnList;
        StringBuilder output = new StringBuilder();

        plugin.reloadConfig();
        doNotSpawnList = String.join(", ", plugin.getConfig().getStringList("DoNotSpawn"));
        output.append("The following entities are stopped from spawning: ");
        output.append(mrGUtils.wrapEntityType(doNotSpawnList));
        output.append(".");
        player.sendMessage(output.toString());
    }

    @Subcommand("DoSpawn")
    @CommandCompletion("@mobs")
    @Description("Allows this mob type to spawn in any world.")
    public void doSpawn(Player player, World world, EntityType entityType){
        String entityTypeName = entityType.name();

        if (entityTypeName.isEmpty()) {
            player.sendMessage("The entity type is either not recognised or was blank.");
        }
        // If we're here then we have a valid entityType
        if (plugin.configManager.doSpawn(entityTypeName)) {
            player.sendMessage(
                    String.format(
                            "Spawning of entity type: '%s' has now been enabled.",
                            MrGUtils.wrapEntityType(entityTypeName)
                    )
            );
        } else {
            player.sendMessage(
                    ChatColor.RED + String.format(
                            "Entities of type: '%s' could already spawn because they were not stopped from spawning.",
                            entityType
                    )
            );
        }
    }

    @Subcommand("DoNotSpawn")
    @CommandCompletion("@mobs")
    @Description("Stops this mob type from spawning in any world.")
    public void doNotSpawn(Player player, World world, EntityType entityType){
        String entityTypeName = entityType.name();

        if (entityTypeName.isEmpty()) {
            player.sendMessage("The entity type is either not recognised or was blank.");
        }
        // If we're here then we have a valid entityType
        if (plugin.configManager.doNotSpawn(entityTypeName)) {
            player.sendMessage(
                    String.format(
                            "Spawning of entity type: '%s' has been stopped.",
                            mrGUtils.wrapEntityType(entityTypeName)
                    )
            );
        } else {
            player.sendMessage(
                    ChatColor.RED + String.format(
                            "Entities of type: '%s' were already stopped from spawning.",
                            entityType
                    )
            );
        }
    }

    @Subcommand("DelWarp")
    @Description("Deletes and forgets about a previously saved warp.")
    @CommandCompletion("@worldwarps @boolean")
    public void delWarp(CommandSender sender, String warpName){
        if (sender instanceof Player player){
            World world;
            Location warpLocation;
            ArmorStand hologram;

            if (warpName==null || warpName.isEmpty()){
                player.sendMessage(mrGUtils.wrapError("That warp name could not be found."));
                return;
            }
            world = player.getWorld();
            if (!plugin.configManager.warpExists(world, warpName)){
                player.sendMessage(
                        String.format(
                                "The warp: %s does not exist in the world: %s so it can't be deleted.",
                                mrGUtils.wrapWarp(warpName),
                                mrGUtils.wrapWorld(world.getName()
                                )
                        )
                );
                return;
            }
            warpLocation = plugin.configManager.getWarpVector(warpName, world);
            plugin.configManager.removeWarp(warpName, world);

            // Now look for our hologram
            hologram = mrGUtils.getHologram(warpLocation);
            if (hologram == null ){
                plugin.bletchLogger.logWarning(
                        String.format(
                                "When deleting warp %s the hologram could not be found.",
                                warpName
                        )
                );
                return;
            }
            // Let's make sure that this armorstand has the PersistentData
            PersistentDataContainer metaData = hologram.getPersistentDataContainer();
            if (metaData.has(new NamespacedKey(plugin, "warp"), PersistentDataType.STRING)){
                // This is our Armorstand / hologram so we can delete it
                hologram.remove();
            }
            player.sendMessage(
                    String.format(
                            "The warp: %s has been deleted from the world: %s",
                            mrGUtils.wrapWarp(warpName),
                            mrGUtils.wrapWorld(world.getName())
                    )
            );
        }
    }

    @Subcommand("SetWarp")
    @Description("Sets a warp to the current location or the location specified.")
    public void setWarp(CommandSender sender, String warpName, @Default("null") String warpLocationX, @Default("null") String warpLocationY, @Default("null") String warpLocationZ){
        Location location = null;
        String errorMessage = "";
        Boolean result;

        if (sender instanceof Player player) {
            World world = player.getWorld();
            ArmorStand hologram;
            PersistentDataContainer metaData;

            plugin.bletchLogger.logWarning("player "+player.getName());
            plugin.bletchLogger.logWarning("world "+world.getName());
            plugin.bletchLogger.logWarning("warpName "+warpName);
            plugin.bletchLogger.logWarning("warpLocationX "+warpLocationX);
            plugin.bletchLogger.logWarning("warpLocationY "+warpLocationY);
            plugin.bletchLogger.logWarning("warpLocationZ "+warpLocationZ);

            // Validate that if any of the warpLocation parameters are set then

            if (!(warpLocationX+warpLocationY+warpLocationZ).equalsIgnoreCase("nullnullnull")){
                // Some parameters were set so the location we get back cannot be null
                location = mrGUtils.parseXYZOrNull(world, warpLocationX, warpLocationY, warpLocationZ);
                // If it is null then we couldn't decode the location
                if (location == null){
                    player.sendMessage(
                        String.format(
                            "I couldn't understand the location" + mrGUtils.wrapWorld(" (%s, %s, %s)"),
                            warpLocationX,
                            warpLocationY,
                            warpLocationZ
                        )
                    );
                    return;
                }
            }
            // If we got here and the location is null then it's because it wasn't specified. We assume it's the player's
            // current position
            if (location == null){
                location = player.getLocation();
            }

            //Now validate that the warpName doesn't already exist and that it is shorter than 32 characters
            if (warpName.length() > 32){
                errorMessage = "The warp name '" + mrGUtils.wrapWarp(warpName) + "' is too long. It must be 32 characters or fewer.";
            } else if (plugin.configManager.warpExists(world, warpName)){
                errorMessage = "The warp named '" + mrGUtils.wrapWarp(warpName) + "' already exists.";
            }
            if (!errorMessage.isEmpty()){
                player.sendMessage("Couldn't add warp. "+errorMessage);
                return;
            }
            // If we got here then we should have a valid warpName, location and world
            result = plugin.configManager.addWarp(warpName, location);
            if (!result){
                player.sendMessage(mrGUtils.wrapError("Something when wrong trying to add this warp!"));
            } else {
                // Create a hologram at the warp point
                hologram = mrGUtils.createHologram(location, "WARP: "+warpName, NamedTextColor.WHITE);
                metaData = (PersistentDataContainer) hologram.getPersistentDataContainer();
                metaData.set(new NamespacedKey(plugin, "warp"), PersistentDataType.STRING, warpName);

            }
        }
    }

    @Subcommand("warps")
    @Description("Shows a list of all of the saved warps in a world.")
    @CommandCompletion("@worlds")
    public void showWarps(CommandSender sender, @Optional World world){
        if (sender instanceof Player player){
            List<String> warpsInWorld = new ArrayList<String>();
            String message;
            if (world == null){
                world = player.getWorld();
            }
            warpsInWorld = plugin.configManager.warpsInWorld(world);
            if (warpsInWorld.isEmpty()){
                message = String.format(
                    "There are no saved warps in the world: %s.",
                    mrGUtils.wrapWorld(world.getName())
                );
            } else {
                message = String.format(
                    "In world %s the follow warps exist:: %s",
                    mrGUtils.wrapWorld(world.getName()),
                    mrGUtils.wrapWarp(String.join(", ", warpsInWorld))
                );
            }
            player.sendMessage(message);
        }
    }

    @Subcommand("WarpTo")
    @Description("Teleports a player to the location of the saved warp.")
    @CommandCompletion("@worldwarps")
    public void warpTo(CommandSender sender, String warpName){
        if (sender instanceof Player player){
            World world = player.getWorld();
            Location location;
            if (warpName == null || warpName.isEmpty() || !plugin.configManager.warpExists(world, warpName)) {
                player.sendMessage(
                    String.format(
                        "You need to specify a warp that has been saved in this world (%s).",
                        mrGUtils.wrapWorld(world.getName())
                    )
                );
                return;
            }
            // We have a valid warpName
            location = plugin.configManager.getWarpVector(warpName, world);
            if (location == null){
                player.sendMessage(mrGUtils.wrapError("Could not find that saved warp."));
                return;
            }
            player.teleport(location);
            mrGUtils.showTitle(
                player,
                warpName,
                String.format(
                    "@ (%d, %d, %d)",
                    location.getBlockX(),
                        location.getBlockY(),
                        location.getBlockZ()
                )
            );
        }
    }

    @Subcommand("hologram|holo")
    @Description("creates a hologram where the player is standing")
    public void doHologram(CommandSender sender, String[] hologramText){
        if (sender instanceof Player player){
            Location playerLocation;
            StringBuilder sb = new StringBuilder();
            ArmorStand hologram;

            if (hologramText == null || hologramText.length==0){
                player.sendMessage("I didn't understand the hologram command. Make sure you set the hologram text.");
                return;
            }
            // Build the string
            for (String hologramWord: hologramText){
                if (!sb.isEmpty()){
                    sb.append(" ");
                }
                sb.append(hologramWord);
            }
            playerLocation = player.getLocation();
            hologram = mrGUtils.createHologram(playerLocation, sb.toString(), NamedTextColor.WHITE);
        }
    }


    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        plugin.bletchLogger.logWarning("doHelp executed.");
        if (sender instanceof Player){
            Player player = (Player) sender;
            player.sendMessage(ChatColor.GREEN+ "Mr G's Plugin Help");
        }
        help.showHelp();
    }
}
