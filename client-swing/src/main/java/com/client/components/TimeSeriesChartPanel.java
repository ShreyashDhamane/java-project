package com.client.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import com.client.constants.UIFonts;

// its a time series chart panel for line chart for our expense/income stats
public class TimeSeriesChartPanel extends JPanel {

    private String title = ""; // chart title like expense over time, income over time
    private Map<String, Long> data = new LinkedHashMap<>(); // key is time, and value is amount

    public TimeSeriesChartPanel() {
        // Transparent bg
        setOpaque(false);
    }

    public void setData(Map<String, Long> data, String title) {
        // set data for chart
        this.data = (data != null) ? data : new LinkedHashMap<>();
        this.title = (title != null) ? title : "";
        // repaint to update the chart with new data
        repaint();
    }

    // --------------------------------------------------------
    // Keys needs to be sorted first for proper time series plotting
    // there is issue that there is no ordering between dates, 
    // because user can dadd past dates at any time
    // sorting is required for proper plotting on x-axis
    // --------------------------------------------------------
    // the issue arrises because the localdate stored in mongodb databse is not getting converted properly using the
    // object mapper ew have currently. its the issue with date serialization/deserialization library we are using.
    private List<Map.Entry<String, Long>> getSortedData() {
        List<Map.Entry<String, Long>> list = new ArrayList<>(data.entrySet());

        // Detect numeric values like years (2020, 2021, 2022)
        // we are matching all keys to be numeric
        boolean allNumeric = list.stream().allMatch(e -> e.getKey().matches("\\d+"));

        // if all keys are numeric, sort numerically, easy
        if (allNumeric) {
            list.sort(Comparator.comparingInt(e -> Integer.valueOf(e.getKey())));
            return list;
        }
        // else 
        // Detect months (Jan, Feb, Mar…)
        List<String> months = Arrays.asList("Jan","Feb","Mar","Apr","May","Jun", "Jul","Aug","Sep","Oct","Nov","Dec");
        // check if all keys are months
        boolean allMonths = list.stream().allMatch(e -> months.contains(e.getKey()));
        // if they are months, sort by month order
        if (allMonths) {
            list.sort(Comparator.comparingInt(e -> months.indexOf(e.getKey())));
            return list;
        }

        // else use default sorting

        // Default alphabetical fallback
        list.sort(Comparator.comparing(Map.Entry::getKey));
        return list;
    }

    // standard paint component override to draw custom line chart
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create(); // 
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // smooth edges

        int width = getWidth();
        int height = getHeight();

        // Title style
        g2.setFont(UIFonts.TEXT_BOLD);
        g2.setColor(Color.WHITE);
        g2.drawString(title, 40, 35); // position title

         // Check for no data

        if (data == null || data.isEmpty()) {
            g2.setFont(UIFonts.TEXT);
            g2.drawString("No data", width / 2 - 20, height / 2);
            g2.dispose();
            // No data to display
            return;
        }

        // get sorted data for proper time series plotting
        List<Map.Entry<String, Long>> sorted = getSortedData();

        // padding around chart area
        int paddingLeft = 60;
        int paddingRight = 30;
        int paddingTop = 55;
        int paddingBottom = 50;

        // chart area dimensions
        int chartW = width - paddingLeft - paddingRight;
        int chartH = height - paddingTop - paddingBottom;

        // chart positions
        int x0 = paddingLeft;
        int y0 = height - paddingBottom;

        // Max Y value for scaling the chart and proper scales
        long max = sorted.stream().mapToLong(Map.Entry::getValue).max().orElse(1); // its a short way to get max value from map entries
        if (max < 5) {
            max = 5; // min max to avoid too much compression
        }

        // this is where we draw the axes, grid lines, labels, and the line chart itself
        g2.setColor(new Color(220, 220, 220));
        g2.setStroke(new BasicStroke(1.4f));
        g2.drawLine(x0, paddingTop, x0, y0);

        int yTicks = 5; // number of horizontal grid lines
        for (int i = 0; i <= yTicks; i++) {
            double ratio = i / (double) yTicks;
            int y = y0 - (int) (ratio * chartH);
            long val = (long) (ratio * max);

            g2.setColor(new Color(255, 255, 255, 40));
            g2.drawLine(x0, y, x0 + chartW, y);

            g2.setColor(Color.WHITE);
            g2.setFont(UIFonts.TEXT);

            String label = String.valueOf(val);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, x0 - fm.stringWidth(label) - 10, y + fm.getAscent() / 2);
        }

        // this is the x-axis
        g2.setColor(new Color(220, 220, 220));
        g2.drawLine(x0, y0, x0 + chartW, y0);

        // here we draw the line chart itself
        int n = sorted.size();
        int prevX = -1, prevY = -1;

        g2.setStroke(new BasicStroke(2.4f));
        g2.setColor(new Color(255, 120, 110));

        for (int i = 0; i < n; i++) {

            Map.Entry<String, Long> entry = sorted.get(i);

            double xRatio = (n == 1) ? 0.5 : ((double) i / (n - 1));
            int x = x0 + (int) (xRatio * chartW);
            int y = y0 - (int) ((entry.getValue() * 1.0 / max) * chartH);

            if (prevX != -1) g2.drawLine(prevX, prevY, x, y);

            g2.fillOval(x - 4, y - 4, 8, 8);

            prevX = x;
            prevY = y;
        }

        //  X-axis labels
        g2.setFont(UIFonts.TEXT);
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < n; i++) {

            Map.Entry<String, Long> entry = sorted.get(i);

            double xRatio = (n == 1) ? 0.5 : ((double) i / (n - 1));
            int x = x0 + (int) (xRatio * chartW);

            String key = entry.getKey();
            int textWidth = fm.stringWidth(key);

            // Default centered position
            int labelX = x - textWidth / 2;

            // ---- Clamp within the visible chart range ----
            if (labelX < x0) {
                labelX = x0;
            } else if (labelX + textWidth > x0 + chartW) {
                labelX = x0 + chartW - textWidth;
            }

            g2.drawString(key, labelX, y0 + fm.getAscent() + 8);
        }

        g2.setFont(UIFonts.TEXT_BOLD);
        String xLabel = "Time Period";   // or "Month" or "Year" depending on mode
        FontMetrics fmX = g2.getFontMetrics();
        int xLabelX = x0 + (chartW - fmX.stringWidth(xLabel)) / 2;
        int xLabelY = y0 + fmX.getAscent() + 28;
        g2.drawString(xLabel, xLabelX, xLabelY);

        // Y-axis label (rotated)
        String yLabel = "Amount";
        FontMetrics fmY = g2.getFontMetrics();
        g2.rotate(-Math.PI / 2);
        int yLabelX = - (paddingTop + (chartH + fmY.stringWidth(yLabel)) / 2);
        int yLabelY = paddingLeft - 35;
        g2.drawString(yLabel, yLabelX, yLabelY);
        g2.rotate(Math.PI / 2);

        g2.dispose();
    }
}
