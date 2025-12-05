package com.client.screens.signup;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.client.core.BasePanel;

public class SignupScreen extends BasePanel {

    // UI references (populated by builder)
    public JTextField firstNameField, lastNameField, usernameField, emailField;
    public JPasswordField passField, confirmPassField;
    public JLabel status, titleLabel, subtitleLabel;
    public JButton signupBtn, loginBtn;

    public SignupScreen() {
        setLayout(null);
        setOpaque(false);

        // Build UI (no logic here)
        new SignupUIBuilder().build(this);

        // Wire logic
        new SignupController().wireEvents(this);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        new SignupUIBuilder().layout(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(
            0, 0, new Color(75, 108, 183),
            0, getHeight(), new Color(24, 40, 72)
        ));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
