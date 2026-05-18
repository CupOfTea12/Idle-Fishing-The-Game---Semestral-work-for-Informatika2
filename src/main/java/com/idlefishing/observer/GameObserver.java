package com.idlefishing.observer;

@FunctionalInterface
public interface GameObserver {
    void onEvent(GameEvent event);
}
