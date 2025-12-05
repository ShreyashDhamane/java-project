package com.client.components.stats;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JToggleButton;

import com.client.constants.UIFonts;

public class TimeRangeToggleButton {
    public static JToggleButton create(String text) {
        JToggleButton btn = new JToggleButton(text) {
            Color bgColor = new Color(255, 100, 90);

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isSelected()) {
                    g2.setColor(bgColor);
                    g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 18, 18);
                }

                super.paintComponent(g);
                g2.dispose();
            }
        };

        btn.setFont(UIFonts.TEXT_BOLD);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);

        return btn;
    }
}
