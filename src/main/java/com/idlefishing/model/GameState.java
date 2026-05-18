package com.idlefishing.model;

import com.idlefishing.factory.SpotFactory;
import com.idlefishing.factory.UpgradeFactory;
import com.idlefishing.factory.VesselFactory;
import com.idlefishing.model.spot.FishingSpot;
import com.idlefishing.model.upgrade.Upgrade;
import com.idlefishing.model.vessel.Vessel;
import com.idlefishing.util.GameConstants;
import java.util.List;

/**
 * Singleton that owns all mutable game data.
 * Every other component reads / writes through this class so that
 * the full state is always consistent and easy to serialize.
 *
 * <p>Pattern: Singleton.
 */
public final class GameState {

    private static GameState instance;

    // ── Financial ──────────────────────────────────────────────────────────
    private double money;
    private double totalEarned;
    private long totalFishCaught;

    // ── Prestige ──────────────────────────────────────────────────────────
    private int prestigeLevel;
    private double prestigeBonus;   // multiplicative, recomputed on each prestige

    // ── Location ──────────────────────────────────────────────────────────
    private int currentSpotIndex;

    // ── Market ────────────────────────────────────────────────────────────
    private double marketMultiplier;

    // ── Timing ────────────────────────────────────────────────────────────
    private long lastSaveTime;

    // ── Collections (polymorphic Lists) ──────────────────────────────────
    private List<FishingSpot> fishingSpots;
    private List<Vessel> vessels;
    private List<Upgrade> upgrades;

    // ─────────────────────────────────────────────────────────────────────

    private GameState() {
        initFresh(false);
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    // ── Initialisation ────────────────────────────────────────────────────

    private void initFresh(boolean keepPrestige) {
        int savedPrestige = keepPrestige ? prestigeLevel : 0;
        double savedBonus  = keepPrestige ? prestigeBonus  : 1.0;

        money            = 0.0;
        totalEarned      = 0.0;
        totalFishCaught  = 0L;
        prestigeLevel    = savedPrestige;
        prestigeBonus    = savedBonus;
        currentSpotIndex = 0;
        marketMultiplier = 1.0;
        lastSaveTime     = System.currentTimeMillis();

        fishingSpots = SpotFactory.createAll();
        vessels      = VesselFactory.createAll();
        upgrades     = UpgradeFactory.createAll();
    }

    /**
     * Prestige: increments level, recalculates the permanent bonus,
     * then resets everything except the prestige state.
     */
    public void prestige() {
        prestigeLevel++;
        prestigeBonus = Math.pow(1.0 + GameConstants.PRESTIGE_BONUS_PER_LEVEL, prestigeLevel);
        initFresh(true);
    }

    // ── Derived stats ─────────────────────────────────────────────────────

    /** Sum of all vessel production * prestige bonus (€/s). */
    public double getTotalProductionPerSecond() {
        double raw = vessels.stream().mapToDouble(Vessel::produce).sum();
        return raw * prestigeBonus;
    }

    public boolean canPrestige() {
        return money >= GameConstants.PRESTIGE_THRESHOLD;
    }

    // ── Money helpers ─────────────────────────────────────────────────────

    public void addMoney(double amount) {
        money       += amount;
        totalEarned += amount;
    }

    public void subtractMoney(double amount) {
        money = Math.max(0.0, money - amount);
    }

    public void incrementFishCaught() { totalFishCaught++; }

    // ── Accessors / mutators ──────────────────────────────────────────────

    public double getMoney()              { return money; }
    public void   setMoney(double v)      { money = v; }

    public double getTotalEarned()        { return totalEarned; }
    public void   setTotalEarned(double v){ totalEarned = v; }

    public long   getTotalFishCaught()    { return totalFishCaught; }
    public void   setTotalFishCaught(long v){ totalFishCaught = v; }

    public int    getPrestigeLevel()      { return prestigeLevel; }
    public void   setPrestigeLevel(int v) { prestigeLevel = v; }

    public double getPrestigeBonus()      { return prestigeBonus; }
    public void   setPrestigeBonus(double v){ prestigeBonus = v; }

    public int    getCurrentSpotIndex()   { return currentSpotIndex; }
    public void   setCurrentSpotIndex(int v){ currentSpotIndex = v; }

    public FishingSpot getCurrentSpot()   { return fishingSpots.get(currentSpotIndex); }

    public double getMarketMultiplier()   { return marketMultiplier; }
    public void   setMarketMultiplier(double v){ marketMultiplier = v; }

    public long   getLastSaveTime()       { return lastSaveTime; }
    public void   setLastSaveTime(long v) { lastSaveTime = v; }

    public List<FishingSpot> getFishingSpots() { return fishingSpots; }
    public List<Vessel>      getVessels()      { return vessels; }
    public List<Upgrade>     getUpgrades()     { return upgrades; }
}
