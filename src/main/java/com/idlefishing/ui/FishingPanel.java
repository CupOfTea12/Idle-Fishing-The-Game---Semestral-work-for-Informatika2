package com.idlefishing.ui;

import com.idlefishing.command.CommandHistory;
import com.idlefishing.command.FishCommand;
import com.idlefishing.model.GameState;
import com.idlefishing.model.fish.Fish;
import com.idlefishing.model.vessel.Vessel;
import com.idlefishing.observer.EventBus;
import com.idlefishing.observer.EventType;
import com.idlefishing.util.UIColors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Left panel — the main interactive fishing area.
 *
 * <ul>
 *   <li>Animated night-sky + ocean scene.</li>
 *   <li>All owned vessels are drawn on the water, bobbing with the waves.</li>
 *   <li>Clicking anywhere (or the button) catches a fish and spawns a
 *       floating value label.</li>
 *   <li>Recent-catches feed at the bottom.</li>
 * </ul>
 */
public class FishingPanel extends JPanel {

    // ── Floating catch-value particle ──────────────────────────────────────

    private static class FloatLabel {
        String text;
        Color  color;
        float  x, y;
        float  alpha = 1.0f;

        FloatLabel(String text, Color color, float x, float y) {
            this.text = text; this.color = color; this.x = x; this.y = y;
        }
        void tick()       { y -= 1.8f; alpha -= 0.018f; }
        boolean isDead()  { return alpha <= 0; }
    }

    // ── Vessel slot descriptor ─────────────────────────────────────────────
    //
    // Defines where a specific boat instance is displayed on the ocean.
    // xFrac is 0..1 across the scene width; phase shifts the bob cycle.

    private static class VesselSlot {
        final String name;
        final double xFrac;
        final double scale;
        final double phase;  // radians — offsets the bob so boats don't sync

        VesselSlot(String name, double xFrac, double scale, double phase) {
            this.name = name; this.xFrac = xFrac;
            this.scale = scale; this.phase = phase;
        }
    }

    // Slot layout — ordered left-to-right.
    // x fractions deliberately avoid ~0.45-0.58 where the player's rod sits.
    //
    // Indices 0-2: rowboats (small, near the rod's sides)
    // Indices 3-4: fishing boats (medium, flanking)
    // Indices 5-6: trawlers (large, wide flanks)
    // Indices 7-8: factory ships (massive, outer edges)
    //
    private static final VesselSlot[] ALL_SLOTS = {
        new VesselSlot("Rowboat",      0.30, 0.52, 0.0),
        new VesselSlot("Rowboat",      0.62, 0.52, 1.1),
        new VesselSlot("Rowboat",      0.20, 0.48, 2.3),
        new VesselSlot("Fishing Boat", 0.14, 0.64, 0.5),
        new VesselSlot("Fishing Boat", 0.72, 0.64, 1.8),
        new VesselSlot("Trawler",      0.07, 0.78, 0.9),
        new VesselSlot("Trawler",      0.80, 0.78, 2.0),
        new VesselSlot("Factory Ship", 0.04, 0.95, 1.4),
        new VesselSlot("Factory Ship", 0.85, 0.95, 0.3),
    };

    // ── Fields ────────────────────────────────────────────────────────────

    private final GameState      state;
    private final CommandHistory history;
    private final FishCommand    fishCmd;

    private final List<FloatLabel> floatLabels   = new ArrayList<>();
    private final Deque<String>    recentCatches = new ArrayDeque<>();
    private static final int       RECENT_MAX    = 6;

    private float waveOffset = 0f;   // drives wave animation + boat bob

    private JLabel  flavourLabel;
    private JPanel  recentPanel;
    private JButton clickButton;
    private boolean isFlashing = false;

    // ── Construction ──────────────────────────────────────────────────────

    public FishingPanel(GameState state, CommandHistory history) {
        this.state   = state;
        this.history = history;
        this.fishCmd = new FishCommand(state);
        initUI();
        startAnimationTimer();
        subscribeToEvents();
    }

    // ── UI build ──────────────────────────────────────────────────────────

