package com.idlefishing.strategy;

/** Linear growth: {@code rate = baseRate * count}. Used by Rowboat and FishingBoat. */
public class LinearProductionStrategy implements ProductionStrategy {

    @Override
    public double calculate(double baseRate, int count) {
        return baseRate * count;
    }
}
