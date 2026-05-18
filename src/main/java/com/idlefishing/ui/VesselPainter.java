package com.idlefishing.ui;

import java.awt.*;
import java.awt.geom.*;

/**
 * Static utility that draws every vessel type using Java2D.
 *
 * <p>All drawing methods work in an internal coordinate space where the
 * hull's bottom centre sits at <b>(46, 52)</b> on a 92 × 70 canvas.
 * Callers use {@link #draw} which applies a translate+scale transform so the
 * boat appears at the requested scene position and scale without duplicating
 * coordinate maths.
 *
 * <p>Both {@link VesselIconPanel} (shop thumbnails) and
 * {@link FishingPanel} (live ocean scene) delegate here.
 */
public final class VesselPainter {

    /** X of the hull's horizontal centre in the internal 92-wide canvas. */
    static final int ORIGIN_CX = 46;

    /** Y of the waterline in the internal 70-tall canvas. */
    static final int ORIGIN_WL = 52;

    private VesselPainter() { }

    // ── Public entry point ────────────────────────────────────────────────────

    /**
     * Draws a vessel so its hull's bottom-centre lands at
     * ({@code cx}, {@code waterlineY + bobY}).
     *
     * @param g2          target graphics context (not modified; a copy is used)
     * @param vesselName  "Rowboat", "Fishing Boat", "Trawler", or "Factory Ship"
     * @param cx          scene X of the hull centre
     * @param waterlineY  scene Y of the water surface
     * @param scale       uniform scale (1.0 = natural shop-icon size)
     * @param bobY        vertical bob offset from wave animation (pixels, pre-scale)
     */
    public static void draw(Graphics2D g2, String vesselName,
                            int cx, int waterlineY, double scale, double bobY) {
        Graphics2D g = (Graphics2D) g2.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        // Position so (ORIGIN_CX, ORIGIN_WL) maps to (cx, waterlineY+bobY)
        g.translate(cx - ORIGIN_CX * scale, waterlineY - ORIGIN_WL * scale + bobY);
        g.scale(scale, scale);

        switch (vesselName) {
            case "Rowboat":      drawRowboat(g);     break;
            case "Fishing Boat": drawFishingBoat(g); break;
            case "Trawler":      drawTrawler(g);     break;
            case "Factory Ship": drawFactoryShip(g); break;
            default:             drawRowboat(g);     break;
        }

        g.dispose();
    }

    // ── Rowboat ───────────────────────────────────────────────────────────────

