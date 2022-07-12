/*
 * Decompiled with CFR 0_132.
 */
package Lifeline.wtf.events.misc;

import Lifeline.wtf.eventapi.events.Event;

public class EventKey
        implements Event {
    private int key;

    public EventKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}

