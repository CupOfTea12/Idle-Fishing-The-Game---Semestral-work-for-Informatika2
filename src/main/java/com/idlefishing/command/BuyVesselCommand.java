package com.idlefishing.command;

import com.idlefishing.model.GameState;
import com.idlefishing.model.vessel.Vessel;
import com.idlefishing.observer.EventBus;
import com.idlefishing.observer.EventType;


public class BuyVesselCommand implements Command {

    private final GameState state;
    private final Vessel vessel;

    public BuyVesselCommand(GameState state, Vessel vessel) {
        this.state  = state;
        this.vessel = vessel;
    }

    @Override
    public boolean execute() {
        double cost = vessel.getCurrentCost();
        if (state.getMoney() < cost) return false;

        state.subtractMoney(cost);
        vessel.purchase();

        EventBus.getInstance().publish(EventType.VESSEL_PURCHASED, vessel);
        EventBus.getInstance().publish(EventType.MONEY_CHANGED, state.getMoney());
        return true;
    }

    @Override
    public String getDescription() {
        return "Buy " + vessel.getName() + " for €" + String.format("%.2f", vessel.getCurrentCost());
    }
}
