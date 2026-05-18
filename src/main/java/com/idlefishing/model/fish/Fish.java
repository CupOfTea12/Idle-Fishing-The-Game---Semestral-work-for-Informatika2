package com.idlefishing.model.fish;

import java.awt.Color;

/**
 * Abstract base for all fish species.
 * Polymorphism is exploited through {@link #getRarity()}, {@link #getDisplayColor()},
 * and {@link #getCatchMessage()} – each subclass expresses its own personality.
 */
public abstract class Fish {

    private final String name;
    private final double baseValue;
    private final double weight; // kg

    protected Fish(String name, double baseValue, double weight) {
        this.name = name;
        this.baseValue = baseValue;
        this.weight = weight;
    }

    // ── Abstract interface (polymorphic) ─────────────────────────────────────

    /** Short rarity label shown in the UI. */
    public abstract String getRarity();

    /** Colour used to tint this fish's name and card in the UI. */
    public abstract Color getDisplayColor();

    /** Exclamatory message shown when the player catches this fish. */
    public abstract String getCatchMessage();

    // ── Template method ──────────────────────────────────────────────────────

    /**
     * Calculates the sell value, applying a weight bonus
     * (10 % extra per kg above 1 kg) and the supplied market multiplier.
     */
    public double calculateValue(double marketMultiplier, double varianceFactor) {
        double weightBonus = Math.max(0.0, (weight - 1.0) * 0.10);
        return baseValue * (1.0 + weightBonus) * marketMultiplier * varianceFactor;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getName() { return name; }
    public double getBaseValue() { return baseValue; }
    public double getWeight() { return weight; }

    @Override
    public String toString() {
        return String.format("%s [%s] %.1f kg  €%.2f", name, getRarity(), weight, baseValue);
    }
}
