package com.idlefishing.persistence;

import com.idlefishing.model.GameState;
import com.idlefishing.model.spot.FishingSpot;
import com.idlefishing.model.upgrade.Upgrade;
import com.idlefishing.model.vessel.Vessel;
import com.idlefishing.observer.EventBus;
import com.idlefishing.observer.EventType;
import com.idlefishing.util.GameConstants;
import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton save/load manager.
 * Persists game state as a Java Properties file.
 * On load, calculates offline earnings for up to {@value GameConstants#MAX_OFFLINE_HOURS} hours.
 *
 * <p>Pattern: Singleton.
 */
public final class SaveManager {

    private static final Logger LOG = Logger.getLogger(SaveManager.class.getName());
    private static SaveManager instance;

    /** Offline earnings calculated during the most recent load (shown in UI). */
    private double lastOfflineEarnings;
    private long   lastOfflineSeconds;

    private SaveManager() { }

    public static SaveManager getInstance() {
        if (instance == null) {
            instance = new SaveManager();
        }
        return instance;
    }

    // ── Save ──────────────────────────────────────────────────────────────

    public void save(GameState state) {
        Properties p = new Properties();
        p.setProperty("money",        String.valueOf(state.getMoney()));
        p.setProperty("totalEarned",  String.valueOf(state.getTotalEarned()));
        p.setProperty("fishCaught",   String.valueOf(state.getTotalFishCaught()));
        p.setProperty("prestige",     String.valueOf(state.getPrestigeLevel()));
        p.setProperty("prestBonus",   String.valueOf(state.getPrestigeBonus()));
        p.setProperty("spotIndex",    String.valueOf(state.getCurrentSpotIndex()));
        p.setProperty("saveTime",     String.valueOf(System.currentTimeMillis()));

        List<FishingSpot> spots = state.getFishingSpots();
        for (int i = 0; i < spots.size(); i++) {
            FishingSpot s = spots.get(i);
            String pfx = "spot." + i + ".";
            p.setProperty(pfx + "unlocked",       String.valueOf(s.isUnlocked()));
            p.setProperty(pfx + "level",          String.valueOf(s.getLevel()));
            p.setProperty(pfx + "clickMult",      String.valueOf(s.getClickMultiplier()));
        }

        List<Vessel> vessels = state.getVessels();
        for (int i = 0; i < vessels.size(); i++) {
            Vessel v = vessels.get(i);
            String pfx = "vessel." + i + ".";
            p.setProperty(pfx + "count",     String.valueOf(v.getCount()));
            p.setProperty(pfx + "prodMult",  String.valueOf(v.getProductionMultiplier()));
        }

        List<Upgrade> upgrades = state.getUpgrades();
        for (int i = 0; i < upgrades.size(); i++) {
            p.setProperty("upgrade." + i + ".purchased",
                          String.valueOf(upgrades.get(i).isPurchased()));
        }

        try (OutputStream os = new FileOutputStream(GameConstants.SAVE_FILE)) {
            p.store(os, "Idle Fishing — save file");
            state.setLastSaveTime(System.currentTimeMillis());
            EventBus.getInstance().publish(EventType.GAME_SAVED);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Could not save game", e);
        }
    }

    // ── Load ──────────────────────────────────────────────────────────────

    /**
     * Loads state from disk.  Returns {@code false} if no save file exists.
     * After a successful load the offline earnings are available via
     * {@link #getLastOfflineEarnings()}.
     */
    public boolean load(GameState state) {
        File file = new File(GameConstants.SAVE_FILE);
        if (!file.exists()) return false;

        Properties p = new Properties();
        try (InputStream is = new FileInputStream(file)) {
            p.load(is);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Could not load game", e);
            return false;
        }

        state.setMoney(           parseDouble(p, "money",       0));
        state.setTotalEarned(     parseDouble(p, "totalEarned", 0));
        state.setTotalFishCaught( parseLong(  p, "fishCaught",  0));
        state.setPrestigeLevel(   parseInt(   p, "prestige",    0));
        state.setPrestigeBonus(   parseDouble(p, "prestBonus",  1.0));
        state.setCurrentSpotIndex(parseInt(   p, "spotIndex",   0));

        long savedTime = parseLong(p, "saveTime", System.currentTimeMillis());
        state.setLastSaveTime(savedTime);

        List<FishingSpot> spots = state.getFishingSpots();
        for (int i = 0; i < spots.size(); i++) {
            FishingSpot s = spots.get(i);
            String pfx = "spot." + i + ".";
            s.setUnlocked(      Boolean.parseBoolean(p.getProperty(pfx + "unlocked", "false")));
            s.setLevel(         parseInt(   p, pfx + "level",    1));
            s.setClickMultiplier(parseDouble(p, pfx + "clickMult", 1.0));
        }
        if (!spots.isEmpty()) spots.get(0).setUnlocked(true); // River always unlocked

        List<Vessel> vessels = state.getVessels();
        for (int i = 0; i < vessels.size(); i++) {
            Vessel v = vessels.get(i);
            String pfx = "vessel." + i + ".";
            v.setCount(            parseInt(   p, pfx + "count",    0));
            v.setProductionMultiplier(parseDouble(p, pfx + "prodMult", 1.0));
        }

        List<Upgrade> upgrades = state.getUpgrades();
        for (int i = 0; i < upgrades.size(); i++) {
            if (Boolean.parseBoolean(p.getProperty("upgrade." + i + ".purchased", "false"))) {
                upgrades.get(i).purchase();
            }
        }

        calculateOfflineEarnings(state, savedTime);
        EventBus.getInstance().publish(EventType.GAME_LOADED);
        EventBus.getInstance().publish(EventType.OFFLINE_EARNINGS, lastOfflineEarnings);
        return true;
    }

    // ── Offline earnings ──────────────────────────────────────────────────

    private void calculateOfflineEarnings(GameState state, long savedTime) {
        long now        = System.currentTimeMillis();
        long elapsedMs  = now - savedTime;
        long maxMs      = (long) GameConstants.MAX_OFFLINE_HOURS * 3_600_000L;
        long effectiveMs = Math.min(elapsedMs, maxMs);
        lastOfflineSeconds = effectiveMs / 1000L;

        double perSec   = state.getTotalProductionPerSecond();
        lastOfflineEarnings = perSec * lastOfflineSeconds;

        if (lastOfflineEarnings > 0) {
            state.addMoney(lastOfflineEarnings);
        }
    }

    public double getLastOfflineEarnings() { return lastOfflineEarnings; }
    public long   getLastOfflineSeconds()  { return lastOfflineSeconds; }

    // ── Parse helpers ─────────────────────────────────────────────────────

    private static double parseDouble(Properties p, String key, double def) {
        try { return Double.parseDouble(p.getProperty(key, String.valueOf(def))); }
        catch (NumberFormatException e) { return def; }
    }

    private static int parseInt(Properties p, String key, int def) {
        try { return Integer.parseInt(p.getProperty(key, String.valueOf(def))); }
        catch (NumberFormatException e) { return def; }
    }

    private static long parseLong(Properties p, String key, long def) {
        try { return Long.parseLong(p.getProperty(key, String.valueOf(def))); }
        catch (NumberFormatException e) { return def; }
    }
}
