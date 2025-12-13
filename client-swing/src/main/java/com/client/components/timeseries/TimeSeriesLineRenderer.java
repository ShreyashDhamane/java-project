package com.client.components.timeseries;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;

public class TimeSeriesLineRenderer {

    public static void drawLine(
        Graphics2D g2,
        List<Map.Entry<String, Long>> sorted,
        int x0, int y0,
        int chartW, int chartH,
        long max
    ) {
        g2.setStroke(new BasicStroke(2.4f));
        g2.setColor(new Color(255, 120, 110));

        int n = sorted.size();
        int prevX = -1, prevY = -1;

        for (int i = 0; i < n; i++) {

            var e = sorted.get(i);

            double xRatio = (n == 1) ? 0.5 : (i / (double)(n - 1));
            int x = x0 + (int)(xRatio * chartW);
            int y = y0 - (int)((e.getValue() * 1.0 / max) * chartH);

            if (prevX != -1) g2.drawLine(prevX, prevY, x, y);

            g2.fillOval(x - 4, y - 4, 8, 8);

            prevX = x;
            prevY = y;
        }
    }
}
