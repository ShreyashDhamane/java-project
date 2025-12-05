package com.client.components;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.client.constants.UIColors;
import com.client.core.BasePanel;
import com.client.core.ScreenManager;
import com.client.screens.dashboard.DashboardScreen;
import com.client.screens.settings.SettingsScreen;
import com.client.screens.stats.StatsScreen;
// bottom navigation bar is a common component in all pages, allowing users to switch between main sections of the app.
public class BottomNavigationBar extends JPanel {

    private JButton chartsButton;
    private JButton settingsButton;
    private JButton dashboardButton;

    // create constatns insteda of using same value again and again
    // these are specific to this file
    public static final String STATS = "Stats";
    public static final String DASHBOARD = "Dashboard";
    public static final String SETTINGS = "Settings";
    public static final Color BASE_COLOR = new Color(40, 40, 40);
    public static final Color INACTIVE_BG_COLOR = new Color(60, 60, 60);
    public static final Color ACTIVE_BG_COLOR = new Color(75, 108, 183);

    private JButton activeButton; // keep track of active button,as we need to highlight it for better UX
    private final JPanel activeIndicator;

    public BottomNavigationBar(String activeTab) {
        setLayout(null); // absolute layout
        setOpaque(true); // makes background opaque
        setBackground(BASE_COLOR); // dark background color as our theme is dark blue, so dark grey/black fits well

        // Create buttons
        chartsButton = createBottomNavButton(STATS);
        dashboardButton = createBottomNavButton(DASHBOARD);
        settingsButton = createBottomNavButton(SETTINGS);

        // Add buttons to panel usiung add method
        add(chartsButton);
        add(dashboardButton);
        add(settingsButton);

            // Active indicator bar
        activeIndicator = new JPanel();
        activeIndicator.setBackground(UIColors.PRIMARY); // dark blue highliting under the active button

        add(activeIndicator);

        // Determine initial active button
        if (SETTINGS.equals(activeTab)) {
            activeButton = settingsButton;
        }
        else if (DASHBOARD.equals(activeTab)) {
            activeButton = dashboardButton;
        }
        else {
            activeButton = chartsButton;
        }

        // Button actions, on click take to their resp screen
        // these are lambda expressions for action listeners
        chartsButton.addActionListener(e -> setActiveTab(chartsButton, new StatsScreen()));
        settingsButton.addActionListener(e -> setActiveTab(settingsButton, new SettingsScreen()));
        dashboardButton.addActionListener(e -> setActiveTab(dashboardButton, new DashboardScreen()));
    }

    private JButton createBottomNavButton(String text) {
        JButton btn = new JButton(text);

        btn.setForeground(Color.WHITE); // white text as bg is black
        btn.setFocusPainted(false); // no focus border
        btn.setBorderPainted(false); // no border

        return btn;
    }

    private void setActiveTab(JButton btn, BasePanel screen) {
        activeButton = btn; // update active button

        // Reset all buttons as active bg will change
        // initiyaly give all same
        settingsButton.setBackground(INACTIVE_BG_COLOR);
        chartsButton.setBackground(INACTIVE_BG_COLOR);
        dashboardButton.setBackground(INACTIVE_BG_COLOR);

        // Highlight active bg only for active button
        activeButton.setBackground(ACTIVE_BG_COLOR);

        // Switch screen
        if (screen != null) {
            // show the selected screen if not null
            ScreenManager.show(screen);
        }


        // Position active indicator by setting its bounds and repainting
        repaint();
    }

    // layout method is called when the component is resized or needs to layout its children
    @Override
    public void doLayout() {
        super.doLayout();

        int w = getWidth(), h = getHeight();
        int navBtnW = w / 3; // three buttons, so divide width by 3

        chartsButton.setBounds(0, 0, navBtnW, h); // set bounds for each button
        dashboardButton.setBounds(navBtnW, 0, navBtnW, h);
        settingsButton.setBounds(2 * navBtnW, 0, navBtnW, h);

        // Position active indicator AFTER layout
        if (activeButton != null) {
            activeIndicator.setBounds(activeButton.getX(), h - 4, activeButton.getWidth(), 4);
        }
    }
}
