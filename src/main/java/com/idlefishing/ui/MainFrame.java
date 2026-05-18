package com.idlefishing.ui;

import com.idlefishing.command.CommandHistory;
import com.idlefishing.model.GameState;
import com.idlefishing.observer.EventBus;
import com.idlefishing.observer.EventType;
import com.idlefishing.persistence.SaveManager;
import com.idlefishing.util.GameConstants;
import com.idlefishing.util.UIColors;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Application window — Facade for the UI subsystem.
 * Owns the game loop (javax.swing.Timer) and wires all panels together.
 */
public class MainFrame extends JFrame {

    private final GameState      state   = GameState.getInstance();
    private final CommandHistory history = new CommandHistory();
    private final EventBus       bus     = EventBus.getInstance();
    private final SaveManager    saves   = SaveManager.getInstance();

    private JLabel moneyLabel;
    private JLabel perSecLabel;

    public MainFrame() {
        applyGlobalDefaults();
        buildUI();
        startGameLoop();
        registerWindowEvents();
        loadGame();
    }

    // ── Look-and-feel defaults ─────────────────────────────────────────────

    private void applyGlobalDefaults() {
        UIManager.put("OptionPane.background",        UIColors.BG_DARK);
        UIManager.put("Panel.background",             UIColors.BG_DARK);
        UIManager.put("OptionPane.messageForeground", UIColors.TEXT);
        UIManager.put("Button.background",            UIColors.BG_MEDIUM);
        UIManager.put("Button.foreground",            UIColors.TEXT);
        UIManager.put("ScrollBar.thumb",              UIColors.BG_MEDIUM);
        UIManager.put("ScrollBar.track",              UIColors.BG_DARK);
    }

    // ── Window construction ────────────────────────────────────────────────

    private void buildUI() {
        setTitle("Idle Fishing  —  Become a Legend");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1140, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIColors.BG_DARK);

        FishingPanel fishing = new FishingPanel(state, history);
        ShopPanel    shop    = new ShopPanel(state, history);
        StatsPanel   stats   = new StatsPanel(state, history);

        setLayout(new BorderLayout(0, 0));
        add(buildTopBar(fishing, shop), BorderLayout.NORTH);
        add(fishing,                    BorderLayout.CENTER);
        add(shop,                       BorderLayout.EAST);
        add(stats,                      BorderLayout.SOUTH);
    }

    private JPanel buildTopBar(FishingPanel fishing, ShopPanel shop) {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setBackground(UIColors.BG_DARKEST);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIColors.BORDER),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));

        // Left — title
        JLabel title = new JLabel("IDLE FISHING");
        title.setFont(UIColors.FONT_TITLE);
        title.setForeground(UIColors.GOLD);

        // Centre — location bar
        LocationBar locations = new LocationBar(state, history);

        // Right — money + €/s + save button
        moneyLabel = new JLabel("€0.00");
        moneyLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        moneyLabel.setForeground(UIColors.GOLD);

        perSecLabel = new JLabel("0.00/s");
        perSecLabel.setFont(UIColors.FONT_BODY);
        perSecLabel.setForeground(UIColors.TEXT_DIM);

        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(UIColors.BG_MEDIUM);
        saveBtn.setForeground(UIColors.TEXT);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setFont(UIColors.FONT_BODY);
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> {
            saves.save(state);
            JOptionPane.showMessageDialog(this,
                    "Game saved successfully!", "Saved",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        right.add(perSecLabel);
        right.add(moneyLabel);
        right.add(saveBtn);

        bar.add(title,     BorderLayout.WEST);
        bar.add(locations, BorderLayout.CENTER);
        bar.add(right,     BorderLayout.EAST);

        // Subscribe to money changes for the header display
        bus.subscribe(EventType.MONEY_CHANGED, ev ->
                SwingUtilities.invokeLater(() ->
                        moneyLabel.setText(String.format("€%s", formatNum(state.getMoney())))));

        bus.subscribe(EventType.GAME_TICK, ev ->
                SwingUtilities.invokeLater(() ->
                        perSecLabel.setText(formatNum(state.getTotalProductionPerSecond()) + "/s")));

        return bar;
    }

    // ── Game loop ──────────────────────────────────────────────────────────

    private void startGameLoop() {
        // Passive income tick — fires on EDT so Swing updates are safe
        Timer gameTick = new Timer(GameConstants.GAME_TICK_MS, e -> {
            double income = state.getTotalProductionPerSecond();
            if (income > 0) {
                state.addMoney(income);
                bus.publish(EventType.MONEY_CHANGED, state.getMoney());
            }
            bus.publish(EventType.GAME_TICK);
        });
        gameTick.start();

        // Auto-save
        Timer autoSave = new Timer(GameConstants.AUTO_SAVE_MS, e -> saves.save(state));
        autoSave.start();
    }

    // ── Window events ──────────────────────────────────────────────────────

    private void registerWindowEvents() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saves.save(state);
                dispose();
                System.exit(0);
            }
        });
    }

    // ── Load ──────────────────────────────────────────────────────────────

    private void loadGame() {
        boolean loaded = saves.load(state);
        bus.publish(EventType.MONEY_CHANGED, state.getMoney());
        bus.publish(EventType.GAME_TICK);

        if (loaded && saves.getLastOfflineEarnings() > 0) {
            // Show offline earnings dialog after the window is visible
            SwingUtilities.invokeLater(() -> {
                long secs = saves.getLastOfflineSeconds();
                String time = formatTime(secs);
                JOptionPane.showMessageDialog(this,
                        String.format("<html>Welcome back!<br>You were away for <b>%s</b>.<br>"
                                    + "Your fleet earned <b>€%s</b> while you were gone.</html>",
                                time, formatNum(saves.getLastOfflineEarnings())),
                        "Offline Earnings",
                        JOptionPane.INFORMATION_MESSAGE);
            });
        }
    }

    // ── Formatting helpers ─────────────────────────────────────────────────

    private static String formatNum(double n) {
        if (n >= 1_000_000_000) return String.format("%.2fB", n / 1_000_000_000);
        if (n >= 1_000_000)     return String.format("%.2fM", n / 1_000_000);
        if (n >= 1_000)         return String.format("%.2fK", n / 1_000);
        return String.format("%,.2f", n);
    }

    private static String formatTime(long seconds) {
        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;
        if (h > 0)  return String.format("%dh %02dm", h, m);
        if (m > 0)  return String.format("%dm %02ds", m, s);
        return String.format("%ds", s);
    }
}
