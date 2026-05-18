package com.idlefishing.model.upgrade;

/** Multiplies the production rate of a specific vessel type. */
public class VesselUpgrade extends Upgrade {

    private final String targetVesselName;
    private final double productionMultiplier;

    public VesselUpgrade(String id, String name, String description,
                         double cost, String targetVesselName, double productionMultiplier) {
        super(id, name, description, cost);
        this.targetVesselName = targetVesselName;
        this.productionMultiplier = productionMultiplier;
    }

    @Override
    public String getCategory() { return "Vessel"; }

    @Override
    public String getEffectDescription() {
        return String.format("x%.1f production for all %ss", productionMultiplier, targetVesselName);
    }

    public String getTargetVesselName() { return targetVesselName; }
    public double getProductionMultiplier() { return productionMultiplier; }
}
