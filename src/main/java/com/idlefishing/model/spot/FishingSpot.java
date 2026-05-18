package com.idlefishing.model.spot;

import com.idlefishing.model.fish.Fish;
import java.util.List;
import java.util.Random;

/**
 * Abstract fishing location.  Concrete subclasses define their fish pool,
 * base click value, and environment flavour text.
 *
 * <p>Design patterns:
 * <ul>
 *   <li>Template Method – {@link #catchFish()} delegates the pool to
 *       {@link #buildFishPool()}, which subclasses override.</li>
 * </ul>
 */
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
        this.name = name;
        this.description = description;
        this.unlockCost = unlockCost;
        this.unlocked = startUnlocked;
        this.level = 1;
        this.clickMultiplier = 1.0;
        this.fishPool = buildFishPool();
    }

    // ── Abstract / polymorphic ─────────────────────────────────────────────

    /**
     * Builds the weighted fish pool once at construction.
     * Higher-rarity fish should appear fewer times in the list.
     */
    protected abstract List<Fish> buildFishPool();

    /** Base €-value per manual click (before multipliers). */
    protected abstract double getBaseClickValue();

    /** Short atmospheric description of the surroundings. */
    public abstract String getEnvironmentFlavour();

    // ── Template method ────────────────────────────────────────────────────

    /**
     * Randomly selects a fish from the pool.  The distribution is implicit
     * in how many times each fish appears in the list.
     */
    public Fish catchFish() {
        if (fishPool.isEmpty()) return null;
        return fishPool.get(random.nextInt(fishPool.size()));
    }

    /** Total click value after all multiplier upgrades and level bonuses. */
    public double getClickValue() {
        return getBaseClickValue() * clickMultiplier * (1.0 + (level - 1) * 0.15);
    }

    // ── State mutators ─────────────────────────────────────────────────────

    public void applyClickMultiplier(double multiplier) {
        this.clickMultiplier *= multiplier;
    }

    public void levelUp() { level++; }

    public void unlock() { unlocked = true; }

    // ── Accessors ──────────────────────────────────────────────────────────

    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getUnlockCost() { return unlockCost; }
    public boolean isUnlocked() { return unlocked; }
    public int getLevel() { return level; }
    public double getClickMultiplier() { return clickMultiplier; }
    public List<Fish> getFishPool() { return fishPool; }

    // Save/load helpers
    public void setUnlocked(boolean v) { this.unlocked = v; }
    public void setLevel(int v) { this.level = v; }
    public void setClickMultiplier(double v) { this.clickMultiplier = v; }
}
