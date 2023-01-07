package org.bletchcraft.mrg.listeners;


import org.bletchcraft.mrg.MrG;
import org.bletchcraft.mrg.helpers.MrGUtils;

public class MrGBaseListener extends MrGUtils {
    public MrGUtils mrGUtils;
    public MrGBaseListener(final MrG thePlugin) {
        super(thePlugin);
        mrGUtils = new MrGUtils(thePlugin);
    }
}
