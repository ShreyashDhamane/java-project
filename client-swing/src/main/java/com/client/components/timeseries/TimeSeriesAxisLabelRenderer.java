package com.client.components.timeseries;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.client.constants.UIFonts;

public class TimeSeriesAxisLabelRenderer {

    public static void drawLabels(
        Graphics2D g2,
        int x0, int y0,
        int chartW, int chartH
    ) {

        // X-axis label
        String xLabel = "Time Period";
        g2.setFont(UIFonts.TEXT_BOLD);
        g2.setColor(Color.WHITE);
        FontMetrics fmX = g2.getFontMetrics();

        int xLabelX = x0 + (chartW - fmX.stringWidth(xLabel)) / 2;
        int xLabelY = y0 + fmX.getAscent() + 28;

        g2.drawString(xLabel, xLabelX, xLabelY);

        // Y-axis label rotated: "Amount"
        String yLabel = "Amount";
        FontMetrics fmY = g2.getFontMetrics();

        g2.rotate(-Math.PI / 2);
        int yLabelX = - (chartH / 2 + fmY.stringWidth(yLabel) / 2 + 55);
        int yLabelY = x0 - 35;
        g2.drawString(yLabel, yLabelX, yLabelY);
        g2.rotate(Math.PI / 2);
    }
}
