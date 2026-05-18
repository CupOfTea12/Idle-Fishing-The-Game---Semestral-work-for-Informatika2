package com.idlefishing.strategy;

@FunctionalInterface
public interface ProductionStrategy {
    double calculate(double baseRate, int count);
}
