package Lifeline.wtf.events;

public interface EventListener<T> {
    void call(T event);
}
