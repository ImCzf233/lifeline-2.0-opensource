package Lifeline.wtf.module.value;


import java.awt.*;

public class ColorSetting extends Value {
    float hue = 0;
    float saturation = 1;
    float brightness = 1;

    public ColorSetting(String name, Color defaultColor) {
        this.name = name;
        this.setColor(defaultColor);
    }

    public Color getColor() {
        return Color.getHSBColor(hue, saturation, brightness);
    }

    public void setColor(Color color) {
        float[] hsb = new float[3];
        hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        hue = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
    }

    public void setColor(float hue, float saturation, float brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public double getHue() {
        return hue;
    }

    public void setHue(float hue) {
        this.hue = hue;
    }

    public double getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public Integer getConfigValue() {
        return getColor().getRGB();
    }

}
