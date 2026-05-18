package com.idlefishing.model.fish;

import com.idlefishing.util.UIColors;
import java.awt.Color;

/** Uncommon catch – noticeably more valuable than common fish. */
public class RareFish extends Fish {

    public RareFish(String name, double baseValue, double weight) {
        super(name, baseValue, weight);
    }

    @Override
    public String getRarity() { return "Rare"; }

    @Override
    public Color getDisplayColor() { return UIColors.RARITY_RARE; }

    @Override
    public String getCatchMessage() {
        return "Nice catch! A rare " + getName() + "!";
    }
}
