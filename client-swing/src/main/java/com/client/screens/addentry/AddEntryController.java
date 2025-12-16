package com.client.screens.addentry;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingUtilities;

import com.client.components.addentry.AddEntryForm;
import com.client.constants.Constants;
import com.client.core.AppState;
import com.client.core.BasePanel;
import com.client.core.ScreenManager;
import com.client.model.DataEntry;
import com.client.screens.dashboard.DashboardScreen;

public class AddEntryController {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    private final BasePanel panel;
    private final AddEntryForm form;
    private final DataEntry editingEntry;

    public AddEntryController(BasePanel panel, AddEntryForm form, DataEntry editingEntry) {
        this.panel = panel;
        this.form = form;
        this.editingEntry = editingEntry;
        wireActions();
    }

    private void wireActions() {
        // Back button
        form.getBackButton().addActionListener(e -> ScreenManager.show(new DashboardScreen()));

        // Submit
        form.getSubmitButton().addActionListener(e -> handleSubmit());

        // Delete (only if editing)
        if (editingEntry != null && form.getDeleteButton() != null) {
            form.getDeleteButton().addActionListener(e -> deleteEntryAsync());
        }
    }

    private void handleSubmit() {
        form.getStatusLabel().setText("");

        String type = form.getIncomeBtn().isSelected() ? "Income" : "Expense";
        Object dateValue = form.getDatePicker().getValue();

        String category = (String) form.getCategoryDropdown().getSelectedItem();
        String paymentType = (String) form.getPaymentTypeDropdown().getSelectedItem();
        String note = form.getNoteField().getText().trim();
        String amountText = form.getAmountField().getText().trim();

        if (amountText.isEmpty()) {
            form.getStatusLabel().setText("Amount cannot be empty");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountText);
            if (amount <= 0) {
                form.getStatusLabel().setText("Amount must be greater than 0");
                return;
            }
        } catch (NumberFormatException ex) {
            form.getStatusLabel().setText("Invalid amount");
            return;
        }

        String username = AppState.getInstance().getUsername();
        String dateStr = SDF.format((Date) dateValue);

        String json = String.format(
                "{\"username\":\"%s\",\"type\":\"%s\",\"date\":\"%s\",\"category\":\"%s\",\"note\":\"%s\",\"amount\":%d,\"paymentType\":\"%s\"}",
                username, type, dateStr, category, note, amount, paymentType
        );

        new Thread(() -> submitToBackend(username, type, dateStr, category, note, amount, paymentType, json)).start();
    }

    private void submitToBackend(
        String username,
        String type,
        String dateStr,
        String category,
        String note,
        int amount,
        String paymentType,
        String json
    ) {
        try {
            boolean isEdit = (editingEntry != null);
            String urlStr;

            if (isEdit) {
                urlStr = Constants.BASE_URL + "/api/data-entries/" + editingEntry.getId();
            } else {
                urlStr = Constants.BASE_URL + "/api/data-entries";
            }

            java.net.URL url = new java.net.URL(urlStr);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();

            conn.setRequestMethod(isEdit ? "PUT" : "POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String token = AppState.getInstance().getJwtToken();
            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            try (java.io.OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }

            int code = conn.getResponseCode();
            if (code == 200) {
                if (isEdit) {
                    // update existing entry in AppState
                    editingEntry.setType(type);
                    editingEntry.setDate(dateStr);
                    editingEntry.setCategory(category);
                    editingEntry.setPaymentType(paymentType);
                    editingEntry.setNote(note);
                    editingEntry.setAmount(amount);
                } else {
                    // create new entry in AppState
                    DataEntry newEntry = new DataEntry(username, type, dateStr, category, note, amount, paymentType);
                    AppState.getInstance().addEntry(newEntry);
                }

                SwingUtilities.invokeLater(() -> ScreenManager.show(new DashboardScreen()));
            } else {
                SwingUtilities.invokeLater(() -> {
                    form.getStatusLabel().setForeground(Color.RED);
                    form.getStatusLabel().setText("Server error: " + code);
                });
            }

            conn.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
            SwingUtilities.invokeLater(() -> form.getStatusLabel().setText("Connection error."));
        }
    }

    private void deleteEntryAsync() {
        new Thread(() -> {
            try {
                String token = AppState.getInstance().getJwtToken();
                if (token == null || token.isEmpty()) {
                    System.out.println(">>> No JWT, cannot delete");
                    return;
                }

                String url = Constants.BASE_URL + "/api/data-entries/" + editingEntry.getId();

                java.net.URL u = new java.net.URL(url);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) u.openConnection();

                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Authorization", "Bearer " + token);

                int code = conn.getResponseCode();
                if (code == 200 || code == 204) {
                    AppState.getInstance().getEntries().remove(editingEntry);

                    SwingUtilities.invokeLater(() -> ScreenManager.show(new DashboardScreen()));
                } else {
                    SwingUtilities.invokeLater(() -> form.getStatusLabel()
                            .setText("Failed to delete. Server code: " + code));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> form.getStatusLabel().setText("Delete error."));
            }
        }).start();
    }
}
