package com.idlefishing.model.spot;

import com.idlefishing.model.fish.*;
import com.idlefishing.util.GameConstants;
import java.util.ArrayList;
import java.util.List;

/** The starting river location. Abundant common fish, minimal investment. */
public class RiverSpot extends FishingSpot {

    public RiverSpot() {
        super("River", "A peaceful river with abundant small fish.",
              GameConstants.LAKE_UNLOCK_COST, true);
    }

    @Override
    protected List<Fish> buildFishPool() {
        List<Fish> pool = new ArrayList<>();
        // Common ×5
        for (int i = 0; i < 3; i++) pool.add(new CommonFish("Perch",  1.5, 0.3));
        for (int i = 0; i < 2; i++) pool.add(new CommonFish("Carp",   2.5, 1.2));
        // Rare ×3
        for (int i = 0; i < 2; i++) pool.add(new RareFish("Pike",    10.0, 2.5));
        pool.add(new RareFish("Catfish", 15.0, 4.0));
        // Legendary ×2
        pool.add(new LegendaryFish("Golden Carp", 50.0, 6.0));
        pool.add(new LegendaryFish("Albino Sturgeon", 80.0, 12.0));
        return pool;
    }

    @Override
    protected double getBaseClickValue() { return 2.0; }

    @Override
    public String getEnvironmentFlavour() {
        return "Clear running water, mossy rocks, morning mist...";
    }
}
