package com.client.components.timeseries;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import com.client.constants.UIFonts;

public class TimeSeriesChartPanel extends JPanel {

    private String title = "";
    private Map<String, Long> data = new LinkedHashMap<>();

    public TimeSeriesChartPanel() {
        setOpaque(false);
    }

    public void setData(Map<String, Long> data, String title) {
        this.data = (data != null) ? data : new LinkedHashMap<>();
        this.title = (title != null) ? title : "";
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setFont(UIFonts.TEXT_BOLD);
        g2.setColor(Color.WHITE);
        g2.drawString(title, 40, 35);

        if (data.isEmpty()) {
            g2.drawString("No data", w/2 - 20, h/2);
            g2.dispose();
            return;
        }

        List<Map.Entry<String, Long>> sorted = TimeSeriesDataSorter.sort(data);

        int padL = 60, padR = 30, padT = 55, padB = 50;
        int chartW = w - padL - padR;
        int chartH = h - padT - padB;

        int x0 = padL;
        int y0 = h - padB;

        long max = sorted.stream().mapToLong(Map.Entry::getValue).max().orElse(1);
        if (max < 5) max = 5;

        TimeSeriesAxisRenderer.drawAxes(g2, x0, y0, chartW, chartH, max);
        TimeSeriesLineRenderer.drawLine(g2, sorted, x0, y0, chartW, chartH, max);
        TimeSeriesLabelRenderer.drawXLabels(g2, sorted, x0, y0, chartW, chartH);

        TimeSeriesAxisLabelRenderer.drawLabels(g2, x0, y0, chartW, chartH);
        g2.dispose();
    }
}
