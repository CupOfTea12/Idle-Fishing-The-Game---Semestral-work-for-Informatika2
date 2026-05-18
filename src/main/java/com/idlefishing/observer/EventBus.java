package com.idlefishing.observer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** App-wide event bus. Subscribe once, get notified on every matching publish. */
public final class EventBus {

    private static EventBus instance;

    private final Map<EventType, List<GameObserver>> listeners = new EnumMap<>(EventType.class);

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

    public void subscribe(EventType type, GameObserver observer) {
        listeners.get(type).add(observer);
    }

    public void publish(EventType type, Object data) {
        List<GameObserver> obs = new ArrayList<>(listeners.get(type));
        GameEvent event = new GameEvent(type, data);
        for (GameObserver o : obs) {
            o.onEvent(event);
        }
    }

    public void publish(EventType type) {
        publish(type, null);
    }
}
