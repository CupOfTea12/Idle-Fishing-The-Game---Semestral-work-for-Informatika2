package com.idlefishing.model.fish;

import com.idlefishing.util.UIColors;
import java.awt.Color;

/** Rare trophy fish – triggers a special UI flash when caught. */
public class LegendaryFish extends Fish {

    public LegendaryFish(String name, double baseValue, double weight) {
        super(name, baseValue, weight);
    }

    @Override
    public String getRarity() { return "Legendary"; }

    @Override
    public Color getDisplayColor() { return UIColors.RARITY_LEGENDARY; }

    @Override
    public String getCatchMessage() {
        return "LEGENDARY!  You caught a " + getName() + "!";
    }
}
