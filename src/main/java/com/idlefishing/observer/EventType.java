package com.idlefishing.observer;

/** All event types that can be published on the {@link EventBus}. */
public enum EventType {
    FISH_CAUGHT,
    MONEY_CHANGED,
    VESSEL_PURCHASED,
    UPGRADE_PURCHASED,
    LOCATION_CHANGED,
    LOCATION_UNLOCKED,
    PRESTIGE_TRIGGERED,
    GAME_TICK,
    GAME_SAVED,
    GAME_LOADED,
    OFFLINE_EARNINGS
}
