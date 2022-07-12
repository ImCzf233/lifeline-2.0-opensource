package Lifeline.wtf.events.rendering;

import Lifeline.wtf.eventapi.events.Event;

public class Render3DEvent implements Event {

    private float ticks;
    private float partialTicks;

    public Render3DEvent(float ticks) {
        this.ticks = ticks;
    }

    public float getTicks() {
        return ticks;
    }

    public void setTicks(float ticks) {
        this.ticks = ticks;
    }


    public float getPartialTicks() {
        return partialTicks;
    }

}