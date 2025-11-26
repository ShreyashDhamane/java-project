package com.client.screens;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import com.client.constants.Constants;
import com.client.constants.UIColors;
import com.client.constants.UIFonts;
import com.client.constants.UIStyle;
import com.client.core.AppState;
import com.client.core.BasePanel;
import com.client.core.ScreenManager;
import com.client.model.DataEntry;
import com.client.model.ExpenseCategory;

// screen to add or edit a data entry (expense/income)
public class AddDataEntryScreen extends BasePanel {

    private JButton backButton;
    private JButton submitButton;

    private JRadioButton incomeBtn;
    private JRadioButton expenseBtn;

    private JComboBox<String> categoryDropdown;
    private JComboBox<String> paymentTypeDropdown;

    private JSpinner datePicker;

    private JTextField noteField;
    private JTextField amountField;

    private JLabel statusLabel; // for on screen error messages if fields are invalid

    private DataEntry editingEntry = null;

    // default constructire for adding new entry
    public AddDataEntryScreen() {
        this.editingEntry = null; // no entry being edited

        setLayout(null); // null layout for manual positioning
        setOpaque(false); // transparent background
        
        createComponents();
    }

    // constructor for editing existing entry
    public AddDataEntryScreen(DataEntry entry) {
        this.editingEntry = entry;

        setLayout(null); // same as above
        setOpaque(false);
        
        createComponents();
        // once components are created, preload data
        preloadEntryData(entry);
    }

