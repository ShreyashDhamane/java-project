package com.client.screens.signup;

import com.client.constants.Constants;
import com.client.constants.UIColors;
import com.client.core.ScreenManager;
import com.client.screens.login.LoginScreen;
import com.client.utils.HttpClient;
import com.client.utils.Validators;

public class SignupController {

    public void wireEvents(SignupScreen scr) {

        scr.signupBtn.addActionListener(e -> handleSignup(scr));
    }

    private void handleSignup(SignupScreen scr) {

        scr.status.setForeground(UIColors.ERROR);
        scr.status.setText("");

        String first   = scr.firstNameField.getText().trim();
        String last    = scr.lastNameField.getText().trim();
        String user    = scr.usernameField.getText().trim();
        String email   = scr.emailField.getText().trim();
        String pass    = new String(scr.passField.getPassword());
        String confirm = new String(scr.confirmPassField.getPassword());

        // VALIDATION
        if (Validators.isEmpty(first) || Validators.isEmpty(last) ||
            Validators.isEmpty(user)  || Validators.isEmpty(email) ||
            Validators.isEmpty(pass)  || Validators.isEmpty(confirm)) {

            scr.status.setText("All fields are required.");
            return;
        }

        if (!Validators.isEmail(email)) {
            scr.status.setText("Invalid email address.");
            return;
        }

        if (!pass.equals(confirm)) {
            scr.status.setText("Passwords do not match.");
            return;
        }

        if (!Validators.isStrongPassword(pass)) {
            scr.status.setText("<html>Password must be at least 8 characters,<br>"
                    + "include uppercase, lowercase, digit, and special character.</html>");
            return;
        }

        // REQUEST BODY
        String body = """
            {
                "firstName": "%s",
                "lastName": "%s",
                "username": "%s",
                "email": "%s",
                "password": "%s"
            }
            """.formatted(first, last, user, email, pass);

        String response = HttpClient.post(Constants.BASE_URL + "/auth/register", body);

        if (response == null) {
            scr.status.setText("Signup failed.");
            return;
        }

        switch (response) {
            case "EXISTS" -> {
                scr.status.setText("User already exists.");
            }
            case "OK" -> {
                scr.status.setForeground(UIColors.SUCCESS);
                scr.status.setText("Account created! Redirecting...");
                ScreenManager.show(new LoginScreen());
            }
            default -> scr.status.setText("Unexpected response: " + response);
        }
    }
}
