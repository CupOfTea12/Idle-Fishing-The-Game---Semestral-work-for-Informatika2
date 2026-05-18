package com.idlefishing.util;

/** Central constants for game balance and configuration. */
public final class GameConstants {

    private GameConstants() { }

    public static final double PRICE_MULTIPLIER = 1.15;
    public static final int MAX_OFFLINE_HOURS = 8;
    public static final int GAME_TICK_MS = 1000;
    public static final int AUTO_SAVE_MS = 60_000;
    public static final double PRESTIGE_THRESHOLD = 1_000_000.0;
    public static final double PRESTIGE_BONUS_PER_LEVEL = 0.10;
    public static final String SAVE_FILE = "idlefishing.sav";
    public static final int RECENT_CATCHES_MAX = 6;

    // Location unlock costs
    public static final double LAKE_UNLOCK_COST = 500.0;
    public static final double SEA_UNLOCK_COST = 10_000.0;
    public static final double OCEAN_UNLOCK_COST = 100_000.0;

    // Vessel base production per second
    public static final double ROWBOAT_PROD = 1.0;
    public static final double FISHING_BOAT_PROD = 12.0;
    public static final double TRAWLER_PROD = 130.0;
    public static final double FACTORY_SHIP_PROD = 1_500.0;

    // Vessel base costs
    public static final double ROWBOAT_COST = 50.0;
    public static final double FISHING_BOAT_COST = 500.0;
    public static final double TRAWLER_COST = 5_000.0;
    public static final double FACTORY_SHIP_COST = 50_000.0;
}
