package com.idlefishing.factory;

import com.idlefishing.model.vessel.*;
import java.util.List;

/**
 * Factory Method for vessels.
 * Returns all four vessel types in cost order so the shop can iterate
 * polymorphically over {@code List<Vessel>}.
 */
public final class VesselFactory {

    private VesselFactory() { }

    /** Creates one instance of every vessel type. */
    public static List<Vessel> createAll() {
        return new java.util.ArrayList<>(List.of(
            new Rowboat(),
            new FishingBoat(),
            new Trawler(),
            new FactoryShip()
        ));
    }
}
