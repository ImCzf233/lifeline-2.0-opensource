package Lifeline.wtf.events.rendering;

import Lifeline.wtf.eventapi.events.Event;
import net.minecraft.client.gui.ScaledResolution;

public class Render2DEvent implements Event {

    public ScaledResolution sr;
    private float width, height;

    public Render2DEvent(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public Render2DEvent(ScaledResolution sr) {
        this.sr = sr;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

}