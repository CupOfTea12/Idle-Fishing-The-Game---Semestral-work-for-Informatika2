package com.idlefishing.factory;

import com.idlefishing.model.vessel.*;
import java.util.ArrayList;
import java.util.List;

public final class VesselFactory {

    private VesselFactory() { }

    public static List<Vessel> createAll() {
        List<Vessel> vessels = new ArrayList<>();
        vessels.add(new Rowboat());
        vessels.add(new FishingBoat());
        vessels.add(new Trawler());
        vessels.add(new FactoryShip());
        return vessels;
    }
}
