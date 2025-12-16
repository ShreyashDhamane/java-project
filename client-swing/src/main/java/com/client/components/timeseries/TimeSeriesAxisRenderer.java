package com.client.components.timeseries;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.client.constants.UIFonts;

public class TimeSeriesAxisRenderer {

    public static void drawAxes(
        Graphics2D g2,
        int x0, int y0,
        int chartW, int chartH,
        long max) {

        g2.setColor(new Color(220, 220, 220));
        g2.setStroke(new BasicStroke(1.4f));

        g2.drawLine(x0, y0 - chartH, x0, y0);
        g2.drawLine(x0, y0, x0 + chartW, y0);

        int ticks = 5;

        for (int i = 0; i <= ticks; i++) {
            double ratio = i / (double) ticks;
            int y = y0 - (int) (ratio * chartH);
            long val = (long) (ratio * max);

            g2.setColor(new Color(255, 255, 255, 40));
            g2.drawLine(x0, y, x0 + chartW, y);

            g2.setColor(Color.WHITE);
            g2.setFont(UIFonts.TEXT);

            FontMetrics fm = g2.getFontMetrics();
            String label = String.valueOf(val);

            g2.drawString(label, x0 - fm.stringWidth(label) - 10, y + fm.getAscent()/2);
        }
    }
}
