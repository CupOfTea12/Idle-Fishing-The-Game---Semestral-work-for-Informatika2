package com.idlefishing.factory;

import com.idlefishing.model.spot.*;
import java.util.List;

/**
 * Factory Method for fishing spots.
 * Returns a mutable list so GameState can iterate polymorphically over
 * {@code List<FishingSpot>}.
 */
public final class SpotFactory {

    private SpotFactory() { }

    /** Creates the canonical four spots in progression order. */
    public static List<FishingSpot> createAll() {
        return new java.util.ArrayList<>(List.of(
            new RiverSpot(),
            new LakeSpot(),
            new SeaSpot(),
            new OceanSpot()
        ));
    }
}
