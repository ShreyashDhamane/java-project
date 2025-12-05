package com.client.components.stats;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import com.client.constants.UIFonts;

public class StatsScreenComponents {
    // this tab is used to swtich btw pir chart and time series chart
    private final static Color ACTIV_COLOR = new Color(255, 100, 90);
    private final static Color INACTIVE_COLOR = new Color(30, 35, 48);

    public static JLabel createTopTabLabel(String text, boolean active) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        Color bgColor = active ? ACTIV_COLOR : INACTIVE_COLOR;
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
}
