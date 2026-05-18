package com.idlefishing.strategy;

/**
 * Strategy interface for vessel production calculation.
 * Different vessels use different growth curves.
 */
@FunctionalInterface
public interface ProductionStrategy {

    /**
     * Calculates total production per second.
     *
     * @param baseRate production rate of a single vessel
     * @param count    number of vessels owned
     * @return total production per second
     */
    double calculate(double baseRate, int count);
}
