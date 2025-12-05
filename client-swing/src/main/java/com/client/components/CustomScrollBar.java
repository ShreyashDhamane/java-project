package com.client.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

// we need custom scroll bar to match the overall dark theme of the app
public class CustomScrollBar extends BasicScrollBarUI {
    private static final Color THUMB_COLOR = new Color(200, 220, 255, 200);  // light blue with some transparency
    private static final Color TRACK_COLOR = new Color(0, 0, 0, 0);          // fully transparent/ invisible
    private static final Dimension ZERO_DIMENSION = new Dimension(0, 0);

    @Override
    protected void configureScrollBarColors() {
        thumbColor = THUMB_COLOR;  // thumb
        trackColor = TRACK_COLOR;          // transparent
        scrollbar.setOpaque(false); // make scrollbar background transparent
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        // no buttons for scroll bar at all, total mouse control
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        // same as above
        return createZeroButton();
    }

    // this is. a helper method to create a zero size button
    // lieteral invisible button
    // use case we dont want buttons on scrollbar, its used above
    private JButton createZeroButton() {
        JButton btn = new JButton();

        btn.setPreferredSize(ZERO_DIMENSION);
        btn.setMinimumSize(ZERO_DIMENSION);
        btn.setMaximumSize(ZERO_DIMENSION);

        return btn;
    }

    // custom paint for thumb
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2 = (Graphics2D) g.create();
        // smooth edges
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        if (!scrollbar.isEnabled()) {
            return;
        }
        // draw thumb
        g2.setColor(THUMB_COLOR);

        int arc = 12; // border radius for thumb
        g2.fillRoundRect(
            r.x,
            r.y,
            r.width,
            r.height,
            arc,
            arc
        );

        g2.dispose();
    }

}
