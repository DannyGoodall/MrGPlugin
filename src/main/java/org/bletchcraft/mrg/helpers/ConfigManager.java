package org.bletchcraft.mrg.helpers;

import org.bletchcraft.mrg.MrG;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;


public class ConfigManager {
    private final MrG plugin;
    public MrGUtils mrGUtils;
    // private final Map<String, Boolean> worldStatuses = new HashMap<String, Boolean>();

    public ConfigManager(final MrG thePlugin) {
        plugin = thePlugin;
        mrGUtils = new MrGUtils(thePlugin);
        // readConfiguration();
    }

    public void dumpConfiguration(String context) {
        List<String> protectedWorlds = plugin.getConfig().getStringList("ProtectedWorlds");
        List<String> excludedWorlds = plugin.getConfig().getStringList("ExcludedWorlds");
        plugin.bletchLogger.logInfo("Dumping Configuration");
        plugin.bletchLogger.logInfo("Protected Worlds");
        for (String worldName : protectedWorlds) {
            plugin.bletchLogger.logInfo(String.format("- %s", worldName));
        }
        plugin.bletchLogger.logInfo("Excluded Worlds");
        for (String worldName : excludedWorlds) {
            plugin.bletchLogger.logInfo(String.format("- %s", worldName));
        }
    }

    public Boolean worldProtected(String worldName) {
        // final List<String> protectedWorlds = plugin.getConfig().getStringList("ProtectedWorlds");
        return plugin.getConfig().getStringList("ProtectedWorlds").contains(worldName);
    }

    public Boolean worldExcluded(String worldName) {
        return plugin.getConfig().getStringList("ExcludedWorlds").contains(worldName);
    }

    public Boolean protectWorld(String worldName, Player player) {
        List<String> protectedWorlds;
        String output = "";
        ChatColor chatColor;
        boolean success;

        // Ensure we're dealing with the latest config
        plugin.reloadConfig();

        // Process command
        if (worldProtected(worldName)) {
            output = String.format("World '%s' is already protected.", worldName);
            chatColor=ChatColor.RED;
            success = false;
        } else if (worldExcluded(worldName)) {
            output = String.format("World '%s' is set as 'excluded' so it can't be protected.", worldName);
            chatColor=ChatColor.RED;
            success = false;
        } else {
            protectedWorlds = plugin.getConfig().getStringList("ProtectedWorlds");
            protectedWorlds.add(worldName);
            plugin.getConfig().set("ProtectedWorlds", protectedWorlds);
            plugin.saveConfig();
            output = String.format("'%s' %swas added to the list of protected worlds.", worldName, ChatColor.WHITE);
            chatColor=ChatColor.GREEN;
            success = true;
        }
        plugin.bletchLogger.logWarning(output, "protectWorld");
        player.sendMessage(chatColor + output);
        return success;
    }

    public Boolean unprotectWorld(String worldName, Player player) {
        List<String> protectedWorlds;
        String output = "";
        ChatColor chatColor;
        boolean success;

        // readConfiguration();
        if (!worldProtected(worldName)) {
            output = String.format("World '%s' is not currently protected so it can't be unprotected.", worldName);
            chatColor=ChatColor.RED;
            success=false;
        } else {
            protectedWorlds = plugin.getConfig().getStringList("ProtectedWorlds");
            protectedWorlds.remove(worldName);
            plugin.getConfig().set("ProtectedWorlds", protectedWorlds);
            plugin.saveConfig();
            output = String.format("'%s' %swas added to the list of unprotected worlds.", worldName, ChatColor.WHITE);
            chatColor=ChatColor.GREEN;
            success = true;
        }
        plugin.bletchLogger.logWarning(output, "unprotectWorld");
        player.sendMessage(chatColor + output);
        return success;
    }


    public Boolean excludeWorld(String worldName, Player player) {
        List<String> excludedWorlds;
        String output = "";
        ChatColor chatColor;
        boolean success;

        // readConfiguration();
        if (worldExcluded(worldName)) {
            output = String.format("World %s is already excluded.", worldName);
            chatColor=ChatColor.RED;
            success = false;
        } else if (worldProtected(worldName)) {
            output = String.format("World %s is set as 'protected' so it can't be excluded. Unprotect it first.", worldName);
            chatColor=ChatColor.RED;
            success = false;
        } else {
            excludedWorlds = plugin.getConfig().getStringList("ExcludedWorlds");
            excludedWorlds.add(worldName);
            plugin.getConfig().set("ExcludedWorlds", excludedWorlds);
            plugin.saveConfig();
            output = String.format("%s %swas added to the list of excluded worlds.", worldName, ChatColor.WHITE);
            chatColor=ChatColor.GREEN;
            success = true;
        }
        plugin.bletchLogger.logWarning(output, "excludeWorld");
        player.sendMessage(chatColor + output);
        return success;
    }

