package com.idlefishing.command;

import com.idlefishing.model.GameState;
import com.idlefishing.observer.EventBus;
import com.idlefishing.observer.EventType;
import com.idlefishing.util.GameConstants;

public class PrestigeCommand implements Command {

    private final GameState state;

    public PrestigeCommand(GameState state) {
        this.state = state;
    }

    @Override
    public boolean execute() {
        if (state.getMoney() < GameConstants.PRESTIGE_THRESHOLD) return false;
        state.prestige();
        EventBus.getInstance().publish(EventType.PRESTIGE_TRIGGERED, state.getPrestigeLevel());
        return true;
    }

    @Override
    public String getDescription() {
        return "Prestige → level " + (state.getPrestigeLevel() + 1);
    }
}
