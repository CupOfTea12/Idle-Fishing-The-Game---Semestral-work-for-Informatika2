package com.idlefishing.model.vessel;

import com.idlefishing.strategy.LinearProductionStrategy;
import com.idlefishing.util.GameConstants;

/** A proper fishing boat with nets. */
public class FishingBoat extends Vessel {

    public FishingBoat() {
        super("Fishing Boat",
              "Proper boat with nets.  A solid mid-tier earner.",
              GameConstants.FISHING_BOAT_COST,
              new LinearProductionStrategy());
    }

    @Override
    protected double getBaseProductionRate() { return GameConstants.FISHING_BOAT_PROD; }

    @Override
    public String getIcon() { return "Fishing Boat"; }
}
