package org.bletchcraft.mrg.helpers;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bletchcraft.mrg.MrG;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MrGUtils {
    final public MrG plugin;

    public MrGUtils(final MrG thePlugin) {
        plugin = thePlugin;
    }

    public List<String> getServerWorldNames() {
        List<String> worldNames = new ArrayList<String>();
        for (World world : plugin.getServer().getWorlds()) {
            worldNames.add(world.getName());
        }
        return worldNames;
    }

    public List<String> getEntityTypes() {
        List<String> entityTypes = new ArrayList<>();
        for (EntityType entityType : EntityType.values()) {
            entityTypes.add(entityType.toString());
        }
        return entityTypes;
    }

    public String wrapWorld(String worldString) {
        return ChatColor.GREEN + worldString + ChatColor.WHITE;
    }

    public String wrapWarp(String warpName) {
        return ChatColor.GREEN + warpName + ChatColor.WHITE;
    }

    public static String wrapEntityType(String entityType) {
        return ChatColor.GREEN + entityType + ChatColor.WHITE;
    }

    public String wrapBlock(String blockName) {
        return ChatColor.GREEN + blockName + ChatColor.WHITE;
    }

    public String wrapError(String errorText) {
        return ChatColor.RED + errorText + ChatColor.WHITE;
    }

    public void sayValidWorlds(Player player) {
        String validWorlds = String.join(", ", getServerWorldNames());
        player.sendMessage("Valid worlds for this server are: " + wrapWorld(validWorlds));
    }

    public String getWorldNameFromArgs(String[] args, Player player) {
        // Return either the world name or an empty string to signify an incorrect number of params
        String worldName;
        if (args.length == 0) {
            // No args so get the players current world
            worldName = player.getWorld().getName();
        } else if (args.length == 1) {
            worldName = args[0];
        } else {
            // Will this re-display the usage?
            worldName = "";
        }
        return worldName;
    }

    public String getEntityTypeFromArgs(String[] args, Player player) {
        // Return either the world name or an empty string to signify an incorrect number of params
        String entityType = "";
        if (args.length == 1) {
            // Must be a way to see if the type exists in the Enum!?!
            plugin.bletchLogger.logWarning("Attempting to find EntityType: " + args[0]);
            if (Arrays.stream(EntityType.values()).anyMatch(e -> e.toString().equalsIgnoreCase(args[0]))) {
                entityType = args[0];
            } else {
                entityType = "";
            }
        }
        return entityType;
    }

    public int tryParseIntOrDefault(String value, int defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public Integer tryParseIntOrNull(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Location parseXYZOrNull(World world, String x, String y, String z) {
        final Location location;
        final Integer xInt, yInt, zInt;
        xInt = tryParseIntOrNull(x);
        yInt = tryParseIntOrNull(y);
        zInt = tryParseIntOrNull(z);

        if (xInt == null || yInt == null || zInt == null) {
            return null;
        }
        location = new Location(world, xInt.doubleValue(), yInt.doubleValue(), zInt.doubleValue());
        return location;
    }

    public void showTitle(Player player, String title, String subTitle){
        if (title == null){
            title = "";
        }
        if (subTitle == null){
            subTitle = "";
        }
        if (title.isEmpty() && subTitle.isEmpty()){
            return;
        }
        final Component componentTitle = Component.text(title, NamedTextColor.WHITE);
        final Component componentSubtitle = Component.text(subTitle, NamedTextColor.GRAY);

        // Creates a simple title with the default values for fade-in, stay on screen and fade-out durations
        final Title theTitle = Title.title(componentTitle, componentSubtitle);

        // Send the title to your audience
        ((Audience) player).showTitle(theTitle);
    }

    public ArmorStand createHologram(Location location, String text, NamedTextColor namedColor){
        ArmorStand hologram;
        final Component componentText;
        PersistentDataContainer metaData;

        hologram = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        hologram.setGravity(false);
        hologram.setVisible(false);
        hologram.setAI(false);
        hologram.setInvulnerable(true);
        hologram.setCustomNameVisible(true);
        componentText = Component.text(text, NamedTextColor.WHITE);
        hologram.customName(componentText);

        return hologram;
    }


    public ArmorStand getHologram(Location location){
        // Let's look for an Armorstand near this location
        World world = location.getWorld();
        ArrayList<Entity> entities = (ArrayList<Entity>) world.getNearbyEntities(location, 1, 1,1);
        if (entities.isEmpty() || entities.get(0).getType() != EntityType.ARMOR_STAND){
            return null;
        } else {
            return (ArmorStand) entities.get(0);
        }
    }
}