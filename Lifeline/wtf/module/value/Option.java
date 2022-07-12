/*
 * Decompiled with CFR 0_132.
 */
package Lifeline.wtf.module.value;

public class Option<V>
extends Value<V> {

    private boolean state;

    public Option(String displayName, String name, V enabled) {
        super(displayName, name);
        this.setValue(enabled);
    }
    public boolean isEnabled() {
        return state;
    }
}

