package com.idlefishing.ui;

import com.idlefishing.command.BuyUpgradeCommand;
import com.idlefishing.command.BuyVesselCommand;
import com.idlefishing.command.CommandHistory;
import com.idlefishing.model.GameState;
import com.idlefishing.model.upgrade.Upgrade;
import com.idlefishing.model.vessel.Vessel;
import com.idlefishing.observer.EventBus;
import com.idlefishing.observer.EventType;
import com.idlefishing.util.UIColors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Right panel — the shop.
 * Two scrollable sections: Vessels and Upgrades.
 * Each buy button is refreshed whenever money changes.
 */
public class ShopPanel extends JPanel {

    private final GameState      state;
    private final CommandHistory history;

    /** Maps vessel → its buy button so we can enable/disable on money changes. */
    private final Map<Vessel, JButton>  vesselButtons  = new LinkedHashMap<>();
    /** Maps upgrade → its buy button. */
    private final Map<Upgrade, JButton> upgradeButtons = new LinkedHashMap<>();

    // ── Construction ───────────────────────────────────────────────────────

    public ShopPanel(GameState state, CommandHistory history) {
        this.state   = state;
        this.history = history;
        initUI();
        subscribeToEvents();
    }

    // ── UI build ───────────────────────────────────────────────────────────

