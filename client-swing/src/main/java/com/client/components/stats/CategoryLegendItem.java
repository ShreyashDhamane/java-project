package com.client.components.stats;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.client.constants.UIFonts;

public class CategoryLegendItem extends JPanel {
    public CategoryLegendItem(Color color, String percent, String emoji, String name, String amount) {

        setOpaque(true);
        setBackground(new Color(25, 30, 45));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(6, 10, 6, 10));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));

        JLabel percentLabel = new JLabel(percent);
        percentLabel.setFont(UIFonts.TEXT_BOLD);
        percentLabel.setOpaque(true);
        percentLabel.setBackground(color);
        percentLabel.setForeground(Color.BLACK);
        percentLabel.setBorder(new EmptyBorder(2, 6, 2, 6));

        JLabel nameLabel = new JLabel("  " + emoji + "  " + name);
        nameLabel.setFont(UIFonts.TEXT);
        nameLabel.setForeground(Color.WHITE);

        left.add(percentLabel);
        left.add(Box.createHorizontalStrut(8));
        left.add(nameLabel);

        JLabel amountLabel = new JLabel(amount);
        amountLabel.setFont(UIFonts.TEXT);
        amountLabel.setForeground(Color.WHITE);
        amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        add(left, BorderLayout.WEST);
        add(amountLabel, BorderLayout.EAST);
    }
}