    private void initUI() {
        setBackground(UIColors.BG_DARK);
        setLayout(new BorderLayout(0, 4));
        setBorder(new EmptyBorder(8, 8, 8, 8));

        flavourLabel = new JLabel(state.getCurrentSpot().getEnvironmentFlavour(),
                                  SwingConstants.CENTER);
        flavourLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        flavourLabel.setForeground(UIColors.TEXT_DIM);
        flavourLabel.setBorder(new EmptyBorder(4, 0, 4, 0));

        ScenePanel scene = new ScenePanel();

        clickButton = new JButton("CLICK TO FISH");
        clickButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        clickButton.setForeground(UIColors.GOLD);
        clickButton.setBackground(new Color(10, 50, 100, 180));
        clickButton.setBorderPainted(false);
        clickButton.setFocusPainted(false);
        clickButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clickButton.setOpaque(false);
        clickButton.addActionListener(e ->
                handleClick(scene, scene.getWidth() / 2, scene.getHeight() / 2));

        scene.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy  = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        scene.add(clickButton, gbc);

        recentPanel = new JPanel();
        recentPanel.setBackground(UIColors.BG_PANEL);
        recentPanel.setLayout(new BoxLayout(recentPanel, BoxLayout.Y_AXIS));
        recentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIColors.BORDER),
                new EmptyBorder(4, 8, 4, 8)));
        JLabel recentTitle = new JLabel("Recent Catches");
        recentTitle.setFont(UIColors.FONT_SMALL);
        recentTitle.setForeground(UIColors.TEXT_DIM);
        recentPanel.add(recentTitle);

        add(flavourLabel, BorderLayout.NORTH);
        add(scene,        BorderLayout.CENTER);
        add(recentPanel,  BorderLayout.SOUTH);
    }

    // ── Click handling ─────────────────────────────────────────────────────

    private void handleClick(JPanel scene, int cx, int cy) {
        if (!fishCmd.execute()) return;
        history.push(fishCmd);

        Fish caught = fishCmd.getLastCatch();
        double val  = fishCmd.getLastValue();

        floatLabels.add(new FloatLabel(
                String.format("+€%.2f  %s", val, caught.getName()),
                caught.getDisplayColor(),
                (float) (cx - 60 + Math.random() * 40),
                (float) (cy - 20)));

        flashClickButton(caught.getDisplayColor());
        scene.repaint();
    }

    private void flashClickButton(Color c) {
        if (isFlashing) return;
        isFlashing = true;
        Color orig = new Color(10, 50, 100, 180);
        clickButton.setBackground(c);
        Timer t = new Timer(150, e -> { clickButton.setBackground(orig); isFlashing = false; });
        t.setRepeats(false);
        t.start();
    }

    // ── Animation timer ────────────────────────────────────────────────────

    private void startAnimationTimer() {
        new Timer(40, e -> {
            waveOffset = (waveOffset + 1.2f) % 360f;
            floatLabels.forEach(FloatLabel::tick);
            floatLabels.removeIf(FloatLabel::isDead);
            repaint();
        }).start();
    }

    // ── Event subscriptions ────────────────────────────────────────────────

    private void subscribeToEvents() {
        EventBus bus = EventBus.getInstance();

        bus.subscribe(EventType.FISH_CAUGHT, event -> SwingUtilities.invokeLater(() -> {
            FishCommand cmd = (FishCommand) event.getData();
            Fish fish = cmd.getLastCatch();
            recentCatches.addFirst(String.format("• %s [%s]  +€%.2f",
                    fish.getName(), fish.getRarity(), cmd.getLastValue()));
            if (recentCatches.size() > RECENT_MAX) recentCatches.pollLast();
            rebuildRecentList();
        }));

        bus.subscribe(EventType.LOCATION_CHANGED, event -> SwingUtilities.invokeLater(() ->
                flavourLabel.setText(state.getCurrentSpot().getEnvironmentFlavour())));

        bus.subscribe(EventType.PRESTIGE_TRIGGERED, event -> SwingUtilities.invokeLater(() -> {
            recentCatches.clear();
            rebuildRecentList();
            flavourLabel.setText(state.getCurrentSpot().getEnvironmentFlavour());
        }));
    }

    private void rebuildRecentList() {
        while (recentPanel.getComponentCount() > 1)
            recentPanel.remove(recentPanel.getComponentCount() - 1);
        for (String entry : recentCatches) {
            JLabel lbl = new JLabel(entry);
            lbl.setFont(UIColors.FONT_SMALL);
            lbl.setForeground(UIColors.TEXT);
            recentPanel.add(lbl);
        }
        recentPanel.revalidate();
        recentPanel.repaint();
    }

    // ── Vessel-slot helpers ────────────────────────────────────────────────

    /**
     * Builds the list of slots that should be rendered this frame.
     * Iterates through {@link #ALL_SLOTS} and emits a slot for each owned
     * vessel instance (up to the per-type cap encoded in ALL_SLOTS).
     */
    private List<VesselSlot> buildActiveSlots() {
        List<VesselSlot> active = new ArrayList<>();
        int[] counts = vesselCountsByType();   // [rowboat, fishingBoat, trawler, factoryShip]
        String[] names = {"Rowboat", "Fishing Boat", "Trawler", "Factory Ship"};
        int[] caps = {3, 2, 2, 2};            // max visible per type

        // Track how many slots of each type we have already added
        int[] added = new int[4];

        for (VesselSlot slot : ALL_SLOTS) {
            for (int t = 0; t < names.length; t++) {
                if (slot.name.equals(names[t])
                        && added[t] < Math.min(counts[t], caps[t])) {
                    active.add(slot);
                    added[t]++;
                    break;
                }
            }
        }
        return active;
    }

    private int[] vesselCountsByType() {
        int[] counts = new int[4];
        String[] names = {"Rowboat", "Fishing Boat", "Trawler", "Factory Ship"};
        for (Vessel v : state.getVessels()) {
            for (int i = 0; i < names.length; i++) {
                if (v.getName().equals(names[i])) {
                    counts[i] = v.getCount();
                    break;
                }
            }
        }
        return counts;
    }

    // ── Custom scene painting ──────────────────────────────────────────────

    private class ScenePanel extends JPanel {

        ScenePanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(400, 320));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    handleClick(ScenePanel.this, e.getX(), e.getY());
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            final int w      = getWidth();
            final int h      = getHeight();
            final int waterY = h / 2;

            paintSky(g2, w, h, waterY);
            paintWaterBody(g2, w, h, waterY);
            paintWaves(g2, w, waterY);
            paintOwnedVessels(g2, w, waterY);
            paintPlayerRod(g2, w, waterY);
            paintFloatLabels(g2);

            g2.dispose();
        }

        // ── Scene layers ──────────────────────────────────────────────────

        private void paintSky(Graphics2D g2, int w, int h, int waterY) {
            GradientPaint sky = new GradientPaint(
                    0, 0,      new Color(10, 15, 40),
                    0, waterY, new Color(20, 40, 80));
            g2.setPaint(sky);
            g2.fillRect(0, 0, w, h);

            g2.setColor(new Color(255, 255, 255, 80));
            long seed = 42L;
            for (int i = 0; i < 40; i++) {
                seed = seed * 6364136223846793005L + 1442695040888963407L;
                int sx = (int) Math.abs(seed % w);
                seed = seed * 6364136223846793005L + 1442695040888963407L;
                int sy = (int) Math.abs(seed % (waterY - 10));
                g2.fillOval(sx, sy, 2, 2);
            }

            // Moon
            g2.setColor(new Color(255, 245, 200, 200));
            g2.fillOval(w - 60, 12, 30, 30);
            g2.setColor(new Color(10, 15, 40));
            g2.fillOval(w - 53, 12, 30, 30);
        }

        private void paintWaterBody(Graphics2D g2, int w, int h, int waterY) {
            GradientPaint water = new GradientPaint(
                    0, waterY, UIColors.WATER_MID,
                    0, h,      UIColors.WATER_DEEP);
            g2.setPaint(water);
            g2.fillRect(0, waterY, w, h - waterY);

            // Moonlight reflection strip
            GradientPaint reflect = new GradientPaint(
                    w / 2 - 30, waterY, new Color(255, 245, 200, 30),
                    w / 2 + 30, waterY, new Color(255, 245, 200, 0));
            g2.setPaint(reflect);
            g2.fillRect(w / 2 - 30, waterY, 60, h - waterY);
        }

        private void paintWaves(Graphics2D g2, int w, int waterY) {
            g2.setColor(new Color(255, 255, 255, 40));
            g2.setStroke(new BasicStroke(1.5f));
            for (int row = 0; row < 5; row++) {
                int wy = waterY + 5 + row * 16;
                drawWaveLine(g2, w, wy, 28 + row * 4, waveOffset + row * 45);
            }
        }

        private void drawWaveLine(Graphics2D g2, int w, int y, int amp, float offset) {
            GeneralPath path = new GeneralPath();
            path.moveTo(0, y);
            for (int x = 0; x <= w; x += 4) {
                double dy = Math.sin(Math.toRadians(x * 1.2 + offset)) * amp * 0.15;
                path.lineTo(x, y + dy);
            }
            g2.draw(path);
        }

        // ── Vessel rendering ──────────────────────────────────────────────

        /**
         * Draws every active vessel slot on the water with a per-slot bob
         * derived from {@code waveOffset} and the slot's phase.
         * Also paints a soft shadow/reflection under each hull.
         */
        private void paintOwnedVessels(Graphics2D g2, int w, int waterY) {
            List<VesselSlot> active = buildActiveSlots();
            for (VesselSlot slot : active) {
                int cx  = (int) (slot.xFrac * w);
                double bob = Math.sin(Math.toRadians(waveOffset * 1.3 + Math.toDegrees(slot.phase))) * 2.8;

                paintHullReflection(g2, cx, waterY, slot.scale);
                VesselPainter.draw(g2, slot.name, cx, waterY, slot.scale, bob);
            }
        }

        /**
         * Paints a blurred oval "shadow" on the water surface beneath a hull,
         * giving a sense of depth.
         */
        private void paintHullReflection(Graphics2D g2, int cx, int waterY, double scale) {
            int rw = (int) (70 * scale);
            int rh = (int) (6  * scale);
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillOval(cx - rw / 2, waterY, rw, rh);
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillOval(cx - rw / 2 - 4, waterY + 1, rw + 8, rh + 3);
        }

        // ── Player's rod ──────────────────────────────────────────────────

        private void paintPlayerRod(Graphics2D g2, int w, int waterY) {
            int rodBaseX = w / 2 - 10;
            int rodBaseY = waterY - 5;
            int rodTipX  = w / 2 + 60;
            int rodTipY  = waterY - 80;
            int lineEndX = rodTipX + 15;
            int lineEndY = waterY + 12;

            g2.setColor(new Color(150, 120, 80));
            g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(rodBaseX, rodBaseY, rodTipX, rodTipY);

            g2.setColor(new Color(200, 200, 200, 180));
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(rodTipX, rodTipY, lineEndX, lineEndY);

            // Animated bobber
            double bobberBob = Math.sin(Math.toRadians(waveOffset * 2)) * 1.5;
            g2.setColor(new Color(220, 50, 50));
            g2.fillOval(lineEndX - 4, (int) (lineEndY + bobberBob), 8, 5);
            g2.setColor(Color.WHITE);
            g2.fillOval(lineEndX - 4, (int) (lineEndY + bobberBob) + 3, 8, 2);
        }

        // ── Floating labels ───────────────────────────────────────────────

        private void paintFloatLabels(Graphics2D g2) {
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            for (FloatLabel fl : floatLabels) {
                AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fl.alpha);
                g2.setComposite(ac);
                g2.setColor(Color.BLACK);
                g2.drawString(fl.text, (int) fl.x + 1, (int) fl.y + 1);
                g2.setColor(fl.color);
                g2.drawString(fl.text, (int) fl.x, (int) fl.y);
            }
            g2.setComposite(AlphaComposite.SrcOver);
        }
    }
}
