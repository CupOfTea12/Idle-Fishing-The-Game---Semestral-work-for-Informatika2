package com.idlefishing.model.fish;

import com.idlefishing.util.UIColors;
import java.awt.Color;

/** Everyday fish – plentiful, low value. */
public class CommonFish extends Fish {

    public CommonFish(String name, double baseValue, double weight) {
        super(name, baseValue, weight);
    }

    @Override
    public String getRarity() { return "Common"; }

    @Override
    public Color getDisplayColor() { return UIColors.RARITY_COMMON; }

    @Override
    public String getCatchMessage() {
        return "You caught a " + getName() + "!";
    }
}
