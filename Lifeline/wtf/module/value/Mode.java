/*
 * Decompiled with CFR 0_132.
 */
package Lifeline.wtf.module.value;

public class Mode<V extends Enum>
extends Value<V> {
    private V[] modes;

    private String currentMode;

    public Mode(String displayName, String name, V[] modes, V value) {
        super(displayName, name);
        this.modes = modes;
        this.setValue(value);
    }

    public V[] getModes() {
        return this.modes;
    }

    public String getModeAsString() {
        return ((Enum)this.getValue()).name();
    }

    public void setMode(String mode) {
        V[] arrV = this.modes;
        int n = arrV.length;
        int n2 = 0;
        while (n2 < n) {
            V e = arrV[n2];
            if (e.name().equalsIgnoreCase(mode)) {
                this.setValue(e);
            }
            ++n2;
        }
    }
    public boolean is(String mode) {
        return currentMode.equalsIgnoreCase(mode);
    }


    public boolean isValid(String name) {
        V[] arrV = this.modes;
        int n = arrV.length;
        int n2 = 0;
        while (n2 < n) {
            V e = arrV[n2];
            if (e.name().equalsIgnoreCase(name)) {
                return true;
            }
            ++n2;
        }
        return false;
    }
    public boolean isCurrentMode(String string) {
        return this.getValue().equals(string);
    }
}

