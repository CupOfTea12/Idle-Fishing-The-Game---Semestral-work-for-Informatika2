package com.idlefishing.model.vessel;

import com.idlefishing.strategy.ProductionStrategy;
import com.idlefishing.util.GameConstants;

/**
 * Abstract passive income producer.
 * <p>
 * Design patterns:
 * <ul>
 *   <li>Template Method – {@link #produce()} delegates to the {@link ProductionStrategy}
 *       injected at construction time.</li>
 *   <li>Strategy – different vessels use Linear, Logarithmic, or Exponential curves.</li>
 * </ul>
 */
public abstract class Vessel {

    private final String name;
    private final String description;
    private final double baseCost;
    private int count;
    private double productionMultiplier;
    private ProductionStrategy strategy;

    protected Vessel(String name, String description, double baseCost, ProductionStrategy strategy) {
        this.name = name;
        this.description = description;
        this.baseCost = baseCost;
        this.count = 0;
        this.productionMultiplier = 1.0;
        this.strategy = strategy;
    }

    // ── Abstract / polymorphic ─────────────────────────────────────────────

    /** Base production rate of a single instance of this vessel (€/s). */
    protected abstract double getBaseProductionRate();

    /** Short text / emoji label shown in the shop. */
    public abstract String getIcon();

    // ── Template method ────────────────────────────────────────────────────

    /**
     * Computes total € produced per second, delegating growth-curve
     * maths to the injected {@link ProductionStrategy}.
     */
    public double produce() {
        if (count <= 0) return 0.0;
        return strategy.calculate(getBaseProductionRate(), count) * productionMultiplier;
    }

    // ── Pricing ───────────────────────────────────────────────────────────

    /** Current buy price (exponential cost curve). */
    public double getCurrentCost() {
        return baseCost * Math.pow(GameConstants.PRICE_MULTIPLIER, count);
    }

    // ── State mutators ────────────────────────────────────────────────────

    public void purchase() { count++; }

    public void applyProductionMultiplier(double multiplier) {
        this.productionMultiplier *= multiplier;
    }

    public void setStrategy(ProductionStrategy strategy) {
        this.strategy = strategy;
    }

    // ── Accessors ─────────────────────────────────────────────────────────

    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getBaseCost() { return baseCost; }
    public int getCount() { return count; }
    public double getProductionMultiplier() { return productionMultiplier; }

    // Save/load helpers
    public void setCount(int v) { this.count = v; }
    public void setProductionMultiplier(double v) { this.productionMultiplier = v; }

    @Override
    public String toString() {
        return String.format("%s x%d  (€%.2f/s)", name, count, produce());
    }
}
