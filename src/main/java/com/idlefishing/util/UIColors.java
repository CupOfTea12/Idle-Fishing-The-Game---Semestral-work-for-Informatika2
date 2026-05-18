package com.idlefishing.util;

import java.awt.Color;
import java.awt.Font;

/** Centralised UI colour palette and font constants for the dark theme. */
public final class UIColors {

    private UIColors() { }

    public static final Color BG_DARKEST  = new Color(13, 17, 30);
    public static final Color BG_DARK     = new Color(22, 28, 45);
    public static final Color BG_MEDIUM   = new Color(30, 40, 65);
    public static final Color BG_PANEL    = new Color(18, 24, 40);
    public static final Color ACCENT      = new Color(233, 69, 96);
    public static final Color ACCENT_DARK = new Color(160, 40, 60);
    public static final Color GOLD        = new Color(255, 215, 0);
    public static final Color GOLD_DARK   = new Color(200, 160, 0);
    public static final Color GREEN       = new Color(72, 199, 116);
    public static final Color GREEN_DARK  = new Color(40, 130, 70);
    public static final Color BLUE        = new Color(80, 160, 255);
    public static final Color PURPLE      = new Color(180, 80, 255);
    public static final Color ORANGE      = new Color(255, 165, 50);
    public static final Color TEXT        = new Color(220, 225, 240);
    public static final Color TEXT_DIM    = new Color(130, 140, 160);
    public static final Color TEXT_BRIGHT = Color.WHITE;
    public static final Color BORDER      = new Color(45, 55, 80);
    public static final Color WATER_DEEP  = new Color(10, 60, 120);
    public static final Color WATER_MID   = new Color(20, 90, 160);
    public static final Color WATER_LIGHT = new Color(40, 130, 200);

    // Rarity colours
    public static final Color RARITY_COMMON    = new Color(160, 210, 160);
    public static final Color RARITY_RARE      = new Color(100, 160, 255);
    public static final Color RARITY_LEGENDARY = new Color(255, 165, 50);
    public static final Color RARITY_EXOTIC    = new Color(200, 80, 255);

    // Fonts
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO    = new Font("Consolas", Font.PLAIN, 12);
    public static final Font FONT_BIG     = new Font("Segoe UI", Font.BOLD, 32);
}
