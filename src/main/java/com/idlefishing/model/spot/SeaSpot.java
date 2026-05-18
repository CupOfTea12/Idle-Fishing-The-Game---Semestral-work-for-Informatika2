package com.idlefishing.model.spot;

import com.idlefishing.model.fish.*;
import com.idlefishing.util.GameConstants;
import java.util.ArrayList;
import java.util.List;

/** Coastal seas — big hauls and dangerous waters. */
public class SeaSpot extends FishingSpot {

    public SeaSpot() {
        super("Sea", "Coastal waters with diverse marine life.",
              GameConstants.SEA_UNLOCK_COST, false);
    }

    @Override
    protected List<Fish> buildFishPool() {
        List<Fish> pool = new ArrayList<>();
        for (int i = 0; i < 3; i++) pool.add(new CommonFish("Mackerel",    8.0,  0.5));
        for (int i = 0; i < 2; i++) pool.add(new CommonFish("Sea Bass",   12.0,  2.0));
        for (int i = 0; i < 2; i++) pool.add(new RareFish("Atlantic Tuna", 60.0, 70.0));
        pool.add(new RareFish("Swordfish",          90.0, 110.0));
        pool.add(new LegendaryFish("Great White Shark", 350.0, 400.0));
        pool.add(new LegendaryFish("Marlin",          280.0, 180.0));
        return pool;
    }

    @Override
    protected double getBaseClickValue() { return 35.0; }

    @Override
    public String getEnvironmentFlavour() {
        return "Salt spray, seagulls, distant waves crashing on the hull...";
    }
}
