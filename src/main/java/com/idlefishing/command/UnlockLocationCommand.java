package com.idlefishing.command;

import com.idlefishing.model.GameState;
import com.idlefishing.model.spot.FishingSpot;
import com.idlefishing.observer.EventBus;
import com.idlefishing.observer.EventType;

/** Command: pay to unlock a fishing location and switch to it. */
public class UnlockLocationCommand implements Command {

    private final GameState state;
    private final FishingSpot spot;
    private final int spotIndex;

    public UnlockLocationCommand(GameState state, FishingSpot spot, int spotIndex) {
        this.state     = state;
        this.spot      = spot;
        this.spotIndex = spotIndex;
    }

    @Override
    public boolean execute() {
        if (spot.isUnlocked()) {
            // Just switch to this spot
            state.setCurrentSpotIndex(spotIndex);
            EventBus.getInstance().publish(EventType.LOCATION_CHANGED, spotIndex);
            return true;
        }
        if (state.getMoney() < spot.getUnlockCost()) return false;

        state.subtractMoney(spot.getUnlockCost());
        spot.unlock();
        state.setCurrentSpotIndex(spotIndex);

        EventBus.getInstance().publish(EventType.LOCATION_UNLOCKED, spot);
        EventBus.getInstance().publish(EventType.LOCATION_CHANGED, spotIndex);
        EventBus.getInstance().publish(EventType.MONEY_CHANGED, state.getMoney());
        return true;
    }

    @Override
    public String getDescription() {
        return spot.isUnlocked()
               ? "Switch to " + spot.getName()
               : "Unlock " + spot.getName() + " for €" + String.format("%,.0f", spot.getUnlockCost());
    }
}
