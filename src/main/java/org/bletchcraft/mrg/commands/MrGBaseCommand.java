package org.bletchcraft.mrg.commands;

import co.aikar.commands.BaseCommand;
import org.bletchcraft.mrg.MrG;
import org.bletchcraft.mrg.helpers.MrGUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MrGBaseCommand extends BaseCommand {
    final public MrG plugin;
    public MrGUtils mrGUtils;

    public MrGBaseCommand(final MrG thePlugin) {
        plugin = thePlugin;
        mrGUtils = new MrGUtils(thePlugin);
    }

}
