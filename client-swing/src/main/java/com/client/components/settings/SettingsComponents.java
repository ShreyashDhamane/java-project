package com.client.components.settings;

import java.awt.Color;

import javax.swing.JButton;

import com.client.constants.UIFonts;

public class SettingsComponents {

    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UIFonts.TEXT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(40,50,70));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        return btn;
    }

    public static JButton dangerButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(new Color(220,80,80));
        return btn;
    }

    public static JButton dangerButton2(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(new Color(150,50,50));
        return btn;
    }
}
