/*
 * Decompiled with CFR 0_132.
 */
package Lifeline.wtf.module.value;

public class Value<V> {
    private String displayName;
    public String name;
    private V value;
    public float optionAnim = 0;
    public float optionAnimNow = 0;

    public Value(String displayName, String name) {
        this.displayName = displayName;
        this.name = name;
    }

    public Value() {
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getName() {
        return this.name;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}

