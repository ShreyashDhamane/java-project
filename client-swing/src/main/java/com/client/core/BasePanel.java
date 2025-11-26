package com.client.core;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.client.constants.UIColors;
import com.client.constants.UIFonts;

// base panel is extended by all other panels to ensure consistent styling
// all ui panels extend this base panel for consistentcy
public class BasePanel extends JPanel {

    public BasePanel() {
        setBackground(UIColors.BACKGROUND); // consistent background color
        setLayout(null); // absolute positioning
    }

    // helper method to create a styled label and add to panel
    public JLabel label(
        String text,
        Font font,
        Color color,
        int x,
        int y,
        int w,
        int h
    ) {
        JLabel lbl = new JLabel(text);

        lbl.setFont(font);
        lbl.setForeground(color);
        lbl.setBounds(x, y, w, h);

        add(lbl);

        return lbl;
    }

    // helper method to create a styled button
    public JButton button(
        String text,
        int x,
        int y,
        int w,
        int h
    ) {
        JButton btn = new JButton(text);

        btn.setFont(UIFonts.BUTTON);
        btn.setBounds(x, y, w, h);
        
        return btn;
    }
}
