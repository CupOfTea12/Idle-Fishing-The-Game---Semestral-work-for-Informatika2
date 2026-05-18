package com.idlefishing.ui;

import com.idlefishing.command.CommandHistory;
import com.idlefishing.command.PrestigeCommand;
import com.idlefishing.model.GameState;
import com.idlefishing.observer.EventBus;
import com.idlefishing.observer.EventType;
import com.idlefishing.util.GameConstants;
import com.idlefishing.util.UIColors;
import javax.swing.*;
import java.awt.*;

/**
 * Bottom status bar: €/s, fish count, prestige info, and the Prestige button.
 */
public class StatsPanel extends JPanel {

    private final GameState state;
    private final CommandHistory history;

    private JLabel perSecLabel;
    private JLabel fishLabel;
    private JLabel prestigeLabel;
    private JButton prestigeBtn;

    public StatsPanel(GameState state, CommandHistory history) {
        this.state   = state;
        this.history = history;
        initUI();
        subscribeToEvents();
    }

    private void initUI() {
        setBackground(UIColors.BG_DARKEST);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIColors.BORDER));
        setLayout(new FlowLayout(FlowLayout.LEFT, 24, 6));

        perSecLabel   = statLabel("€/s: 0.00");
        fishLabel     = statLabel("Fish: 0");
        prestigeLabel = statLabel("Prestige: 0  (x1.00)");

        prestigeBtn = new JButton("PRESTIGE  (€1,000,000)");
        stylePrestigeButton();
        prestigeBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    SwingUtilities.getWindowAncestor(this),
                    "<html>Reset everything for a permanent <b>"
                    + String.format("%.0f", GameConstants.PRESTIGE_BONUS_PER_LEVEL * 100)
                    + "% income bonus</b>?<br>Prestige level: "
                    + (state.getPrestigeLevel() + 1) + "</html>",
                    "Prestige Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                PrestigeCommand cmd = new PrestigeCommand(state);
                cmd.execute();
                history.push(cmd);
            }
        });

        add(perSecLabel);
        add(sep());
        add(fishLabel);
        add(sep());
        add(prestigeLabel);
        add(Box.createHorizontalStrut(20));
        add(prestigeBtn);
    }

    private void stylePrestigeButton() {
        prestigeBtn.setBackground(UIColors.ACCENT_DARK);
        prestigeBtn.setForeground(Color.WHITE);
        prestigeBtn.setFocusPainted(false);
        prestigeBtn.setBorderPainted(false);
        prestigeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        prestigeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        prestigeBtn.setEnabled(false);
    }

    private JLabel statLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIColors.FONT_BODY);
        l.setForeground(UIColors.TEXT);
        return l;
    }

    private JSeparator sep() {
        JSeparator s = new JSeparator(SwingConstants.VERTICAL);
        s.setPreferredSize(new Dimension(1, 18));
        s.setForeground(UIColors.BORDER);
        return s;
    }

    private void refresh() {
        double pps = state.getTotalProductionPerSecond();
        perSecLabel.setText(String.format("€/s: %s", formatNum(pps)));
        fishLabel.setText("Fish: " + formatNum(state.getTotalFishCaught()));
        int pl = state.getPrestigeLevel();
        prestigeLabel.setText(String.format("Prestige: %d  (x%.2f)", pl, state.getPrestigeBonus()));

        boolean canPrestige = state.canPrestige();
        prestigeBtn.setEnabled(canPrestige);
        prestigeBtn.setBackground(canPrestige ? UIColors.ACCENT : UIColors.ACCENT_DARK);
    }

    private void subscribeToEvents() {
        EventBus bus = EventBus.getInstance();
        bus.subscribe(EventType.GAME_TICK,         e -> SwingUtilities.invokeLater(this::refresh));
        bus.subscribe(EventType.MONEY_CHANGED,     e -> SwingUtilities.invokeLater(this::refresh));
        bus.subscribe(EventType.PRESTIGE_TRIGGERED,e -> SwingUtilities.invokeLater(this::refresh));
        bus.subscribe(EventType.GAME_LOADED,       e -> SwingUtilities.invokeLater(this::refresh));
    }

    private static String formatNum(double n) {
        if (n >= 1_000_000_000) return String.format("%.2fB", n / 1_000_000_000);
        if (n >= 1_000_000)     return String.format("%.2fM", n / 1_000_000);
        if (n >= 1_000)         return String.format("%.2fK", n / 1_000);
        return String.format("%.2f", n);
    }

    private static String formatNum(long n) {
        return formatNum((double) n);
    }
}
