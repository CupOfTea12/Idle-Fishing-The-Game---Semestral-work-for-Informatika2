package com.idlefishing.model.spot;

import com.idlefishing.model.fish.*;
import com.idlefishing.util.GameConstants;
import java.util.ArrayList;
import java.util.List;

/** Serene lake — bigger fish, quiet surface. */
public class LakeSpot extends FishingSpot {

    public LakeSpot() {
        super("Lake", "A serene lake with larger prey.",
              GameConstants.LAKE_UNLOCK_COST, false);
    }

    @Override
    protected List<Fish> buildFishPool() {
        List<Fish> pool = new ArrayList<>();
        for (int i = 0; i < 3; i++) pool.add(new CommonFish("Trout",         4.0, 0.8));
        for (int i = 0; i < 2; i++) pool.add(new CommonFish("Bass",          6.0, 1.5));
        for (int i = 0; i < 2; i++) pool.add(new RareFish("Zander",         22.0, 3.0));
        pool.add(new RareFish("Rainbow Trout",  35.0, 4.0));
        pool.add(new LegendaryFish("King Salmon", 120.0, 14.0));
        pool.add(new LegendaryFish("Lake Monster", 200.0, 80.0));
        return pool;
    }

    @Override
    protected double getBaseClickValue() { return 9.0; }

    @Override
    public String getEnvironmentFlavour() {
        return "Still waters, distant mountains, occasional splash...";
    }
}
