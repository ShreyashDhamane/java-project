package com.client.screens.login;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.client.core.BasePanel;

public class LoginScreen extends BasePanel {

    // UI references
    public JTextField userField;
    public JPasswordField passField;
    public JLabel status;
    public JButton loginBtn;
    public JButton signupBtn;

    public LoginScreen() {
        setLayout(null);
        setOpaque(false);

        // Build UI (components + styling)
        new LoginUIBuilder().build(this);

        // Attach logic
        new LoginController().wireEvents(this);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        new LoginUIBuilder().layout(this);
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
