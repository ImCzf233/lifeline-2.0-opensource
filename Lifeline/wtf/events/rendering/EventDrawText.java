package Lifeline.wtf.events.rendering;


import Lifeline.wtf.eventapi.events.Event;

public class EventDrawText implements Event {
   public String text;

   public EventDrawText(String text) {
      this.text = text;
   }
}