    private void initUI() {
        setBackground(UIColors.BG_PANEL);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, UIColors.BORDER),
                new EmptyBorder(8, 8, 8, 8)));
        setLayout(new BorderLayout(0, 8));
        setPreferredSize(new Dimension(340, 0));

        JLabel title = new JLabel("SHOP");
        title.setFont(UIColors.FONT_TITLE);
        title.setForeground(UIColors.GOLD);
        title.setBorder(new EmptyBorder(0, 0, 4, 0));

        JScrollPane scroll = new JScrollPane(buildScrollContent());
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(title,  BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildScrollContent() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(sectionLabel("  VESSELS"));
        content.add(Box.createVerticalStrut(4));
        for (Vessel v : state.getVessels()) {
            content.add(buildVesselRow(v));
            content.add(Box.createVerticalStrut(3));
        }

        content.add(Box.createVerticalStrut(12));
        content.add(sectionLabel("  UPGRADES"));
        content.add(Box.createVerticalStrut(4));
        for (Upgrade u : state.getUpgrades()) {
            content.add(buildUpgradeRow(u));
            content.add(Box.createVerticalStrut(3));
        }

        content.add(Box.createVerticalGlue());
        return content;
    }

    // ── Vessel row ─────────────────────────────────────────────────────────

    private JPanel buildVesselRow(Vessel vessel) {
        JPanel row = new JPanel(new BorderLayout(6, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, VesselIconPanel.ICON_H + 6));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIColors.BORDER),
                BorderFactory.createEmptyBorder(2, 0, 2, 0)));

        // ── Boat drawing on the left ──────────────────────────────────────────
        VesselIconPanel icon = new VesselIconPanel(vessel.getName());

        // ── Name + stats in the centre ────────────────────────────────────────
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel nameLbl = new JLabel(vessel.getName());
        nameLbl.setFont(UIColors.FONT_HEADING);
        nameLbl.setForeground(UIColors.TEXT_BRIGHT);

        JLabel statsLbl = vesselStatsLabel(vessel);

        info.add(nameLbl);
        info.add(statsLbl);

        // ── Buy button on the right ───────────────────────────────────────────
        JButton buyBtn = new JButton(formatCost(vessel.getCurrentCost()));
        styleBuyButton(buyBtn, UIColors.GREEN_DARK);
        buyBtn.addActionListener(e -> {
            BuyVesselCommand cmd = new BuyVesselCommand(state, vessel);
            if (cmd.execute()) {
                history.push(cmd);
                buyBtn.setText(formatCost(vessel.getCurrentCost()));
                statsLbl.setText(vesselStatsText(vessel));
                refreshAffordability();
            }
        });

        vesselButtons.put(vessel, buyBtn);

        row.add(icon,   BorderLayout.WEST);
        row.add(info,   BorderLayout.CENTER);
        row.add(buyBtn, BorderLayout.EAST);
        return row;
    }

    private JLabel vesselStatsLabel(Vessel vessel) {
        JLabel l = new JLabel(vesselStatsText(vessel));
        l.setFont(UIColors.FONT_SMALL);
        l.setForeground(UIColors.TEXT_DIM);
        return l;
    }

    private String vesselStatsText(Vessel vessel) {
        return String.format("x%d  |  €%.1f/s", vessel.getCount(), vessel.produce());
    }

    // ── Upgrade row ────────────────────────────────────────────────────────

    private JPanel buildUpgradeRow(Upgrade upgrade) {
        JPanel row = new JPanel(new BorderLayout(6, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel nameLbl   = new JLabel(upgrade.getName());
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLbl.setForeground(upgrade.isPurchased() ? UIColors.TEXT_DIM : UIColors.TEXT);

        JLabel effectLbl = new JLabel(upgrade.getEffectDescription());
        effectLbl.setFont(UIColors.FONT_SMALL);
        effectLbl.setForeground(upgrade.isPurchased() ? UIColors.TEXT_DIM : UIColors.BLUE);

        info.add(nameLbl);
        info.add(effectLbl);

        JButton buyBtn;
        if (upgrade.isPurchased()) {
            buyBtn = new JButton("Owned");
            buyBtn.setEnabled(false);
            buyBtn.setBackground(UIColors.BG_MEDIUM);
            buyBtn.setForeground(UIColors.TEXT_DIM);
            buyBtn.setFont(UIColors.FONT_SMALL);
            buyBtn.setFocusPainted(false);
            buyBtn.setBorderPainted(false);
        } else {
            buyBtn = new JButton(formatCost(upgrade.getCost()));
            styleBuyButton(buyBtn, new Color(60, 80, 140));
            buyBtn.addActionListener(e -> {
                BuyUpgradeCommand cmd = new BuyUpgradeCommand(state, upgrade);
                if (cmd.execute()) {
                    history.push(cmd);
                    buyBtn.setText("Owned");
                    buyBtn.setEnabled(false);
                    buyBtn.setBackground(UIColors.BG_MEDIUM);
                    nameLbl.setForeground(UIColors.TEXT_DIM);
                    effectLbl.setForeground(UIColors.TEXT_DIM);
                    refreshAffordability();
                }
            });
        }

        upgradeButtons.put(upgrade, buyBtn);

        row.add(info,   BorderLayout.CENTER);
        row.add(buyBtn, BorderLayout.EAST);
        return row;
    }

    // ── Affordability refresh ──────────────────────────────────────────────

    private void refreshAffordability() {
        double money = state.getMoney();

        vesselButtons.forEach((vessel, btn) -> {
            if (btn.isEnabled()) {
                boolean affordable = money >= vessel.getCurrentCost();
                btn.setBackground(affordable ? UIColors.GREEN : UIColors.GREEN_DARK);
                btn.setText(formatCost(vessel.getCurrentCost()));
            }
        });

        upgradeButtons.forEach((upgrade, btn) -> {
            if (!upgrade.isPurchased()) {
                boolean affordable = money >= upgrade.getCost();
                btn.setBackground(affordable ? new Color(80, 110, 200) : new Color(60, 80, 140));
            }
        });
    }

    private void subscribeToEvents() {
        EventBus bus = EventBus.getInstance();
        bus.subscribe(EventType.MONEY_CHANGED,     e -> SwingUtilities.invokeLater(this::refreshAffordability));
        bus.subscribe(EventType.GAME_TICK,         e -> SwingUtilities.invokeLater(this::refreshAffordability));
        bus.subscribe(EventType.PRESTIGE_TRIGGERED,e -> SwingUtilities.invokeLater(this::rebuildShop));
        bus.subscribe(EventType.GAME_LOADED,       e -> SwingUtilities.invokeLater(this::rebuildShop));
    }

    private void rebuildShop() {
        removeAll();
        vesselButtons.clear();
        upgradeButtons.clear();
        initUI();
        revalidate();
        repaint();
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private static void styleBuyButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(UIColors.FONT_SMALL);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(80, 38));
    }

    private static JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIColors.FONT_HEADING);
        l.setForeground(UIColors.GOLD);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIColors.BORDER));
        return l;
    }

    private static String formatCost(double cost) {
        if (cost >= 1_000_000_000) return String.format("€%.1fB", cost / 1_000_000_000);
        if (cost >= 1_000_000)     return String.format("€%.1fM", cost / 1_000_000);
        if (cost >= 1_000)         return String.format("€%.1fK", cost / 1_000);
        return String.format("€%.0f", cost);
    }
}
