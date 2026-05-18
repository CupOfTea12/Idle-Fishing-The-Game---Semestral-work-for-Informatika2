package com.idlefishing.strategy;

/** Each extra trawler gives a smaller boost than the last — diminishing returns. */
public class LogarithmicProductionStrategy implements ProductionStrategy {
    @Override
    public double calculate(double baseRate, int count) {
        return baseRate * count * (Math.log(count + 1) / Math.log(2));
    }
}
