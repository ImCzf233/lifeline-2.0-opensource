/*
 * Decompiled with CFR 0_132.
 */
package Lifeline.wtf.module.modules.world;

import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.world.EventTick;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;

import java.awt.*;

public class FastPlace
extends Module {
    public FastPlace() {
        super("FastPlace", new String[]{"fplace", "fc"}, Category.World);
        this.setColor(new Color(226, 197, 78).getRGB());
    }

    @EventTarget
    private void onTick(EventTick e) {
        this.mc.rightClickDelayTimer = 0;
    }
}

