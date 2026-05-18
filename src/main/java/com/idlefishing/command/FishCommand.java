package com.idlefishing.command;

import com.idlefishing.model.GameState;
import com.idlefishing.model.fish.Fish;
import com.idlefishing.model.spot.FishingSpot;
import com.idlefishing.observer.EventBus;
import com.idlefishing.observer.EventType;
import java.util.Random;

/**
 * Command: player manually clicks to catch a fish at the current spot.
 * Value has ±25 % variance to keep clicking interesting.
 */
public class FishCommand implements Command {

    private static final Random RANDOM = new Random();

    private final GameState state;
    private Fish lastCatch;
    private double lastValue;

    public FishCommand(GameState state) {
        this.state = state;
    }

    @Override
    public boolean execute() {
        FishingSpot spot = state.getCurrentSpot();
        if (spot == null || !spot.isUnlocked()) return false;

        Fish fish = spot.catchFish();
        if (fish == null) return false;

        double variance = 0.75 + RANDOM.nextDouble() * 0.50; // 0.75 – 1.25
        double value    = fish.calculateValue(state.getMarketMultiplier(), variance)
                          * state.getPrestigeBonus();

        state.addMoney(value);
        state.incrementFishCaught();

        lastCatch = fish;
        lastValue = value;

        EventBus.getInstance().publish(EventType.FISH_CAUGHT, this);
        EventBus.getInstance().publish(EventType.MONEY_CHANGED, state.getMoney());
        return true;
    }

    @Override
    public String getDescription() {
        return "Fish at " + state.getCurrentSpot().getName();
    }

    public Fish getLastCatch() { return lastCatch; }
    public double getLastValue() { return lastValue; }
}
