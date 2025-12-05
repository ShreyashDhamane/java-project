package com.client.components.category;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class CategoryUIFactory {

    public static JButton createAddButton() {

        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();
                int d = Math.min(w, h);

                g2.setColor(new Color(255,100,90));
                g2.fillOval(0,0,d,d);

                g2.setColor(Color.WHITE);
                int cx = w/2, cy = h/2;
                int lineLength = (int)(d * 0.45);
                int t = 4;

                g2.fillRoundRect(cx - lineLength/2, cy - t/2, lineLength, t, 4,4);
                g2.fillRoundRect(cx - t/2, cy - lineLength/2, t, lineLength, 4,4);

                g2.dispose();
            }
        };

        btn.setPreferredSize(new Dimension(52,52));
        btn.setOpaque(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);

        return btn;
    }
}
