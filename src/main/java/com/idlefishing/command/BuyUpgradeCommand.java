package com.idlefishing.command;

import com.idlefishing.model.GameState;
import com.idlefishing.model.spot.FishingSpot;
import com.idlefishing.model.upgrade.SpotUpgrade;
import com.idlefishing.model.upgrade.Upgrade;
import com.idlefishing.model.upgrade.VesselUpgrade;
import com.idlefishing.model.vessel.Vessel;
import com.idlefishing.observer.EventBus;
import com.idlefishing.observer.EventType;

public class BuyUpgradeCommand implements Command {

    private final GameState state;
    private final Upgrade   upgrade;

    public BuyUpgradeCommand(GameState state, Upgrade upgrade) {
        this.state   = state;
        this.upgrade = upgrade;
    }

    @Override
    public boolean execute() {
        if (upgrade.isPurchased())                return false;
        if (state.getMoney() < upgrade.getCost()) return false;

        state.subtractMoney(upgrade.getCost());
        upgrade.purchase();
        applyEffect();

        EventBus.getInstance().publish(EventType.UPGRADE_PURCHASED, upgrade);
        EventBus.getInstance().publish(EventType.MONEY_CHANGED, state.getMoney());
        return true;
    }

    private void applyEffect() {
        if (upgrade instanceof VesselUpgrade) {
            VesselUpgrade vu = (VesselUpgrade) upgrade;
            for (Vessel v : state.getVessels()) {
                if (v.getName().equals(vu.getTargetVesselName())) {
                    v.applyProductionMultiplier(vu.getProductionMultiplier());
                }
            }
        } else if (upgrade instanceof SpotUpgrade) {
            SpotUpgrade su = (SpotUpgrade) upgrade;
            for (FishingSpot spot : state.getFishingSpots()) {
                if (spot.getName().equals(su.getTargetSpotName())) {
                    spot.applyClickMultiplier(su.getClickMultiplier());
                }
            }
        }
    }

    @Override
    public String getDescription() {
        return "Buy upgrade: " + upgrade.getName();
    }
}
