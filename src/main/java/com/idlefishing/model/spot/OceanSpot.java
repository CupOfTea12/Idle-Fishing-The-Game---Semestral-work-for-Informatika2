package com.idlefishing.model.spot;

import com.idlefishing.model.fish.*;
import com.idlefishing.util.GameConstants;
import java.util.ArrayList;
import java.util.List;

/** The deep ocean — home to legendary and exotic creatures. */
public class OceanSpot extends FishingSpot {

    public OceanSpot() {
        super("Ocean", "The abyss — mythical creatures lurk below.",
              GameConstants.OCEAN_UNLOCK_COST, false);
    }

    @Override
    protected List<Fish> buildFishPool() {
        List<Fish> pool = new ArrayList<>();
        for (int i = 0; i < 3; i++) pool.add(new RareFish("Deep-Sea Tuna", 130.0,  80.0));
        for (int i = 0; i < 2; i++) pool.add(new LegendaryFish("Giant Squid",  600.0, 250.0));
        pool.add(new LegendaryFish("Basking Shark",  500.0, 600.0));
        pool.add(new ExoticFish("Blue Whale",      2_500.0, 100_000.0));
        pool.add(new ExoticFish("Kraken",          6_000.0, 500_000.0));
        pool.add(new ExoticFish("Leviathan",      12_000.0, 999_999.0));
        return pool;
    }

    @Override
    protected double getBaseClickValue() { return 120.0; }

    @Override
    public String getEnvironmentFlavour() {
        return "Endless horizon, ink-black depths, unknown entities below...";
    }
}
