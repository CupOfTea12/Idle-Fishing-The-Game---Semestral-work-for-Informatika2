package com.idlefishing.strategy;

public class LinearProductionStrategy implements ProductionStrategy {
    @Override
    public double calculate(double baseRate, int count) {
        return baseRate * count;
    }
}
