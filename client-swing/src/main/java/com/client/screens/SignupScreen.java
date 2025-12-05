package com.client.screens;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.client.constants.Constants;
import com.client.constants.UIColors;
import com.client.constants.UIFonts;
import com.client.constants.UISizes;
import com.client.constants.UIStyle;
import com.client.core.BasePanel;
import com.client.core.ScreenManager;
import com.client.screens.login.LoginScreen;
import com.client.utils.HttpClient;
import com.client.utils.Validators;

public class SignupScreen extends BasePanel {

    private JTextField firstNameField, lastNameField, usernameField, emailField;
    private JPasswordField passField, confirmPassField;
    private JLabel status, titleLabel, subtitleLabel;
    private JButton signupBtn, loginBtn;

    public SignupScreen() {
        setLayout(null);
        setOpaque(false);
        buildUI();
    }

    private void buildUI() {
        // Labels
        titleLabel = UIStyle.titleLabel("Create Account");
        subtitleLabel = UIStyle.subtitleLabel("Fill your details below");

        add(titleLabel);
        add(subtitleLabel);

        // create all labels
        JLabel firstLabel = UIStyle.styledLabel("First Name");
        JLabel lastLabel = UIStyle.styledLabel("Last Name");
        JLabel userLabel = UIStyle.styledLabel("Username");
        JLabel emailLabel = UIStyle.styledLabel("Email");
        JLabel passLabel = UIStyle.styledLabel("Password");
        JLabel confirmLabel = UIStyle.styledLabel("Confirm Password");
        // we can position them in doLayout
        add(firstLabel);
        add(lastLabel);
        add(userLabel);
        add(emailLabel);
        add(passLabel);
        add(confirmLabel);

        // Fields
        firstNameField = new JTextField();
        firstNameField.setFont(UIFonts.TEXT);

        UIStyle.styleTextField(firstNameField);
        
        add(firstNameField);
        
        lastNameField  = new JTextField();
        lastNameField.setFont(UIFonts.TEXT);

        UIStyle.styleTextField(lastNameField);
        add(lastNameField);

        usernameField  = new JTextField();
        usernameField.setFont(UIFonts.TEXT);
        UIStyle.styleTextField(usernameField);

        add(usernameField);

        emailField     = new JTextField();
        emailField.setFont(UIFonts.TEXT);
        UIStyle.styleTextField(emailField);

        add(emailField);

        passField      = new JPasswordField();
        passField.setFont(UIFonts.TEXT);
        UIStyle.stylePasswordField(passField);

        add(passField);

        confirmPassField = new JPasswordField();
        confirmPassField.setFont(UIFonts.TEXT);

        UIStyle.stylePasswordField(confirmPassField);

        add(confirmPassField);

        // Buttons
        signupBtn = UIStyle.createPrimaryButton("Sign Up");
        add(signupBtn);
        loginBtn = UIStyle.createPrimaryButton("Login"); add(loginBtn);

        // Status
        status = label("", UIFonts.TEXT, UIColors.ERROR, 0, 0, 350, 60);
        add(status);

        // Actions
        loginBtn.addActionListener(e -> ScreenManager.show(new LoginScreen()));
        signupBtn.addActionListener(e -> handleSignup(firstLabel, lastLabel, userLabel, emailLabel, passLabel, confirmLabel));
    }


    private void handleSignup(JLabel... labels) {
        status.setText("");

        // get data
        String first = firstNameField.getText().trim();
        String last  = lastNameField.getText().trim();
        String user  = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String pass  = new String(passField.getPassword());
        String confirm = new String(confirmPassField.getPassword());

        // validate
        if (Validators.isEmpty(first) || Validators.isEmpty(last) || Validators.isEmpty(user) ||
            Validators.isEmpty(email) || Validators.isEmpty(pass) || Validators.isEmpty(confirm)) {
            status.setText("All fields are required.");
            return;
        }
        if (!Validators.isEmail(email)) {
            status.setText("Invalid email address.");
            return;
        }
        if (!pass.equals(confirm)) {
            status.setText("Passwords do not match.");
            return;
        }
        if (!Validators.isStrongPassword(pass)) {
            status.setText("<html>Password must be at least 8 characters,<br>include uppercase, lowercase, digit, and special character.</html>");
            return;
        }

        // send signup request
        String body = """
            {
                "firstName": "%s",
                "lastName": "%s",
                "username": "%s",
                "email": "%s",
                "password": "%s"
            }
            """.formatted(first, last, user, email, pass);

            // make request
        String response = HttpClient.post(Constants.BASE_URL + "/auth/register", body);

        // handle response
        if (response == null) {
            status.setText("Signup failed.");
        } else if (response.equals("EXISTS")) {
            status.setText("User already exists.");
        } else if (response.equals("OK")) {
            status.setForeground(UIColors.SUCCESS);
            status.setText("Account created! Redirecting...");
            // redirect to login after a short delay
            ScreenManager.show(new LoginScreen());
        } else {
            status.setText("Unexpected response: " + response);
        }
    }

    @Override
    public void doLayout() {
        super.doLayout();

        int panelW = getWidth();
        int panelH = getHeight();

        int startX = (panelW - 400) / 2 + 15;
        int startY = (panelH - 400) / 2;

        int leftX = startX + 20;
        int rightX = startX + 220;
        int y = startY;

        // Title
        titleLabel.setBounds(startX + 100, y - 70, 300, 40);
        subtitleLabel.setBounds(startX + 100, y - 35, 300, 25);

        // Row 1
        getComponent(2).setBounds(leftX, y, 180, 20);
        
        firstNameField.setBounds(leftX, y + 25, 160, UISizes.INPUT_HEIGHT);
        getComponent(3).setBounds(rightX, y, 180, 20);
        
        lastNameField.setBounds(rightX, y + 25, 160, UISizes.INPUT_HEIGHT);
        y += 70;

        // Row 2
        getComponent(4).setBounds(leftX, y, 180, 20);
        
        usernameField.setBounds(leftX, y + 25, 160, UISizes.INPUT_HEIGHT);
        getComponent(5).setBounds(rightX, y, 180, 20);
        
        emailField.setBounds(rightX, y + 25, 160, UISizes.INPUT_HEIGHT);
        y += 70;

        // Row 3
        getComponent(6).setBounds(leftX, y, 180, 20);
        
        passField.setBounds(leftX, y + 25, 160, UISizes.INPUT_HEIGHT);
        getComponent(7).setBounds(rightX, y, 180, 20);
        
        confirmPassField.setBounds(rightX, y + 25, 160, UISizes.INPUT_HEIGHT);
        y += 80;

        
        // Buttons
        signupBtn.setBounds(leftX, y, 140, 40);
        loginBtn.setBounds(rightX, y, 140, 40);
        // Status
        y += 30;
        status.setBounds(leftX, y, 350, 60);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(0, 0, new Color(75, 108, 183), 0, getHeight(), new Color(24, 40, 72)));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
