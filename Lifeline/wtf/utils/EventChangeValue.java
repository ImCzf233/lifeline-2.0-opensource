package Lifeline.wtf.utils;

import Lifeline.wtf.eventapi.events.Event;

public class EventChangeValue implements Event {
    public String valKey;
    public String valName;
    public Object oldVal;
    public Object val;

    public EventChangeValue(String valKey, String valName, Object oldVal, Object val) {
        this.valKey = valKey;
        this.valName = valName;
        this.oldVal = oldVal;
        this.val = val;
    }

}
