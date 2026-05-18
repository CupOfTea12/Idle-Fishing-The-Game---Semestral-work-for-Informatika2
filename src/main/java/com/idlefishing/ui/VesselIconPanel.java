package com.idlefishing.ui;

import com.idlefishing.util.UIColors;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Fixed-size thumbnail that shows a vessel drawing inside a small sky+water
 * scene.  Drawing is delegated to {@link VesselPainter}.
 */
public class VesselIconPanel extends JPanel {

    public static final int ICON_W = 92;
    public static final int ICON_H = 70;
    private static final int WATERLINE = VesselPainter.ORIGIN_WL;

    private final String vesselName;

    public VesselIconPanel(String vesselName) {
        this.vesselName = vesselName;
        setPreferredSize(new Dimension(ICON_W, ICON_H));
        setOpaque(true);
        setBackground(new Color(13, 20, 38));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        paintSky(g2);
        paintWater(g2);

        // Delegate boat drawing — centred in the icon, scale 1.0, no bob
        VesselPainter.draw(g2, vesselName, ICON_W / 2, WATERLINE, 1.0, 0.0);

        g2.dispose();
    }

    // ── Background helpers ────────────────────────────────────────────────────

    private void paintSky(Graphics2D g2) {
        GradientPaint sky = new GradientPaint(
                0, 0, new Color(10, 15, 40),
                0, WATERLINE, new Color(18, 38, 72));
        g2.setPaint(sky);
        g2.fillRect(0, 0, ICON_W, WATERLINE);

        g2.setColor(new Color(255, 255, 255, 90));
        long seed = 12345L;
        for (int i = 0; i < 12; i++) {
            seed = seed * 1664525L + 1013904223L;
            int sx = (int) Math.abs(seed % ICON_W);
            seed = seed * 1664525L + 1013904223L;
            int sy = (int) Math.abs(seed % (WATERLINE - 10));
            g2.fillOval(sx, sy, 1, 1);
        }

        g2.setColor(new Color(255, 248, 210, 210));
        g2.fillOval(ICON_W - 18, 4, 12, 12);
        g2.setColor(new Color(13, 20, 38));
        g2.fillOval(ICON_W - 15, 4, 12, 12);
    }

    private void paintWater(Graphics2D g2) {
        GradientPaint water = new GradientPaint(
                0, WATERLINE, UIColors.WATER_MID,
                0, ICON_H, UIColors.WATER_DEEP);
        g2.setPaint(water);
        g2.fillRect(0, WATERLINE, ICON_W, ICON_H - WATERLINE);

        g2.setColor(new Color(255, 255, 255, 50));
        g2.setStroke(new BasicStroke(0.9f));
        for (int x = 0; x < ICON_W; x += 6) {
            double dy = Math.sin(x * 0.28) * 1.4;
            g2.drawLine(x, WATERLINE + (int) dy, x + 3, WATERLINE + (int) dy);
        }
    }
}
