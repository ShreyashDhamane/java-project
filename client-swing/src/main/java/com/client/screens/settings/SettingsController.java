package com.client.screens.settings;

import javax.swing.JOptionPane;

import com.client.constants.Constants;
import com.client.core.AppState;
import com.client.core.ScreenManager;
import com.client.screens.category.ManageCategories;
import com.client.screens.login.LoginScreen;
import com.client.utils.HttpClient;

public class SettingsController {

    private final SettingsScreen scr;

    public SettingsController(SettingsScreen scr) {
        this.scr = scr;
    }

    public void wireEvents() {

        // Currency change
        scr.currencyDropdown.addActionListener(e -> {
            String item = (String) scr.currencyDropdown.getSelectedItem();
            if (item != null) {
                String code = item.split(" ")[0];
                AppState.getInstance().setCurrencyCode(code);
            }
        });

        // Manage categories
        scr.categoriesBtn.addActionListener(e ->
            ScreenManager.show(new ManageCategories())
        );

        // Logout
        scr.logoutBtn.addActionListener(e -> {
            AppState.getInstance().reset();
            ScreenManager.show(new LoginScreen());
        });

        // Delete account
        scr.deleteAccountBtn.addActionListener(e -> confirmDeleteAccount());
    }

    private void confirmDeleteAccount() {
        int opt = JOptionPane.showConfirmDialog(
                scr,
                "Are you sure you want to permanently delete your account?\nThis action cannot be undone.",
                "Delete Account",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (opt == JOptionPane.YES_OPTION) {
            deleteAccountAsync();
        }
    }

    private void deleteAccountAsync() {
        new Thread(() -> {
            try {
                String username = AppState.getInstance().getUsername();
                String result = HttpClient.delete(Constants.BASE_URL + "/auth/" + username);

                if ("OK".equals(result)) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(
                            scr,
                            "Your account has been deleted.",
                            "Deleted",
                            JOptionPane.INFORMATION_MESSAGE
                        );

                        AppState.getInstance().reset();
                        ScreenManager.show(new LoginScreen());
                    });
                } else {
                    javax.swing.SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(
                            scr,
                            "Failed to delete account. Try again later.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        )
                    );
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}
