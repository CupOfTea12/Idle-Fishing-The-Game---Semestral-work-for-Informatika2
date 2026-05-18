package com.idlefishing.model.upgrade;

/** Multiplies the manual-click value of a specific fishing spot. */
public class SpotUpgrade extends Upgrade {

    private final String targetSpotName;
    private final double clickMultiplier;

    public SpotUpgrade(String id, String name, String description,
                       double cost, String targetSpotName, double clickMultiplier) {
        super(id, name, description, cost);
        this.targetSpotName = targetSpotName;
        this.clickMultiplier = clickMultiplier;
    }

    @Override
    public String getCategory() { return "Spot"; }

    @Override
    public String getEffectDescription() {
        return String.format("x%.1f click value at %s", clickMultiplier, targetSpotName);
    }

    public String getTargetSpotName() { return targetSpotName; }
    public double getClickMultiplier() { return clickMultiplier; }
}
