package com.client.components.timeseries;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;

import com.client.constants.UIFonts;

public class TimeSeriesLabelRenderer {

    public static void drawXLabels(
        Graphics2D g2,
        List<Map.Entry<String, Long>> sorted,
        int x0, int y0,
        int chartW, int chartH
    ) {

        g2.setFont(UIFonts.TEXT);
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();

        int n = sorted.size();

        for (int i = 0; i < n; i++) {

            var e = sorted.get(i);

            double xRatio = (n == 1) ? 0.5 : (i / (double)(n - 1));
            int x = x0 + (int)(xRatio * chartW);

            String key = e.getKey();
            int tw = fm.stringWidth(key);

            int lx = x - tw/2;
            if (lx < x0) lx = x0;
            if (lx + tw > x0 + chartW) lx = x0 + chartW - tw;

            g2.drawString(key, lx, y0 + fm.getAscent() + 8);
        }
    }
}
