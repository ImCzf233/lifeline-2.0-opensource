package Lifeline.wtf.events.world;


import Lifeline.wtf.eventapi.events.Event;

public class EventPostStep implements Event {
	private float height;

	public EventPostStep(float height) {
		this.height = height;
	}

	public float getHeight() {
		return this.height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
