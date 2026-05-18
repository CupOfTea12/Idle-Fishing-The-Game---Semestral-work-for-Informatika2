package com.idlefishing.strategy;

/**
 * Exponential bonus growth: {@code rate = baseRate * count * 1.05^(count-1)}.
 * Factory Ships scale aggressively the more you own.
 */
public class ExponentialProductionStrategy implements ProductionStrategy {

    private static final double SCALE = 1.05;

    @Override
    public double calculate(double baseRate, int count) {
        return baseRate * count * Math.pow(SCALE, count - 1);
    }
}
