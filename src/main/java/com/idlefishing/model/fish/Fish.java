package com.idlefishing.model.fish;

import java.awt.Color;

/** Base class for all fish. Subclasses define rarity, colour, and catch message. */
public abstract class Fish {

    private final String name;
    private final double baseValue;
    private final double weight;

    protected Fish(String name, double baseValue, double weight) {
        this.name      = name;
        this.baseValue = baseValue;
        this.weight    = weight;
    }

    public abstract String getRarity();
    public abstract Color  getDisplayColor();
    public abstract String getCatchMessage();

    /** Weight above 1 kg gives a small value bonus (+10% per kg). */
    public double calculateValue(double marketMultiplier, double varianceFactor) {
        double weightBonus = Math.max(0.0, (weight - 1.0) * 0.10);
        return baseValue * (1.0 + weightBonus) * marketMultiplier * varianceFactor;
    }

    public String getName()      { return name; }
    public double getBaseValue() { return baseValue; }
    public double getWeight()    { return weight; }

    @Override
    public String toString() {
        return String.format("%s [%s] %.1f kg  €%.2f", name, getRarity(), weight, baseValue);
    }
}
