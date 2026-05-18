package com.idlefishing.model.spot;

import com.idlefishing.model.fish.Fish;
import java.util.List;
import java.util.Random;

/** Base class for all fishing locations. Subclasses define their fish pool and click value. */
public abstract class FishingSpot {

    private final String name;
    private final String description;
    private final double unlockCost;
    private boolean unlocked;
    private int level;
    private double clickMultiplier;

    private final List<Fish> fishPool;
    private final Random random = new Random();

    protected FishingSpot(String name, String description, double unlockCost, boolean startUnlocked) {
        this.name          = name;
        this.description   = description;
        this.unlockCost    = unlockCost;
        this.unlocked      = startUnlocked;
        this.level         = 1;
        this.clickMultiplier = 1.0;
        this.fishPool      = buildFishPool();
    }

    /** Weighted list of catchable fish — higher rarity = fewer entries. */
    protected abstract List<Fish> buildFishPool();

    protected abstract double getBaseClickValue();

    public abstract String getEnvironmentFlavour();

    public Fish catchFish() {
        if (fishPool.isEmpty()) return null;
        return fishPool.get(random.nextInt(fishPool.size()));
    }

    public double getClickValue() {
        return getBaseClickValue() * clickMultiplier * (1.0 + (level - 1) * 0.15);
    }

    public void applyClickMultiplier(double multiplier) { this.clickMultiplier *= multiplier; }
    public void levelUp()    { level++; }
    public void unlock()     { unlocked = true; }

    public String getName()           { return name; }
    public String getDescription()    { return description; }
    public double getUnlockCost()     { return unlockCost; }
    public boolean isUnlocked()       { return unlocked; }
    public int getLevel()             { return level; }
    public double getClickMultiplier(){ return clickMultiplier; }
    public List<Fish> getFishPool()   { return fishPool; }

    public void setUnlocked(boolean v)       { this.unlocked = v; }
    public void setLevel(int v)              { this.level = v; }
    public void setClickMultiplier(double v) { this.clickMultiplier = v; }
}
