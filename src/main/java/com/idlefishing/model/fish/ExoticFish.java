package com.idlefishing.model.fish;

import com.idlefishing.util.UIColors;
import java.awt.Color;

/** Mythical deep-sea creature – only found in the Ocean. */
public class ExoticFish extends Fish {

    public ExoticFish(String name, double baseValue, double weight) {
        super(name, baseValue, weight);
    }

    @Override
    public String getRarity() { return "Exotic"; }

    @Override
    public Color getDisplayColor() { return UIColors.RARITY_EXOTIC; }

    @Override
    public String getCatchMessage() {
        return "*** EXOTIC! ***  You hooked a " + getName() + "!";
    }
}
