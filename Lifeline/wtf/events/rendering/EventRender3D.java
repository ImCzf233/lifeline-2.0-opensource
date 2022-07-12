/*
 * Decompiled with CFR 0_132.
 */
package Lifeline.wtf.events.rendering;

import Lifeline.wtf.eventapi.events.Event;
import shadersmod.client.Shaders;

public class EventRender3D
        implements Event {
    public float ticks;
    public boolean isUsingShaders;

    public EventRender3D() {
        this.isUsingShaders = Shaders.getShaderPackName() != null;
    }

    public EventRender3D(float ticks) {
        this.ticks = ticks;
        this.isUsingShaders = Shaders.getShaderPackName() != null;
    }

    public float getPartialTicks() {
        return this.ticks;
    }

    public boolean isUsingShaders() {
        return this.isUsingShaders;
    }
}

