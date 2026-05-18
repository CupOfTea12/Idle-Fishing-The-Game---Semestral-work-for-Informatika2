package com.idlefishing.model.vessel;

import com.idlefishing.strategy.ProductionStrategy;
import com.idlefishing.util.GameConstants;

/** Base class for passive income producers. Subclasses pick a production curve via strategy. */
public abstract class Vessel {

    private final String name;
    private final String description;
    private final double baseCost;
    private int count;
    private double productionMultiplier;
    private ProductionStrategy strategy;

    protected Vessel(String name, String description, double baseCost, ProductionStrategy strategy) {
        this.name                 = name;
        this.description          = description;
        this.baseCost             = baseCost;
        this.count                = 0;
        this.productionMultiplier = 1.0;
        this.strategy             = strategy;
    }

    protected abstract double getBaseProductionRate();

    public abstract String getIcon();

    public double produce() {
        if (count <= 0) return 0.0;
        return strategy.calculate(getBaseProductionRate(), count) * productionMultiplier;
    }

    /** Price grows exponentially with how many you already own. */
    public double getCurrentCost() {
        return baseCost * Math.pow(GameConstants.PRICE_MULTIPLIER, count);
    }

    public void purchase()                             { count++; }
    public void applyProductionMultiplier(double m)    { this.productionMultiplier *= m; }
    public void setStrategy(ProductionStrategy s)      { this.strategy = s; }

    public String getName()              { return name; }
    public String getDescription()       { return description; }
    public double getBaseCost()          { return baseCost; }
    public int    getCount()             { return count; }
    public double getProductionMultiplier() { return productionMultiplier; }

    public void setCount(int v)                  { this.count = v; }
    public void setProductionMultiplier(double v) { this.productionMultiplier = v; }

    @Override
    public String toString() {
        return String.format("%s x%d  (€%.2f/s)", name, count, produce());
    }
}
