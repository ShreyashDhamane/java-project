package com.client.screens.signup;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.client.components.signup.SignupComponents;
import com.client.constants.UIFonts;
import com.client.constants.UISizes;
import com.client.constants.UIStyle;
import com.client.core.ScreenManager;
import com.client.screens.login.LoginScreen;

public class SignupUIBuilder {

    public void build(SignupScreen scr) {

        scr.titleLabel = UIStyle.titleLabel("Create Account");
        scr.subtitleLabel = UIStyle.subtitleLabel("Fill your details below");

        scr.add(scr.titleLabel);
        scr.add(scr.subtitleLabel);

        // Labels
        JLabel firstLabel   = UIStyle.styledLabel("First Name");
        JLabel lastLabel    = UIStyle.styledLabel("Last Name");
        JLabel userLabel    = UIStyle.styledLabel("Username");
        JLabel emailLabel   = UIStyle.styledLabel("Email");
        JLabel passLabel    = UIStyle.styledLabel("Password");
        JLabel confirmLabel = UIStyle.styledLabel("Confirm Password");

        scr.add(firstLabel);
        scr.add(lastLabel);
        scr.add(userLabel);
        scr.add(emailLabel);
        scr.add(passLabel);
        scr.add(confirmLabel);

        // Fields
        scr.firstNameField = new JTextField();
        UIStyle.styleTextField(scr.firstNameField);
        scr.firstNameField.setFont(UIFonts.TEXT);
        scr.add(scr.firstNameField);

        scr.lastNameField = new JTextField();
        UIStyle.styleTextField(scr.lastNameField);
        scr.lastNameField.setFont(UIFonts.TEXT);
        scr.add(scr.lastNameField);

        scr.usernameField = new JTextField();
        UIStyle.styleTextField(scr.usernameField);
        scr.usernameField.setFont(UIFonts.TEXT);
        scr.add(scr.usernameField);

        scr.emailField = new JTextField();
        UIStyle.styleTextField(scr.emailField);
        scr.emailField.setFont(UIFonts.TEXT);
        scr.add(scr.emailField);

        scr.passField = new JPasswordField();
        UIStyle.stylePasswordField(scr.passField);
        scr.passField.setFont(UIFonts.TEXT);
        scr.add(scr.passField);

        scr.confirmPassField = new JPasswordField();
        UIStyle.stylePasswordField(scr.confirmPassField);
        scr.confirmPassField.setFont(UIFonts.TEXT);
        scr.add(scr.confirmPassField);

        // Buttons
        scr.signupBtn = UIStyle.createPrimaryButton("Sign Up");
        scr.add(scr.signupBtn);

        scr.loginBtn = UIStyle.createPrimaryButton("Login");
        scr.loginBtn.addActionListener(e -> ScreenManager.show(new LoginScreen()));
        scr.add(scr.loginBtn);

        scr.status = SignupComponents.statusLabel();
        scr.add(scr.status);
    }

    public void layout(SignupScreen scr) {

        int panelW = scr.getWidth();
        int panelH = scr.getHeight();

        int startX = (panelW - 400) / 2 + 15;
        int startY = (panelH - 400) / 2;

        int leftX = startX + 20;
        int rightX = startX + 220;
        int y = startY;

        // Title
        scr.titleLabel.setBounds(startX + 100, y - 70, 300, 40);
        scr.subtitleLabel.setBounds(startX + 100, y - 35, 300, 25);

        // Row 1 - First + Last name
        scr.getComponent(2).setBounds(leftX,  y,     180, 20); // First label
        scr.firstNameField.setBounds(leftX,  y + 25, 160, UISizes.INPUT_HEIGHT);

        scr.getComponent(3).setBounds(rightX, y,     180, 20); // Last label
        scr.lastNameField.setBounds(rightX, y + 25, 160, UISizes.INPUT_HEIGHT);
        y += 70;

        // Row 2 - Username + Email
        scr.getComponent(4).setBounds(leftX, y,     180, 20); // Username label
        scr.usernameField.setBounds(leftX, y + 25, 160, UISizes.INPUT_HEIGHT);

        scr.getComponent(5).setBounds(rightX, y,     180, 20); // Email label
        scr.emailField.setBounds(rightX, y + 25, 160, UISizes.INPUT_HEIGHT);
        y += 70;

        // Row 3 - Password + Confirm
        scr.getComponent(6).setBounds(leftX, y,     180, 20); // Pass label
        scr.passField.setBounds(leftX, y + 25, 160, UISizes.INPUT_HEIGHT);

        scr.getComponent(7).setBounds(rightX, y,     180, 20); // Confirm label
        scr.confirmPassField.setBounds(rightX, y + 25, 160, UISizes.INPUT_HEIGHT);
        y += 80;

        // Buttons
        scr.signupBtn.setBounds(leftX,  y, 140, 40);
        scr.loginBtn.setBounds(rightX, y, 140, 40);

        // Status
        y += 30;
        scr.status.setBounds(leftX, y, 350, 60);
    }
}
