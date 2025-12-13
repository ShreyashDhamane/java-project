package com.client.screens.settings;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.client.components.BottomNavigationBar;
import com.client.core.BasePanel;

public class SettingsScreen extends BasePanel {

    public BottomNavigationBar bottomNav;

    public JButton categoriesBtn;
    public JButton logoutBtn;
    public JButton deleteAccountBtn;

    public JComboBox<String> currencyDropdown;
    public JLabel currencyLabel;
    public JLabel titleLabel;

    private final SettingsController controller;

    public SettingsScreen() {
        setLayout(null);
        setOpaque(false);

        // Build UI
        new SettingsUIBuilder().build(this);

        // Controller
        controller = new SettingsController(this);

        // Wire events
        controller.wireEvents();
    }

    @Override
    public void doLayout() {
        super.doLayout();
        new SettingsUIBuilder().layout(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(new GradientPaint(
                0, 0, new Color(75,108,183),
                0, getHeight(), new Color(24,40,72)
        ));
        g2.fillRect(0,0,getWidth(),getHeight());
        g2.dispose();
    }
}
