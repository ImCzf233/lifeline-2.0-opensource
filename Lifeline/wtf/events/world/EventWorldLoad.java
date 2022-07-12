package Lifeline.wtf.events.world;

import Lifeline.wtf.eventapi.events.Event;
import net.minecraft.client.multiplayer.WorldClient;

public class EventWorldLoad implements Event {

    private final WorldClient world;

    public EventWorldLoad(WorldClient world) {
        this.world = world;
    }

    public WorldClient getWorld() {
        return world;
    }
}
