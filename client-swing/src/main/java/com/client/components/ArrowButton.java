package com.client.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;
// arrow buttons are used in carousel like compoments to navigate isLeftButton/right, 
// changing month on stats and dashboard page.
public class ArrowButton extends JButton {
    private final boolean isLeftButton;

    public ArrowButton(boolean isLeftButton) {
        this.isLeftButton = isLeftButton;

        setOpaque(false); // transparent background
        setContentAreaFilled(false); // no content area fill
        setBorderPainted(false); // no border

        setFocusPainted(false); // no focus border
    }

    // paint component is used to custom draw the button
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create(); // create a copy of Graphics2D
        // enable aliasing for smooth edges
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        int w = getWidth();
        int h = getHeight();

        // Background circle,
        g2.setColor(new Color(0, 0, 0, 60)); // semi transparent black
        g2.fillRoundRect(0, 0, w, h, 8, 8); // circular background

        // Arrow shape
        g2.setColor(Color.WHITE); // white arrow
        int size = Math.min(w, h) / 4; // size of arrow

        int cx = w / 2; // center x pos
        int cy = h / 2; // center y pos

        int[] x, y;

        if (isLeftButton) {
            // points isLeftButton
            x = new int[]{cx + size/2, cx - size/2, cx + size/2};
            y = new int[]{cy - size, cy, cy + size};
        } else {
            // coords points right
            x = new int[]{cx - size/2, cx + size/2, cx - size/2};
            y = new int[]{cy - size, cy, cy + size};
        }

        g2.fillPolygon(x, y, 3);
        g2.dispose();
    }
}
