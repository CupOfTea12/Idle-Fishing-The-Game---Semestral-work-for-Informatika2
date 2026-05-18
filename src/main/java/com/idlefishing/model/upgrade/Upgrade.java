package com.idlefishing.model.upgrade;

/**
 * Abstract base for all purchasable upgrades.
 * Subclasses define what kind of bonus they provide (Vessel production or Spot clicks).
 */
public abstract class Upgrade {

    private final String id;
    private final String name;
    private final String description;
    private final double cost;
    private boolean purchased;

    protected Upgrade(String id, String name, String description, double cost) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.purchased = false;
    }

    /** Category label shown in the shop panel ("Vessel" or "Spot"). */
    public abstract String getCategory();

    /** One-line description of the numeric effect. */
    public abstract String getEffectDescription();

    // ── Accessors ─────────────────────────────────────────────────────────────

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getCost() { return cost; }
    public boolean isPurchased() { return purchased; }
    public void purchase() { this.purchased = true; }
}