    public Boolean unexcludeWorld(String worldName, Player player) {
        List<String> excludedWorlds;
        String output = "";
        ChatColor chatColor;
        boolean success;

        if (!worldExcluded(worldName)) {
            output = String.format("World %s is not currently excluded so it can't be unexcluded.", worldName);
            chatColor=ChatColor.RED;
            success = false;
        } else {
            excludedWorlds = plugin.getConfig().getStringList("ExcludedWorlds");
            excludedWorlds.remove(worldName);
            plugin.getConfig().set("ExcludedWorlds", excludedWorlds);
            plugin.saveConfig();
            output = String.format("%s %swas removed from the list of excluded worlds.", worldName, ChatColor.WHITE);
            chatColor=ChatColor.GREEN;
            success = true;
        }
        plugin.bletchLogger.logWarning(output, "unexcludeWorld");
        player.sendMessage(chatColor + output);
        return success;
    }

    public String protectedWorldList() {
        List<String> protectedWorlds = plugin.getConfig().getStringList("ProtectedWorlds");
        return String.join(", ", protectedWorlds);
    }

    public String excludedWorldList() {
        List<String> excludedWorlds = plugin.getConfig().getStringList("ExcludedWorlds");
        return String.join(", ", excludedWorlds);
    }

    public String doNotSpawnList(){
        List<String> doNotSpawnList = plugin.getConfig().getStringList("DoNotSpawn");
        return String.join(", ", doNotSpawnList);
    }

    public boolean canSpawn(String entityType){
        List<String> doNotSpawnList = plugin.getConfig().getStringList("DoNotSpawn");
        return !doNotSpawnList.contains(entityType.toUpperCase());
    }

    public boolean cannotSpawn(String entityType){
        return !canSpawn(entityType);
    }

    public boolean doNotSpawn(String entityType){
        List<String> doNotSpawnList;

        plugin.reloadConfig();
        doNotSpawnList = plugin.getConfig().getStringList("DoNotSpawn");
        plugin.bletchLogger.logInfo(String.format("doNotSpawnList=%s",doNotSpawnList.toString()),"doNotSpawn");
        if (canSpawn(entityType)){
            plugin.bletchLogger.logInfo("Inside canSpawn IF BLOCK", "doNotSpawn");
            // We haven't already restricted this so we can do so now...
            doNotSpawnList.add(entityType.toUpperCase());
            plugin.getConfig().set("DoNotSpawn", doNotSpawnList);
            plugin.saveConfig();
            return true;
        }
        plugin.bletchLogger.logInfo("Couldn't change config. Returning false.", "doNotSpawn");
        return false;
    }

    public boolean doSpawn(String entityType){
        List<String> doNotSpawnList;

        plugin.reloadConfig();
        doNotSpawnList = plugin.getConfig().getStringList("DoNotSpawn");
        if (cannotSpawn(entityType)){
            // We have already restricted this so we can remove it
            doNotSpawnList.remove(entityType.toUpperCase());
            plugin.getConfig().set("DoNotSpawn", doNotSpawnList);
            plugin.saveConfig();
            return true;
        }
        return false;
    }

    public void setBlockBreakWarningEvery(int intValue){
        plugin.reloadConfig();
        plugin.getConfig().set("BlockBreakWarningEvery", intValue);
    }

    public int getBlockBreakWarningEvery(){
        plugin.reloadConfig();
        return plugin.getConfig().getInt("BlockBreakWarningEvery");
    }

    public String warpVectorsConfigPath(World world, String warpName){
        return String.format("Warps.%s.%s", world.getName(), warpName.toLowerCase());
    }

    public String warpWorldsConfigPath(World world){
        return String.format("Warps.%s", world.getName());
    }

    public List<String> warpsInWorld(World world){
        List<String> warpsFound = new ArrayList<String>();
        ConfigurationSection worldsConfig;
        plugin.reloadConfig();
        worldsConfig = plugin.getConfig().getConfigurationSection(warpWorldsConfigPath(world));
        if (!(worldsConfig==null)){
            warpsFound.addAll(worldsConfig.getKeys(false));
        };
        return warpsFound;
    }

    public boolean warpExists(World world, String warpName){
        plugin.reloadConfig();
        return warpsInWorld(world).contains(warpName.toLowerCase());
    }

    public boolean addWarp(String warpName, Location location){
        World world = location.getWorld();
        List<String> spawnList = warpsInWorld(world);
        String configPath;
        if (spawnList.contains(warpName.toLowerCase())){
            // Might need a message here
            return false;
        } else {
            configPath = warpVectorsConfigPath(world, warpName);
            plugin.getConfig().set(configPath, location.toVector());
            plugin.saveConfig();
            return true;
        }
    }

    public boolean removeWarp(String warpName, World world){
        List<String> spawnList = warpsInWorld(world);
        String configPath;
        if (!spawnList.contains(warpName.toLowerCase())){
            return false;
        } else {
            configPath = warpVectorsConfigPath(world, warpName);
            plugin.getConfig().set(configPath, null);
            plugin.saveConfig();
            return true;
        }
    }

    public Location getWarpVector(String warpname, World world){
        Vector v;
        v = plugin.getConfig().getVector(warpVectorsConfigPath(world, warpname));
        if (v == null){
            return null;
        } else {
            return v.toLocation(world);
        }
    }
}
