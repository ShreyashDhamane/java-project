package com.client.components;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.client.constants.StatsScreenConstants;
import com.client.constants.UIFonts;

public class StatsScreenComponents {
    // this tab is used to swtich btw pir chart and time series chart
    public static JLabel createTopTabLabel(String text, boolean active) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        Color bgColor = active ? new Color(255, 100, 90) : new Color(30, 35, 48);
        int top = 6, left = 14, bottom = 6, right = 14;
        Color inactiveTextColor = new Color(180, 180, 180);

        lbl.setFont(UIFonts.TEXT_BOLD);
        lbl.setOpaque(true);
        lbl.setBackground(bgColor);
        lbl.setForeground(active ? Color.WHITE : inactiveTextColor);
        lbl.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        
        return lbl;
    }
    
    //  toggle button for selecting time range
    public static JToggleButton createToggleButton(String text) {
        JToggleButton jToggleButton = new JToggleButton(text) {
            Color bgColor = new Color(255, 100, 90);

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Selected pill background
                if (isSelected()) {
                    g2.setColor(bgColor); // coral highlight
                    g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 18, 18);
                }

                super.paintComponent(g);
                g2.dispose();
            }
        };

        jToggleButton.setFont(UIFonts.TEXT_BOLD);
        jToggleButton.setOpaque(false);
        jToggleButton.setContentAreaFilled(false);
        jToggleButton.setBorderPainted(false);
        jToggleButton.setFocusPainted(false);
        jToggleButton.setHorizontalAlignment(SwingConstants.CENTER);

        return jToggleButton;
    }

    // Pie slice data model
    public static class PieSlice {
        public final String category;
        public final String emoji;
        public final long amount;
        public final double percent;
        public final Color color;
        
        public PieSlice(
            String category,
            String emoji,
            long amount,
            double percent,
            Color color
        ) {
            this.category = category;
            this.emoji = emoji;
            this.amount = amount;
            this.percent = percent;
            this.color = color;
        }
    }

    // Emoji mapper for categories
    public static class EmojiMapper {
        // case insensitive map of category name to emoji
        public static final Map<String, String> MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        
        static {
            MAP.put("Food", "🍜");
            MAP.put("Grocery", "🛒");
            MAP.put("Groceries", "🛒");
            MAP.put("Transport", "🚗");
            MAP.put("Travel", "✈️");
            MAP.put("Shopping", "🛍️");
            MAP.put("Salary", "💼");
            MAP.put("Investment", "📈");
            MAP.put("Medical", "🏥");
            MAP.put("Health", "💊");
            MAP.put("Household", "🪑");
            MAP.put("Rent", "🏠");
            MAP.put("Cloth", "👕");
            MAP.put("Clothes", "👕");
            MAP.put("Entertainment", "🎬");
            MAP.put("Other", "💸");
        }


        public static String getEmojiForCategory(String category) {
            if (category == null) {
                return "💸";
            }
            String emoji = MAP.get(category);

            if(emoji != null) {
                return emoji;
            }
            return "💸";
        }
    }

// Legend item for pie chart
    public static class CategoryLegendItem extends JPanel {
        public CategoryLegendItem(
            Color color,
            String percentText,
            String emoji,
            String name,
            String amount) {
            // panel settings
            setOpaque(true);
            setBackground(new Color(25, 30, 45));
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(6, 10, 6, 10));
            setAlignmentX(LEFT_ALIGNMENT);

            // Left: colored pill + category name + emoji
            JPanel leftPanel = new JPanel();
            leftPanel.setOpaque(false);
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));

            JLabel percentLabel = new JLabel(percentText);
            percentLabel.setFont(UIFonts.TEXT_BOLD);
            percentLabel.setForeground(Color.BLACK);
            percentLabel.setOpaque(true);
            percentLabel.setBackground(color);
            percentLabel.setBorder(new EmptyBorder(2, 6, 2, 6));

            JLabel nameLabel = new JLabel("  " + emoji + "  " + name);
            nameLabel.setFont(UIFonts.TEXT);
            nameLabel.setForeground(Color.WHITE);

            leftPanel.add(percentLabel);
            leftPanel.add(Box.createHorizontalStrut(8));
            leftPanel.add(nameLabel);

            // Right: amount
            JLabel amountLabel = new JLabel(amount);
            amountLabel.setFont(UIFonts.TEXT);
            amountLabel.setForeground(Color.WHITE);
            amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            add(leftPanel, BorderLayout.WEST);
            add(amountLabel, BorderLayout.EAST);

            setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
        }
    }

    // Pie chart panel
    public static class PieChartPanel extends JPanel {

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
}
