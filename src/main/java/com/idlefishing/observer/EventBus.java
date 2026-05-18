package com.idlefishing.observer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton publish-subscribe event bus.
 * All game components communicate through this bus to stay decoupled.
 */
public final class EventBus {

    private static EventBus instance;

    private final Map<EventType, List<GameObserver>> listeners =
            new EnumMap<>(EventType.class);

    private EventBus() {
        for (EventType type : EventType.values()) {
            listeners.put(type, new ArrayList<>());
        }
    }

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    /** Registers an observer for the given event type. */
    public void subscribe(EventType type, GameObserver observer) {
        listeners.get(type).add(observer);
    }

    /** Publishes an event carrying optional data to all subscribers. */
    public void publish(EventType type, Object data) {
        GameEvent event = new GameEvent(type, data);
        List<GameObserver> obs = new ArrayList<>(listeners.get(type));
        for (GameObserver o : obs) {
            o.onEvent(event);
        }
    }

    /** Convenience overload for events without payload. */
    public void publish(EventType type) {
        publish(type, null);
    }
}
