/*
 * Decompiled with CFR 0_132.
 */
package Lifeline.wtf.module.modules.visual;

import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.world.EventTick;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;

import java.awt.*;

public class FullBright
extends Module {
    private float old;

    public FullBright() {
        super("FullBright", new String[]{"fbright", "brightness", "bright"}, Category.Visual);
        this.setColor(new Color(244, 255, 149).getRGB());
    }

    @Override
    public void onEnable() {
        this.old = this.mc.gameSettings.gammaSetting;
    }

    @EventTarget
    private void onTick(EventTick e) {
        this.mc.gameSettings.gammaSetting = 1.5999999E7f;
    }

    @Override
    public void onDisable() {
        this.mc.gameSettings.gammaSetting = this.old;
    }
}

