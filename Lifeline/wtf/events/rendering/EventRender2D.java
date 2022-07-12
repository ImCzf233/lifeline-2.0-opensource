/*
 * Decompiled with CFR 0_132.
 */
package Lifeline.wtf.events.rendering;

import Lifeline.wtf.eventapi.events.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRender2D
        implements Event {
    private float partialTicks;
    private ScaledResolution scaledResolution;

    public EventRender2D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }


    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}

