package com.client.screens;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.client.constants.Constants;
import com.client.constants.UIColors;
import com.client.constants.UIFonts;
import com.client.constants.UISizes;
import com.client.constants.UIStyle;
import com.client.core.AppState;
import com.client.core.BasePanel;
import com.client.core.ScreenManager;
import com.client.model.DataEntry;
import com.client.model.ExpenseCategory;
import com.client.screens.dashboard.DashboardScreen;
import com.client.utils.HttpClient;
import com.client.utils.Validators;

public class LoginScreen extends BasePanel {
    private JTextField userField;
    private JPasswordField passField;
    private JLabel status;
    private JButton loginBtn;
    private JButton signupBtn;

    public LoginScreen() {
        // initialize panel
        setLayout(null);
        setOpaque(false);

        buildUI();      // build UI only ONCE
    }

    private void buildUI() {
        int boxWidth = 350;
        int boxHeight = 300;

        int startX = (getWidth() - boxWidth) / 2 + 30;
        int startY = (getHeight() - boxHeight) / 2;

        // title and subtitle
        add(UIStyle.titleLabel("Money Manager"));
        add(UIStyle.subtitleLabel("Login to continue"));

        // username label
        add(UIStyle.styledLabel("Username"));

        
        // username field
        userField = new JTextField();

        UIStyle.styleTextField(userField);
        userField.setBounds(startX, startY + 25, 300, UISizes.INPUT_HEIGHT);
        
        add(userField);

        // password label
        add(UIStyle.styledLabel("Password"));

        // password field
        passField = new JPasswordField();

        UIStyle.stylePasswordField(passField);
        passField.setBounds(startX, startY + 90, 300, UISizes.INPUT_HEIGHT);
        
        add(passField);

        // login button
        loginBtn = UIStyle.createPrimaryButton("Login");
        loginBtn.setBounds(startX, startY + 140, 140, 40);

        loginBtn.addActionListener(e -> {
            status.setText("");

            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (Validators.isEmpty(username) || Validators.isEmpty(password)) {
                status.setText("All fields are required.");
                return;
            }

            if (!Validators.isStrongPassword(password)) {
                status.setText("<html>Password must be at least 8 characters,<br>"
                        + "include uppercase, lowercase, digit, and special character</html>");
                return;
            }

            // ---- SEND LOGIN REQUEST ----
            String response = HttpClient.post(
                    Constants.BASE_URL + "/auth/login",
                    "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"
            );

            if (response == null) {
                status.setText("Login failed. Could not reach server.");
                return;
            }

            try {
                // Parse JSON
                org.json.JSONObject obj = new org.json.JSONObject(response);

                if (!obj.getBoolean("success")) {
                    status.setText("Invalid username or password.");
                    return;
                }

                // ---- GET JWT ----
                String token = obj.getString("token");

                // Save in AppState
                AppState.getInstance().setUsername(username);
                AppState.getInstance().setJwtToken(token);

                // ---- Fetch entries with JWT ----
                String entriesJson = HttpClient.get(
                        Constants.BASE_URL + "/api/data-entries/user/" + username
                );

                if (entriesJson != null && !entriesJson.isEmpty()) {
                    List<DataEntry> entries = HttpClient.fromJsonList(entriesJson, DataEntry.class);
                    AppState.getInstance().setEntries(entries);
                }

                // ---- Fetch categories with JWT ----
                String catJson = HttpClient.get(
                        Constants.BASE_URL + "/categories/" + username
                );

                if (catJson != null && !catJson.isEmpty()) {
                    List<ExpenseCategory> cats = HttpClient.fromJsonList(catJson, ExpenseCategory.class);
                    AppState.getInstance().setCategories(cats);
                }

                // ---- Go to dashboard ----
                ScreenManager.show(new DashboardScreen());

            } catch (Exception ex) {
                ex.printStackTrace();
                status.setText("Error parsing login response.");
            }

        });


        add(loginBtn);

        // signup button
        signupBtn = UIStyle.createPrimaryButton("Sign Up");
        signupBtn.setBounds(startX + 160, startY + 140, 140, 40);
        signupBtn.addActionListener(e -> ScreenManager.show(new SignupScreen()));

        add(signupBtn);

        // status label
        status = label("", UIFonts.TEXT, UIColors.ERROR,
            startX, startY + 120, 500, 100);

        add(status);

        
    }

    @Override
    public void doLayout() {
        // reposition components based on current panel size
        super.doLayout();

        int panelW = getWidth();
        int panelH = getHeight();

        int boxWidth = 350;
        int boxHeight = 300;

        int startX = (panelW - boxWidth) / 2 + 30;
        int startY = (panelH - boxHeight) / 2;

        // reposition existing components
        getComponent(0).setBounds(startX + 50, startY - 100, 300, 40); // title
        getComponent(1).setBounds(startX + 70, startY - 55, 250, 25);  // subtitle

        getComponent(2).setBounds(startX, startY, 300, 20);            // Username label
        userField.setBounds(startX, startY + 25, 300, UISizes.INPUT_HEIGHT);

        getComponent(4).setBounds(startX, startY + 65, 300, 20);       // Password label
        passField.setBounds(startX, startY + 90, 300, UISizes.INPUT_HEIGHT);

        loginBtn.setBounds(startX, startY + 140, 140, 40);
        signupBtn.setBounds(startX + 160, startY + 140, 140, 40);

        status.setBounds(startX, startY + 190, 350, 70);
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
