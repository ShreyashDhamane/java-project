package com.client.components.stats;

import java.awt.Color;

public class PieSlice {
    public final String category;
    public final String emoji;
    public final long amount;
    public final double percent;
    public final Color color;

    public PieSlice(String category, String emoji, long amount, double percent, Color color) {
        this.category = category;
        this.emoji = emoji;
        this.amount = amount;
        this.percent = percent;
        this.color = color;
    }
}
