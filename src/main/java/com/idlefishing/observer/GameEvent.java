package com.idlefishing.observer;

public final class GameEvent {

    private final EventType type;
    private final Object data;
    private final long timestamp;

    public GameEvent(EventType type, Object data) {
        this.type      = type;
        this.data      = data;
        this.timestamp = System.currentTimeMillis();
    }

    public EventType getType()      { return type; }
    public Object    getData()      { return data; }
    public long      getTimestamp() { return timestamp; }
}
