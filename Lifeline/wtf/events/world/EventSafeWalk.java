package Lifeline.wtf.events.world;

import Lifeline.wtf.eventapi.events.callables.EventCancellable;

public class EventSafeWalk extends EventCancellable {

    public EventSafeWalk(boolean safeWalking) {
        setCancelled(safeWalking);
    }
}
