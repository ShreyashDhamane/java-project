package com.client.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.plaf.basic.BasicComboBoxUI;


// custom dropdown UI for better look and feel, used in stats and dashboard screen for filtering data by month/year/category
public class CustomDropdown extends BasicComboBoxUI {

    private static final Color DROPDOWN_BG = new Color(35, 45, 65);        // dark navy
    
    // this arrow button is up and down diff from we have in arrowbutton.java
    // also we are ovverding the createArrowButton method of dropdown UI
    @Override
    protected JButton createArrowButton() {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                // custom paint for arrow button
                Graphics2D g2 = (Graphics2D) g.create();

                // smooth edges
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );

                int w = getWidth();
                int h = getHeight();
                int size = 6;

                // draw arrow
                g2.setColor(Color.WHITE);

                // ▼ (single arrow)
                g2.fillPolygon(
                    new int[]{w/2 - size, w/2, w/2 + size},
                    new int[]{h/2 - 2, h/2 + 4, h/2 - 2},
                    3
                );

                g2.dispose();
            }
        };

        btn.setBorder(null); // no border
        btn.setFocusPainted(false); // no focus border
        btn.setOpaque(false); // transparent background
        btn.setContentAreaFilled(false);  // no content area fill

        return btn;
    }

    // Paint the background of the selected value to giv custom look
    @Override
    public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(DROPDOWN_BG); // set custom bg color
        g2.fillRoundRect(
            bounds.x, 
            bounds.y, 
            bounds.width, 
            bounds.height, 
            10, 
            10
        ); // border radius for rectangle
    }

}