    // preload existing entry data into form fields for editing screen
    private void preloadEntryData(DataEntry e) {
        // entry type
        if ("Income".equalsIgnoreCase(e.getType())) {
            incomeBtn.setSelected(true);
        } else {
            expenseBtn.setSelected(true);
        }

        // Date field parse, and set
        try {
            java.util.Date d = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(e.getDate());
            datePicker.setValue(d);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // Category of expense/income
        categoryDropdown.setSelectedItem(e.getCategory());

        // Payment type for entry
        paymentTypeDropdown.setSelectedItem(e.getPaymentType());

        // Note field
        noteField.setText(e.getNote());

        // Amount field
        amountField.setText(String.valueOf(e.getAmount()));

        // Change button text from "Save" to "Update"
        submitButton.setText("Update");
    }

    // create and add all UI components to panel
    private void createComponents() {
        // back button to dashboard
        backButton = UIStyle.createPrimaryButton("Back");
        backButton.addActionListener(e -> ScreenManager.show(new DashboardScreen()));
        
        add(backButton);

        // type field, income/expense toggle
        JLabel typeLabel = new JLabel("Type:");

        typeLabel.setFont(UIFonts.TEXT_BOLD);
        typeLabel.setForeground(Color.WHITE);
        
        add(typeLabel);

        incomeBtn = createToggle("Income", UIColors.PRIMARY, Color.WHITE);
        expenseBtn = createToggle("Expense", Color.RED, Color.WHITE);
        
        ButtonGroup group = new ButtonGroup();
        
        group.add(incomeBtn);
        group.add(expenseBtn);
        
        expenseBtn.setSelected(true); // default selection
        
        add(expenseBtn);
        add(incomeBtn);

        // date picker
        JLabel dateLabel = new JLabel("Date:");
        
        dateLabel.setFont(UIFonts.TEXT_BOLD);
        dateLabel.setForeground(Color.WHITE);
        
        add(dateLabel);

        datePicker = new JSpinner(new SpinnerDateModel());
        datePicker.setFont(UIFonts.TEXT);
        
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(datePicker, "yyyy-MM-dd");
        
        datePicker.setEditor(dateEditor);
        datePicker.getEditor().getComponent(0).setBackground(new Color(35,45,65));
        datePicker.getEditor().getComponent(0).setForeground(Color.WHITE);

        add(datePicker);

        // category dropdown
        JLabel categoryLabel = new JLabel("Category:");
        
        categoryLabel.setFont(UIFonts.TEXT_BOLD);
        categoryLabel.setForeground(Color.WHITE);
        
        add(categoryLabel);
        // all categories are stored in app state using thread /parallel fetch during app login
        List<ExpenseCategory> categoryList = AppState.getInstance().getCategories();

        // extract category names for dropdown
        String[] categoryNames = categoryList.stream()
                .map(ExpenseCategory::getName)
                .toArray(String[]::new);

        categoryDropdown = new JComboBox<>(categoryNames);
        categoryDropdown.setFont(UIFonts.TEXT);
        
        add(categoryDropdown);
        // style dropdown customly for dark theme
        UIStyle.styleDarkDropdown(categoryDropdown);

        // payment type dropdown
        JLabel paymentLabel = new JLabel("Payment Type:");
        
        paymentLabel.setFont(UIFonts.TEXT_BOLD);
        paymentLabel.setForeground(Color.WHITE);
        
        add(paymentLabel);
        // payment list is fixed for now
        List<String> paymentTypes = Arrays.asList("Cash", "Bank Transfer", "Credit Card", "Debit Card");
        
        paymentTypeDropdown = new JComboBox<>(paymentTypes.toArray(new String[0]));
        paymentTypeDropdown.setFont(UIFonts.TEXT);
        
        add(paymentTypeDropdown);
        // style dropdown customly for dark theme
        UIStyle.styleDarkDropdown(paymentTypeDropdown);


        // note field
        JLabel noteLabel = new JLabel("Note:");
        
        noteLabel.setFont(UIFonts.TEXT_BOLD);
        noteLabel.setForeground(Color.WHITE);
        
        add(noteLabel);

        noteField = new JTextField();
        noteField.setFont(UIFonts.TEXT);
        // custom style for dark theme
        UIStyle.styleTextField(noteField);
        
        add(noteField);

        // amount field
        JLabel amountLabel = new JLabel("Amount:");
        
        amountLabel.setFont(UIFonts.TEXT_BOLD);
        amountLabel.setForeground(Color.WHITE);
        
        add(amountLabel);

        amountField = new JTextField();
        amountField.setFont(UIFonts.TEXT);
        // custom style for dark theme
        
        UIStyle.styleTextField(amountField);
        
        add(amountField);

        // status label for error messages
        statusLabel = new JLabel("");

        statusLabel.setFont(UIFonts.TEXT_BOLD);
        statusLabel.setForeground(Color.RED);
        
        add(statusLabel);

        // submit button
        submitButton = UIStyle.createPrimaryButton("Save");
        submitButton.addActionListener(this::submitAction);

        add(submitButton);
    }

    // helper to create income/expense toggle buttons
    private JRadioButton createToggle(String text, Color bg, Color fg) {
        JRadioButton btn = new JRadioButton(text);

        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(UIFonts.TEXT_BOLD);
        btn.setFocusPainted(false);
        
        return btn;
    }

    // function to handle form submission
    private void submitAction(ActionEvent e) {
        // first validate fields
        statusLabel.setText("");

        // gather form data
        String type = incomeBtn.isSelected() ? "Income" : "Expense";
        Object dateValue = datePicker.getValue();
        String category = (String) categoryDropdown.getSelectedItem();
        String paymentType = (String) paymentTypeDropdown.getSelectedItem();
        String note = noteField.getText().trim();
        String amountText = amountField.getText().trim();

        // validate amount
        if (amountText.isEmpty()) {
            statusLabel.setText("Amount cannot be empty");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountText);
            if (amount <= 0) {
                // amount must be positive
                statusLabel.setText("Amount must be greater than 0");
                return;
            }
        } catch (NumberFormatException ex) {
            statusLabel.setText("Invalid amount");
            return;
        }

        // prepare JSON payload to send to backend
        String username = AppState.getInstance().getUsername();
        String dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format((java.util.Date) dateValue);

        // JSON payload for backend
        String json = String.format(
                "{\"username\":\"%s\",\"type\":\"%s\",\"date\":\"%s\",\"category\":\"%s\",\"note\":\"%s\",\"amount\":%d,\"paymentType\":\"%s\"}",
                username, type, dateStr, category, note, amount, paymentType
        );

        // send data to backend in a separate thread to avoid blocking UI
        // we can add loader but not needed for now
        // if error thrown we can later show error message on status label
        new Thread(() -> {
            try {
                String urlStr;
                boolean isEdit = (editingEntry != null);

                // ad and edit have different endpoints
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

                try (java.io.OutputStream os = conn.getOutputStream()) {
                    os.write(json.getBytes());
                }

                int code = conn.getResponseCode();
                // 200 OK
                if (code == 200) {
                    // update AppState, as we have new/updated entry
                    if (isEdit) {
                        // update existing entry in app state
                        editingEntry.setType(type);
                        editingEntry.setDate(dateStr);
                        editingEntry.setCategory(category);
                        editingEntry.setPaymentType(paymentType);
                        editingEntry.setNote(note);
                        editingEntry.setAmount(amount);

                    } else {
                        // Parse new entry with ID from backend if needed
                        DataEntry newEntry = new DataEntry(username, type, dateStr, category, note, amount, paymentType);
                        AppState.getInstance().addEntry(newEntry);
                    }
                    // go back to dashboard on success
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        ScreenManager.show(new DashboardScreen());
                    });
                } else {
                    // show error message
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Server error: " + code);
                }
                // disconnect
                conn.disconnect();

            } catch (Exception ex) {
                ex.printStackTrace();
                statusLabel.setText("Connection error.");
            }
        }).start();
    }

    // layout components manually due to null layout set
    @Override
    public void doLayout() {
        // manually position components
        super.doLayout();
        int w = getWidth(); // panel width
        int labelWidth = 120;
        int margin = 30;
        int fieldH = 40;
        int spacing = 20;
        // back button position set
        backButton.setBounds(margin, margin, 100, fieldH);

        int y = margin + 60;

        // type field positions
        getComponentByName("Type:").setBounds(margin, y, labelWidth, fieldH);
        
        incomeBtn.setBounds(margin + labelWidth, y, 120, fieldH);
        expenseBtn.setBounds(margin + labelWidth + 130, y, 120, fieldH);
        
        y += fieldH + spacing;

        // date field positions
        getComponentByName("Date:").setBounds(margin, y, labelWidth, fieldH);

        datePicker.setBounds(margin + labelWidth, y, w - margin * 2 - labelWidth, fieldH);

        y += fieldH + spacing;

        // Category field positions
        getComponentByName("Category:").setBounds(margin, y, labelWidth, fieldH);

        categoryDropdown.setBounds(margin + labelWidth, y, w - margin * 2 - labelWidth, fieldH);

        y += fieldH + spacing;

        // Payment Type field positions
        getComponentByName("Payment Type:").setBounds(margin, y, labelWidth, fieldH);

        paymentTypeDropdown.setBounds(margin + labelWidth, y, w - margin * 2 - labelWidth, fieldH);

        y += fieldH + spacing;

        // Note field positions
        getComponentByName("Note:").setBounds(margin, y, labelWidth, fieldH);

        noteField.setBounds(margin + labelWidth, y, w - margin * 2 - labelWidth, fieldH);

        y += fieldH + spacing;

        // Amount field positions
        getComponentByName("Amount:").setBounds(margin, y, labelWidth, fieldH);

        amountField.setBounds(margin + labelWidth, y, w - margin * 2 - labelWidth, fieldH);

        y += fieldH + spacing;

        // Submit
        submitButton.setBounds(margin, y, w - 2 * margin, fieldH);

        // Status
        y += fieldH + 10;
        statusLabel.setBounds(margin, y, w - 2 * margin, fieldH);
    }


    // Helper to find JLabel by its text so we can position it easily
    // works because all labels have unique text and its a small form
    private JLabel getComponentByName(String name) {
        for (var c : getComponents()) { // iterate all components, check for JLabel with matching text
            if (c instanceof JLabel && ((JLabel)c).getText().equals(name)) {
                return (JLabel)c;
            }
        }
        return null;
    }

    // custom paint to add gradient background dark blue
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color startColor = new Color(24, 40, 72);
        Color endColor = new Color(75, 108, 183);
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(0, 0, endColor, 0, getHeight(), startColor));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
