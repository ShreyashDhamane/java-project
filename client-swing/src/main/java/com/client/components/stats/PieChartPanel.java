package com.client.components.stats;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.client.constants.StatsScreenConstants;
import com.client.constants.UIFonts;

public class PieChartPanel extends JPanel {

    public static final Color[] SLICE_COLORS = {
        new Color(0xFF6B6B),
        new Color(0xFFD93D),
        new Color(0xFFF7B32B),
        new Color(0xA3DE83),
        new Color(0x28C2FF),
        new Color(0x8E94F2),
        new Color(0xFF9CEE),
        new Color(0xFFB86C)
    };
    private List<PieSlice> slices = new ArrayList<>();
    private String centerLabel = "";

    public PieChartPanel() {
        setOpaque(false);
    }

    public void setData(List<PieSlice> slices, String centerLabel) {
        this.slices = slices != null ? slices : new ArrayList<>();
        this.centerLabel = centerLabel != null ? centerLabel : "";
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (slices == null || slices.isEmpty()) {
            g2.setColor(new Color(200, 200, 200));
            g2.setFont(UIFonts.TEXT);
            FontMetrics fm = g2.getFontMetrics();
            String msg = "No data";
            int tx = (w - fm.stringWidth(msg)) / 2;
            int ty = (h + fm.getAscent()) / 2;
            g2.drawString(msg, tx, ty);
            g2.dispose();
            return;
        }

        int diameter = Math.min(w, h) - 160;
        if (diameter <= 0) {
            g2.dispose();
            return;
        }
        int cx = w / 2;
        int cy = h / 2;
        int radius = diameter / 2;

        int x = cx - radius;
        int y = cy - radius;

        // draw slices
        double startAngle = 90.0; // start at top
        for (PieSlice slice : slices) {
            int arcAngle = (int) Math.round(slice.percent * 360.0);
            g2.setColor(slice.color);
            g2.fillArc(x, y, diameter, diameter, (int) startAngle, -arcAngle);
            startAngle -= arcAngle;
        }

        // leader lines + labels
        startAngle = 90.0;
        g2.setStroke(new BasicStroke(1.2f));
        g2.setFont(UIFonts.TEXT);
        FontMetrics fm = g2.getFontMetrics();

        // store previous label Y positions for collision avoidance
        List<Integer> usedLabelYs = new ArrayList<>();

        for (PieSlice slice : slices) {
            if (slice.percent <= 0.01) {
                startAngle -= slice.percent * 360.0;
                continue;
            }

            double angle = startAngle - slice.percent * 360.0 / 2.0;
            double rad = Math.toRadians(angle);

            double sin = Math.sin(rad);
            double cos = Math.cos(rad);
            int sx = cx + (int) (radius * cos);
            int sy = cy - (int) (radius * sin);

            int ex = cx + (int) ((radius + 20) * cos);
            int ey = cy - (int) ((radius + 20) * sin);

            // initial label point
            int labelX;
            int labelY = ey;

            boolean rightSide = cos >= 0;
            labelX = rightSide ? ex + 10 : ex - 80;

            // collision detection
            int labelHeight = fm.getHeight() + 2;
            boolean shifted = true;

            while (shifted) {
                shifted = false;
                for (int usedY : usedLabelYs) {
                    if (Math.abs(labelY - usedY) < labelHeight) {
                        labelY += labelHeight; // push label down
                        shifted = true;
                        break;
                    }
                }
            }

            usedLabelYs.add(labelY);

            g2.setColor(new Color(230, 230, 230));
            g2.drawLine(sx, sy, ex, ey);

            String txt = slice.category + " " +
                    StatsScreenConstants.PERCENT_FMT.format(slice.percent * 100.0);

            g2.drawString(txt, labelX, labelY);

            startAngle -= slice.percent * 360.0;
        }


        // center label (total)
        g2.setFont(UIFonts.TEXT_BOLD);
        g2.setColor(Color.WHITE);
        FontMetrics fm2 = g2.getFontMetrics();
        int tx = (w - fm2.stringWidth(centerLabel)) / 2;
        int ty = cy + fm2.getAscent() / 2;
        g2.drawString(centerLabel, tx, ty);

        g2.dispose();
    }
}