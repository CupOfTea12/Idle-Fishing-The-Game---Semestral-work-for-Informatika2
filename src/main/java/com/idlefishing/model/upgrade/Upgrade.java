package com.idlefishing.model.upgrade;

/** A one-time purchasable bonus. Subclasses define what actually gets boosted. */
public abstract class Upgrade {

    private final String id;
    private final String name;
    private final String description;
    private final double cost;
    private boolean purchased;

    protected Upgrade(String id, String name, String description, double cost) {
        this.id          = id;
        this.name        = name;
        this.description = description;
        this.cost        = cost;
        this.purchased   = false;
    }

    public abstract String getCategory();
    public abstract String getEffectDescription();

    public String  getId()          { return id; }
    public String  getName()        { return name; }
    public String  getDescription() { return description; }
    public double  getCost()        { return cost; }
    public boolean isPurchased()    { return purchased; }
    public void    purchase()       { this.purchased = true; }
}
