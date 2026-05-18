package com.idlefishing.model.vessel;

import com.idlefishing.strategy.LinearProductionStrategy;
import com.idlefishing.util.GameConstants;

/** The cheapest vessel — simple linear scaling. */
public class Rowboat extends Vessel {

    public Rowboat() {
        super("Rowboat",
              "A humble rowboat.  Slow, but it gets the job done.",
              GameConstants.ROWBOAT_COST,
              new LinearProductionStrategy());
    }

    @Override
    protected double getBaseProductionRate() { return GameConstants.ROWBOAT_PROD; }

    @Override
    public String getIcon() { return "Rowboat"; }
}