    static void drawRowboat(Graphics2D g2) {
        final int WL = ORIGIN_WL;

        // Hull
        int[] hx = {14, 78, 70, 22};
        int[] hy = {WL - 11, WL - 11, WL, WL};
        g2.setColor(new Color(90, 55, 20));
        g2.fillPolygon(hx, hy, 4);

        // Inner wood
        int[] ix = {17, 75, 68, 24};
        int[] iy = {WL - 11, WL - 11, WL - 2, WL - 2};
        g2.setColor(new Color(130, 85, 35));
        g2.fillPolygon(ix, iy, 4);

        // Planks
        g2.setColor(new Color(110, 70, 28, 150));
        g2.setStroke(new BasicStroke(0.7f));
        for (int y = WL - 9; y < WL - 2; y += 3) g2.drawLine(18, y, 74, y);

        // Bilge water reflection
        int[] wx = {20, 72, 67, 25};
        int[] wy = {WL - 4, WL - 4, WL - 2, WL - 2};
        g2.setColor(new Color(40, 100, 160, 80));
        g2.fillPolygon(wx, wy, 4);

        // Thwart
        g2.setColor(new Color(170, 120, 55));
        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(34, WL - 11, 58, WL - 11);

        // Oars
        g2.setColor(new Color(110, 70, 28));
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(28, WL - 11, 5,  WL + 3);
        g2.setStroke(new BasicStroke(5f,   BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(4,  WL,      4,  WL + 7);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(64, WL - 11, 87, WL + 3);
        g2.setStroke(new BasicStroke(5f,   BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(87, WL,      87, WL + 7);

        // Fisherman
        g2.setColor(new Color(40, 35, 30));
        g2.fillOval(42, WL - 24, 9, 9);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(46, WL - 15, 46, WL - 11);
        g2.drawLine(40, WL - 13, 53, WL - 13);

        // Fishing rod
        g2.setColor(new Color(100, 70, 35));
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(52, WL - 14, 70, WL - 28);
        g2.setColor(new Color(220, 220, 200, 160));
        g2.setStroke(new BasicStroke(0.8f));
        g2.drawLine(70, WL - 28, 76, WL - 7);
        g2.setColor(new Color(220, 50, 50));
        g2.fillOval(74, WL - 8, 3, 2);

        // Outline
        g2.setColor(new Color(60, 35, 10));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawPolygon(hx, hy, 4);
    }

    // ── Fishing Boat ──────────────────────────────────────────────────────────

    static void drawFishingBoat(Graphics2D g2) {
        final int WL = ORIGIN_WL;

        // Hull
        int[] hx = {5, 87, 79, 13};
        int[] hy = {WL - 13, WL - 13, WL, WL};
        g2.setColor(new Color(28, 72, 155));
        g2.fillPolygon(hx, hy, 4);

        // White stripe
        int[] sx = {5, 87, 84, 8};
        int[] sy = {WL - 13, WL - 13, WL - 10, WL - 10};
        g2.setColor(Color.WHITE);
        g2.fillPolygon(sx, sy, 4);

        // Deck
        g2.setColor(new Color(180, 160, 110));
        g2.fillRect(8, WL - 13, 76, 4);

        // Cabin
        g2.setColor(new Color(230, 225, 210));
        g2.fillRoundRect(20, WL - 27, 36, 15, 4, 4);
        g2.setColor(new Color(190, 185, 170));
        g2.fillRoundRect(50, WL - 27, 6, 15, 2, 2);

        // Portholes
        g2.setColor(new Color(110, 195, 230));
        g2.fillOval(24, WL - 24, 8, 7);
        g2.fillOval(36, WL - 24, 8, 7);
        g2.setColor(new Color(140, 130, 110));
        g2.setStroke(new BasicStroke(1f));
        g2.drawOval(24, WL - 24, 8, 7);
        g2.drawOval(36, WL - 24, 8, 7);

        // Mast
        g2.setColor(new Color(90, 65, 35));
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(62, WL - 46, 62, WL - 13);

        // Rigging
        g2.setColor(new Color(170, 150, 120, 160));
        g2.setStroke(new BasicStroke(0.8f));
        g2.drawLine(62, WL - 46, 14, WL - 13);
        g2.drawLine(62, WL - 46, 84, WL - 13);

        // Boom
        g2.setColor(new Color(90, 65, 35));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(62, WL - 38, 80, WL - 26);

        // Fishing line from boom
        g2.setColor(new Color(210, 210, 200, 180));
        g2.setStroke(new BasicStroke(0.7f));
        g2.drawLine(80, WL - 26, 84, WL - 7);

        // Flag
        int[] fx = {62, 62, 74};
        int[] fy = {WL - 46, WL - 36, WL - 41};
        g2.setColor(new Color(210, 45, 45));
        g2.fillPolygon(fx, fy, 3);

        // Exhaust
        g2.setColor(new Color(60, 60, 65));
        g2.fillRect(16, WL - 26, 6, 13);
        g2.fillOval(14, WL - 28, 10, 4);
        g2.setColor(new Color(130, 130, 130, 100));
        g2.fillOval(15, WL - 34, 8, 7);
        g2.fillOval(17, WL - 40, 6, 6);

        // Outline
        g2.setColor(new Color(15, 45, 100));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawPolygon(hx, hy, 4);
    }

    // ── Trawler ───────────────────────────────────────────────────────────────

    static void drawTrawler(Graphics2D g2) {
        final int WL = ORIGIN_WL;

        // Hull
        int[] hx = {1, 91, 83, 9};
        int[] hy = {WL - 16, WL - 16, WL, WL};
        g2.setColor(new Color(58, 58, 68));
        g2.fillPolygon(hx, hy, 4);

        // Anti-fouling
        int[] bx = {9, 83, 80, 12};
        int[] by = {WL - 5, WL - 5, WL, WL};
        g2.setColor(new Color(145, 42, 28));
        g2.fillPolygon(bx, by, 4);

        // Deck
        g2.setColor(new Color(78, 78, 90));
        g2.fillRect(3, WL - 16, 86, 5);

        // Bow structure
        g2.setColor(new Color(95, 95, 108));
        g2.fillRect(4, WL - 29, 24, 13);
        g2.setColor(new Color(130, 130, 145));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(4, WL - 29, 28, WL - 29);

        // Bridge
        g2.setColor(new Color(200, 198, 188));
        g2.fillRoundRect(30, WL - 38, 34, 22, 3, 3);
        g2.setColor(new Color(178, 175, 162));
        g2.fillRoundRect(33, WL - 47, 26, 10, 3, 3);
        g2.setColor(new Color(95, 195, 235));
        g2.fillRect(34, WL - 35, 8, 6);
        g2.fillRect(45, WL - 35, 8, 6);
        g2.fillRect(56, WL - 35, 5, 6);
        g2.fillRect(36, WL - 44, 6, 4);
        g2.fillRect(46, WL - 44, 6, 4);

        // Radar mast
        g2.setColor(new Color(130, 125, 115));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(46, WL - 47, 46, WL - 54);
        g2.drawLine(43, WL - 52, 49, WL - 52);
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawArc(43, WL - 55, 8, 5, 0, 180);

        // Net boom
        g2.setColor(new Color(148, 118, 55));
        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(68, WL - 30, 88, WL - 16);

        // Net lines
        g2.setColor(new Color(190, 165, 100, 160));
        g2.setStroke(new BasicStroke(0.9f));
        for (int i = 0; i < 5; i++) g2.drawLine(70 + i * 5, WL - 29 + i * 2, 72 + i * 5, WL - 8);
        g2.drawLine(70, WL - 22, 88, WL - 18);
        g2.drawLine(71, WL - 15, 87, WL - 12);

        // Stack
        g2.setColor(new Color(55, 55, 60));
        g2.fillRect(66, WL - 45, 10, 18);
        g2.setColor(new Color(220, 110, 30));
        g2.fillRect(65, WL - 48, 12, 4);
        g2.setColor(new Color(55, 55, 60));
        g2.fillOval(63, WL - 50, 14, 5);
        g2.setColor(new Color(140, 140, 140, 110));
        g2.fillOval(68, WL - 57, 9, 8);
        g2.fillOval(72, WL - 62, 7, 7);

        // Outline
        g2.setColor(new Color(28, 28, 36));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawPolygon(hx, hy, 4);
    }

    // ── Factory Ship ──────────────────────────────────────────────────────────

    static void drawFactoryShip(Graphics2D g2) {
        final int WL = ORIGIN_WL;

        // Hull
        int[] hx = {0, 92, 86, 6};
        int[] hy = {WL - 18, WL - 18, WL, WL};
        g2.setColor(new Color(42, 42, 52));
        g2.fillPolygon(hx, hy, 4);

        // Anti-fouling
        int[] bx = {6, 86, 83, 9};
        int[] by = {WL - 6, WL - 6, WL, WL};
        g2.setColor(new Color(130, 30, 20));
        g2.fillPolygon(bx, by, 4);

        // Deck
        g2.setColor(new Color(65, 65, 78));
        g2.fillRect(2, WL - 18, 88, 5);
        g2.setColor(new Color(80, 80, 95, 100));
        for (int dx = 6; dx < 86; dx += 5) g2.fillOval(dx, WL - 16, 2, 2);

        // Factory block
        g2.setColor(new Color(158, 155, 145));
        g2.fillRect(6, WL - 36, 60, 18);
        g2.setColor(new Color(128, 125, 115));
        g2.fillRect(60, WL - 36, 6, 18);
        g2.setColor(new Color(95, 190, 230));
        for (int col = 0; col < 6; col++) {
            g2.fillRect(10 + col * 9, WL - 33, 6, 4);
            g2.fillRect(10 + col * 9, WL - 26, 6, 4);
        }

        // Bridge
        g2.setColor(new Color(195, 192, 180));
        g2.fillRoundRect(68, WL - 46, 20, 28, 3, 3);
        g2.setColor(new Color(175, 172, 160));
        g2.fillRoundRect(70, WL - 54, 16, 9, 3, 3);
        g2.setColor(new Color(95, 200, 240));
        g2.fillRect(71, WL - 43, 5, 4);
        g2.fillRect(79, WL - 43, 5, 4);
        g2.fillRect(71, WL - 36, 5, 4);
        g2.fillRect(79, WL - 36, 5, 4);
        g2.fillRect(72, WL - 51, 4, 3);
        g2.fillRect(80, WL - 51, 4, 3);

        // Smokestacks
        int[] stackX = {14, 28, 42};
        Color[] bands = {new Color(220, 50, 50), new Color(220, 170, 30), new Color(220, 50, 50)};
        for (int si = 0; si < 3; si++) {
            int sx = stackX[si];
            g2.setColor(new Color(45, 45, 52));
            g2.fillRect(sx, WL - 55, 10, 20);
            g2.setColor(bands[si]);
            g2.fillRect(sx, WL - 57, 10, 4);
            g2.setColor(new Color(35, 35, 42));
            g2.fillOval(sx - 2, WL - 59, 14, 5);
            g2.setColor(new Color(150, 150, 150, 90 + si * 20));
            g2.fillOval(sx + 1, WL - 65, 8, 7);
            g2.fillOval(sx + 3, WL - 70, 6, 6);
        }

        // Crane
        g2.setColor(new Color(210, 170, 40));
        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(3, WL - 18, 3, WL - 38);
        g2.drawLine(3, WL - 38, 18, WL - 26);
        g2.setColor(new Color(190, 190, 190, 180));
        g2.setStroke(new BasicStroke(0.8f));
        g2.drawLine(18, WL - 26, 14, WL - 16);

        // Radar
        g2.setColor(new Color(180, 178, 165));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(78, WL - 54, 78, WL - 62);
        g2.drawLine(74, WL - 60, 82, WL - 60);
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawArc(74, WL - 64, 8, 5, 0, 180);

        // Rub-rail
        g2.setColor(new Color(200, 200, 215));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawLine(6, WL - 18, 86, WL - 18);

        // Outline
        g2.setColor(new Color(18, 18, 25));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawPolygon(hx, hy, 4);
    }
}
