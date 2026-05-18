package com.idlefishing.observer;

/** Observer interface for the game event system. */
@FunctionalInterface
public interface GameObserver {

    /**
     * Called when a subscribed event is published.
     *
     * @param event the published event
     */
    void onEvent(GameEvent event);
}
