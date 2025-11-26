package com.client.core;

import javax.swing.JFrame;
import javax.swing.JPanel;
// this is the screen manager that handles switching between different screens/panels
// this is used to change views in the main application frame
public class ScreenManager {
    private static JFrame frame;

    public static void init(JFrame mainFrame) {
        frame = mainFrame;
    }
    // we use show method to switch panels/screens
    public static void show(JPanel panel) {
        // attach
        frame.setContentPane(panel);

        // resize and layout
        panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        panel.doLayout();

        // refresh
        frame.revalidate();
        frame.repaint();
    }
}
