package com.idlefishing.factory;

import com.idlefishing.model.upgrade.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory Method for the default upgrade catalogue.
 * All upgrade IDs are stable across saves (used as keys in persistence).
 */
public final class UpgradeFactory {

    private UpgradeFactory() { }

    /** Returns the full progression upgrade list. */
    public static List<Upgrade> createAll() {
        List<Upgrade> list = new ArrayList<>();

        // ── Click upgrades (SpotUpgrade) ──────────────────────────────────
        list.add(new SpotUpgrade("river_c1", "Sharp Hook",
                "Double click value at River", 100.0, "River", 2.0));
        list.add(new SpotUpgrade("river_c2", "Master Bait",
                "Triple click value at River", 600.0, "River", 3.0));
        list.add(new SpotUpgrade("river_c3", "River Mastery",
                "5x click value at River", 5_000.0, "River", 5.0));

        list.add(new SpotUpgrade("lake_c1", "Premium Lure",
                "Double click value at Lake", 2_500.0, "Lake", 2.0));
        list.add(new SpotUpgrade("lake_c2", "Magic Bait",
                "Triple click value at Lake", 10_000.0, "Lake", 3.0));
        list.add(new SpotUpgrade("lake_c3", "Lake Mastery",
                "5x click value at Lake", 60_000.0, "Lake", 5.0));

        list.add(new SpotUpgrade("sea_c1", "Deep Rig",
                "Double click value at Sea", 30_000.0, "Sea", 2.0));
        list.add(new SpotUpgrade("sea_c2", "Sea Mastery",
                "5x click value at Sea", 200_000.0, "Sea", 5.0));

        list.add(new SpotUpgrade("ocean_c1", "Abyss Lure",
                "Double click value at Ocean", 250_000.0, "Ocean", 2.0));
        list.add(new SpotUpgrade("ocean_c2", "Ocean Mastery",
                "5x click value at Ocean", 1_500_000.0, "Ocean", 5.0));

        // ── Vessel production upgrades (VesselUpgrade) ────────────────────
        list.add(new VesselUpgrade("row_p1", "Bigger Nets (Rowboat)",
                "2x production for Rowboats", 500.0, "Rowboat", 2.0));
        list.add(new VesselUpgrade("row_p2", "Turbo Oars",
                "3x production for Rowboats", 3_000.0, "Rowboat", 3.0));
        list.add(new VesselUpgrade("row_p3", "Rowboat Fleet",
                "5x production for Rowboats", 20_000.0, "Rowboat", 5.0));

        list.add(new VesselUpgrade("boat_p1", "Extended Nets",
                "2x production for Fishing Boats", 6_000.0, "Fishing Boat", 2.0));
        list.add(new VesselUpgrade("boat_p2", "GPS Sonar",
                "3x production for Fishing Boats", 30_000.0, "Fishing Boat", 3.0));
        list.add(new VesselUpgrade("boat_p3", "Boat Syndicate",
                "5x production for Fishing Boats", 150_000.0, "Fishing Boat", 5.0));

        list.add(new VesselUpgrade("trawl_p1", "Industrial Nets",
                "2x production for Trawlers", 60_000.0, "Trawler", 2.0));
        list.add(new VesselUpgrade("trawl_p2", "Radar Arrays",
                "3x production for Trawlers", 300_000.0, "Trawler", 3.0));

        list.add(new VesselUpgrade("fact_p1", "Auto Processing",
                "2x production for Factory Ships", 600_000.0, "Factory Ship", 2.0));
        list.add(new VesselUpgrade("fact_p2", "AI Fleet",
                "5x production for Factory Ships", 5_000_000.0, "Factory Ship", 5.0));

        return list;
    }
}
