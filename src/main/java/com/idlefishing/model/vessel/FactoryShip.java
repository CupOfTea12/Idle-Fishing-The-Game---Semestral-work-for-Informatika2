package com.idlefishing.model.vessel;

import com.idlefishing.strategy.ExponentialProductionStrategy;
import com.idlefishing.util.GameConstants;

/** Floating factory — exponential scaling, absurd late-game output. */
public class FactoryShip extends Vessel {

    public FactoryShip() {
        super("Factory Ship",
              "Floating fish-processing plant.  Exponential output per fleet.",
              GameConstants.FACTORY_SHIP_COST,
              new ExponentialProductionStrategy());
    }

    @Override
    protected double getBaseProductionRate() { return GameConstants.FACTORY_SHIP_PROD; }

    @Override
    public String getIcon() { return "Factory Ship"; }
}
