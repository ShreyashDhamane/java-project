package com.client.components.settings;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicComboBoxUI;

import com.client.constants.UIFonts;

public class CurrencyDropdownUI {

    public static void apply(JComboBox<String> dropdown) {

        dropdown.setBackground(new Color(35,45,65));
        dropdown.setForeground(Color.WHITE);
        dropdown.setBorder(BorderFactory.createEmptyBorder());

        dropdown.setUI(new BasicComboBoxUI() {

            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton("▼");
                btn.setForeground(Color.WHITE);
                btn.setBackground(new Color(35,45,65));
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setFocusPainted(false);
                return btn;
            }

            @Override
            public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
                g.setColor(new Color(35,45,65));
                g.fillRect(0,0,c.getWidth(),c.getHeight());
                super.paint(g,c);
            }
        });

        dropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                lbl.setOpaque(true);
                lbl.setForeground(Color.WHITE);
                lbl.setBackground(isSelected ? new Color(60,70,90) : new Color(35,45,65));
                lbl.setFont(UIFonts.TEXT);
                lbl.setBorder(new javax.swing.border.EmptyBorder(6,12,6,12));

                return lbl;
            }
        });
    }
}
