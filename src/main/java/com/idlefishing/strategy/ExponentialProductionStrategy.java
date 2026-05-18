package com.idlefishing.strategy;

/** Factory ships snowball hard — the more you own, the faster each one produces. */
public class ExponentialProductionStrategy implements ProductionStrategy {
    @Override
    public double calculate(double baseRate, int count) {
        return baseRate * count * Math.pow(1.05, count - 1);
    }
}
