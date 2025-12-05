package com.client.components.stats;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.client.constants.UIFonts;

public class StatsTopTabLabel {
    private static final Color ACTIVE_COLOR = new Color(255, 100, 90);
    private static final Color INACTIVE_COLOR = new Color(30, 35, 48);

    public static JLabel create(String text, boolean active) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(UIFonts.TEXT_BOLD);
        lbl.setOpaque(true);
        lbl.setBackground(active ? ACTIVE_COLOR : INACTIVE_COLOR);
        lbl.setForeground(active ? Color.WHITE : new Color(180, 180, 180));
        lbl.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return lbl;
    }
}
