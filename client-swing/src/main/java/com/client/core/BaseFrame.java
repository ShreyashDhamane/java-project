package com.client.core;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.client.constants.UIColors;
import com.client.constants.UISizes;

// this is the base frame that all other frames will extend,
// to keep consistent styling and behavior across the application
public class BaseFrame extends JFrame {

    public BaseFrame(String title) {
        super(title);

        setSize(UISizes.WINDOW_WIDTH, UISizes.WINDOW_HEIGHT); // fixed size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit app on close
        setLocationRelativeTo(null); // center on screen
        getContentPane().setBackground(UIColors.BACKGROUND); // consistent background color
        setLayout(new BorderLayout()); // use border layout for flexibility
        setResizable(false); // fixed size window

        // all windoes extend this base frame, consistent look and feel
    }
}
