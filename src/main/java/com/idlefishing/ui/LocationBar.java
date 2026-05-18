package com.idlefishing.ui;

import com.idlefishing.command.CommandHistory;
import com.idlefishing.command.UnlockLocationCommand;
import com.idlefishing.model.GameState;
import com.idlefishing.model.spot.FishingSpot;
import com.idlefishing.observer.EventBus;
import com.idlefishing.observer.EventType;
import com.idlefishing.util.UIColors;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Horizontal row of location-switch / unlock buttons.
 * Subscribes to MONEY_CHANGED and repaints button states whenever money changes.
 */
public class LocationBar extends JPanel {

    private final GameState state;
    private final CommandHistory history;
    private final List<JButton> locationButtons = new ArrayList<>();

    public LocationBar(GameState state, CommandHistory history) {
        this.state   = state;
        this.history = history;
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 6, 0));
        buildButtons();
        subscribeToEvents();
    }

    private void buildButtons() {
        removeAll();
        locationButtons.clear();
        List<FishingSpot> spots = state.getFishingSpots();
        for (int i = 0; i < spots.size(); i++) {
            FishingSpot spot = spots.get(i);
            int idx = i;
            JButton btn = createLocationButton(spot, idx);
            locationButtons.add(btn);
            add(btn);
        }
    }

    private JButton createLocationButton(FishingSpot spot, int idx) {
        JButton btn = new JButton();
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(UIColors.FONT_BODY);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 32));
        btn.addActionListener(e -> {
            UnlockLocationCommand cmd = new UnlockLocationCommand(state, spot, idx);
            if (cmd.execute()) {
                history.push(cmd);
                refreshButtons();
            }
        });
        updateButton(btn, spot, idx);
        return btn;
    }

    private void updateButton(JButton btn, FishingSpot spot, int idx) {
        boolean active = (state.getCurrentSpotIndex() == idx);
        boolean affordable = state.getMoney() >= spot.getUnlockCost();

        if (!spot.isUnlocked()) {
            btn.setText(spot.getName() + " (€" + formatCost(spot.getUnlockCost()) + ")");
            btn.setBackground(affordable ? UIColors.BG_MEDIUM : UIColors.BG_DARK);
            btn.setForeground(affordable ? UIColors.GOLD : UIColors.TEXT_DIM);
        } else {
            btn.setText(spot.getName());
            btn.setBackground(active ? UIColors.ACCENT : UIColors.BG_MEDIUM);
            btn.setForeground(active ? Color.WHITE : UIColors.TEXT);
        }
    }

    private void refreshButtons() {
        List<FishingSpot> spots = state.getFishingSpots();
        for (int i = 0; i < locationButtons.size() && i < spots.size(); i++) {
            updateButton(locationButtons.get(i), spots.get(i), i);
        }
        repaint();
    }

    private void subscribeToEvents() {
        EventBus bus = EventBus.getInstance();
        bus.subscribe(EventType.MONEY_CHANGED,    e -> SwingUtilities.invokeLater(this::refreshButtons));
        bus.subscribe(EventType.LOCATION_CHANGED, e -> SwingUtilities.invokeLater(this::refreshButtons));
        bus.subscribe(EventType.LOCATION_UNLOCKED,e -> SwingUtilities.invokeLater(this::refreshButtons));
    }

    private static String formatCost(double cost) {
        if (cost >= 1_000_000) return String.format("%.1fM", cost / 1_000_000);
        if (cost >= 1_000)     return String.format("%.1fK", cost / 1_000);
        return String.format("%.0f", cost);
    }
}
