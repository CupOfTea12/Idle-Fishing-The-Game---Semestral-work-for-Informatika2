package com.idlefishing.model.vessel;

import com.idlefishing.strategy.LogarithmicProductionStrategy;
import com.idlefishing.util.GameConstants;

/** Industrial trawler — logarithmic scaling rewards early purchases. */
public class Trawler extends Vessel {

    public Trawler() {
        super("Trawler",
              "Industrial vessel with massive nets.  Logarithmic bonus per fleet.",
              GameConstants.TRAWLER_COST,
              new LogarithmicProductionStrategy());
    }

    @Override
    protected double getBaseProductionRate() { return GameConstants.TRAWLER_PROD; }

    @Override
    public String getIcon() { return "Trawler"; }
}
