package com.client.screens.login;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.client.constants.UIColors;
import com.client.constants.UIFonts;
import com.client.constants.UISizes;
import com.client.constants.UIStyle;
import com.client.core.ScreenManager;
import com.client.screens.signup.SignupScreen;

public class LoginUIBuilder {

    public void build(LoginScreen scr) {

        // Title
        JLabel title = UIStyle.titleLabel("Money Manager");
        scr.add(title);

        // Subtitle
        JLabel subtitle = UIStyle.subtitleLabel("Login to continue");
        scr.add(subtitle);

        // Username label
        JLabel usernameLabel = UIStyle.styledLabel("Username");
        scr.add(usernameLabel);

        // Username field
        scr.userField = new JTextField();
        UIStyle.styleTextField(scr.userField);
        scr.add(scr.userField);

        // Password label
        JLabel passwordLabel = UIStyle.styledLabel("Password");
        scr.add(passwordLabel);

        // Password field
        scr.passField = new JPasswordField();
        UIStyle.stylePasswordField(scr.passField);
        scr.add(scr.passField);

        // Login button
        scr.loginBtn = UIStyle.createPrimaryButton("Login");
        scr.add(scr.loginBtn);

        // Signup button
        scr.signupBtn = UIStyle.createPrimaryButton("Sign Up");
        scr.signupBtn.addActionListener(e -> ScreenManager.show(new SignupScreen()));
        scr.add(scr.signupBtn);

        // Status label
        scr.status = new JLabel("", JLabel.LEFT);
        scr.status.setFont(UIFonts.TEXT);
        scr.status.setForeground(UIColors.ERROR);
        scr.add(scr.status);
    }

    public void layout(LoginScreen scr) {
        int panelW = scr.getWidth();
        int panelH = scr.getHeight();

        int boxWidth = 350;
        int boxHeight = 300;

        int startX = (panelW - boxWidth) / 2 + 30;
        int startY = (panelH - boxHeight) / 2;

        scr.getComponent(0).setBounds(startX + 50, startY - 100, 300, 40); // title
        scr.getComponent(1).setBounds(startX + 70, startY - 55, 250, 25);  // subtitle

        scr.getComponent(2).setBounds(startX, startY, 300, 20);            // username label
        scr.userField.setBounds(startX, startY + 25, 300, UISizes.INPUT_HEIGHT);

        scr.getComponent(4).setBounds(startX, startY + 65, 300, 20);       // password label
        scr.passField.setBounds(startX, startY + 90, 300, UISizes.INPUT_HEIGHT);

        scr.loginBtn.setBounds(startX, startY + 140, 140, 40);
        scr.signupBtn.setBounds(startX + 160, startY + 140, 140, 40);

        scr.status.setBounds(startX, startY + 190, 350, 70);
    }
}
