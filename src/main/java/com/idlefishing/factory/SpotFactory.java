package com.idlefishing.factory;

import com.idlefishing.model.spot.*;
import java.util.ArrayList;
import java.util.List;

public final class SpotFactory {

    private SpotFactory() { }

    public static List<FishingSpot> createAll() {
        List<FishingSpot> spots = new ArrayList<>();
        spots.add(new RiverSpot());
        spots.add(new LakeSpot());
        spots.add(new SeaSpot());
        spots.add(new OceanSpot());
        return spots;
    }
}
