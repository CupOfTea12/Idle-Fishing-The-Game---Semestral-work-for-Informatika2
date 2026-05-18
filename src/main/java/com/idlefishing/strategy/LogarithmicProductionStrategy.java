package com.idlefishing.strategy;

/**
 * Logarithmic bonus growth: {@code rate = baseRate * count * log2(count+1)}.
 * Each extra Trawler adds a diminishing bonus on top of the base.
 */
public class LogarithmicProductionStrategy implements ProductionStrategy {

    @Override
    public double calculate(double baseRate, int count) {
        return baseRate * count * (Math.log(count + 1) / Math.log(2));
    }
}
