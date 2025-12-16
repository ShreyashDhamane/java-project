package com.client.screens.login;

import java.util.List;

import org.json.JSONObject;

import com.client.constants.Constants;
import com.client.core.AppState;
import com.client.core.ScreenManager;
import com.client.model.DataEntry;
import com.client.model.ExpenseCategory;
import com.client.screens.dashboard.DashboardScreen;
import com.client.utils.HttpClient;
import com.client.utils.Validators;

public class LoginController {

    public void wireEvents(LoginScreen scr) {

        scr.loginBtn.addActionListener(e -> {
            scr.status.setText("");

            String username = scr.userField.getText();
            String password = new String(scr.passField.getPassword());

            if (Validators.isEmpty(username) || Validators.isEmpty(password)) {
                scr.status.setText("All fields are required.");
                return;
            }

            if (!Validators.isStrongPassword(password)) {
                scr.status.setText("<html>Password must be at least 8 characters,<br>"
                        + "include uppercase, lowercase, digit, and special character</html>");
                return;
            }

            // SEND LOGIN REQUEST
            String response = HttpClient.post(
                    Constants.BASE_URL + "/auth/login",
                    "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"
            );

            if (response == null) {
                scr.status.setText("Login failed. Could not reach server.");
                return;
            }

            try {

                JSONObject obj = new JSONObject(response);

                if (!obj.getBoolean("success")) {
                    scr.status.setText("Invalid username or password.");
                    return;
                }

                // GET JWT
                String token = obj.getString("token");

                AppState.getInstance().setUsername(username);
                AppState.getInstance().setJwtToken(token);

                // Fetch entries and categories
                String entriesJson = HttpClient.get(Constants.BASE_URL + "/api/data-entries/user/" + username);

                if (entriesJson != null && !entriesJson.isEmpty()) {
                    List<DataEntry> entries = HttpClient.fromJsonList(entriesJson, DataEntry.class);
                    AppState.getInstance().setEntries(entries);
                }

                String catJson = HttpClient.get(Constants.BASE_URL + "/categories/" + username);

                if (catJson != null && !catJson.isEmpty()) {
                    List<ExpenseCategory> cats = HttpClient.fromJsonList(catJson, ExpenseCategory.class);
                    AppState.getInstance().setCategories(cats);
                }

                ScreenManager.show(new DashboardScreen());

            } catch (Exception ex) {
                ex.printStackTrace();
                scr.status.setText("Error parsing login response.");
            }
        });
    }
}
