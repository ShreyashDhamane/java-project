package com.client.components.addentry;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import com.client.constants.UIColors;
import com.client.constants.UIFonts;
import com.client.constants.UIStyle;
import com.client.core.AppState;
import com.client.core.BasePanel;
import com.client.model.ExpenseCategory;

public class AddEntryForm {

    private JButton backButton;
    private JButton submitButton;
    private JButton deleteButton;

    private JRadioButton incomeBtn;
    private JRadioButton expenseBtn;

    private JComboBox<String> categoryDropdown;
    private JComboBox<String> paymentTypeDropdown;

    private JSpinner datePicker;

    private JTextField noteField;
    private JTextField amountField;

    private JLabel statusLabel;

    private JLabel typeLabel;
    private JLabel dateLabel;
    private JLabel categoryLabel;
    private JLabel paymentLabel;
    private JLabel noteLabel;
    private JLabel amountLabel;

    public void build(BasePanel panel, boolean isEditMode) {
        // back button
        backButton = UIStyle.createPrimaryButton("Back");
        panel.add(backButton);

        // Type
        typeLabel = new JLabel("Type:");
        typeLabel.setFont(UIFonts.TEXT_BOLD);
        typeLabel.setForeground(Color.WHITE);
        panel.add(typeLabel);

        incomeBtn = createToggle("Income", UIColors.PRIMARY, Color.WHITE);
        expenseBtn = createToggle("Expense", Color.RED, Color.WHITE);

        var group = new javax.swing.ButtonGroup();
        group.add(incomeBtn);
        group.add(expenseBtn);
        expenseBtn.setSelected(true); // default

        panel.add(expenseBtn);
        panel.add(incomeBtn);

        // Date
        dateLabel = new JLabel("Date:");
        dateLabel.setFont(UIFonts.TEXT_BOLD);
        dateLabel.setForeground(Color.WHITE);
        panel.add(dateLabel);

        datePicker = new JSpinner(new SpinnerDateModel());
        datePicker.setFont(UIFonts.TEXT);

        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(datePicker, "yyyy-MM-dd");
        datePicker.setEditor(dateEditor);
        datePicker.getEditor().getComponent(0).setBackground(new Color(35, 45, 65));
        datePicker.getEditor().getComponent(0).setForeground(Color.WHITE);

        panel.add(datePicker);

        // Category
        categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(UIFonts.TEXT_BOLD);
        categoryLabel.setForeground(Color.WHITE);
        panel.add(categoryLabel);

        List<ExpenseCategory> categoryList = AppState.getInstance().getCategories();
        String[] categoryNames = categoryList.stream()
                .map(ExpenseCategory::getName)
                .toArray(String[]::new);

        categoryDropdown = new JComboBox<>(categoryNames);
        categoryDropdown.setFont(UIFonts.TEXT);
        panel.add(categoryDropdown);
        UIStyle.styleDarkDropdown(categoryDropdown);

        // Payment Type
        paymentLabel = new JLabel("Payment Type:");
        paymentLabel.setFont(UIFonts.TEXT_BOLD);
        paymentLabel.setForeground(Color.WHITE);
        panel.add(paymentLabel);

        List<String> paymentTypes = Arrays.asList("Cash", "Bank Transfer", "Credit Card", "Debit Card");
        paymentTypeDropdown = new JComboBox<>(paymentTypes.toArray(new String[0]));
        paymentTypeDropdown.setFont(UIFonts.TEXT);
        panel.add(paymentTypeDropdown);
        UIStyle.styleDarkDropdown(paymentTypeDropdown);

        // Note
        noteLabel = new JLabel("Note:");
        noteLabel.setFont(UIFonts.TEXT_BOLD);
        noteLabel.setForeground(Color.WHITE);
        panel.add(noteLabel);

        noteField = new JTextField();
        noteField.setFont(UIFonts.TEXT);
        UIStyle.styleTextField(noteField);
        panel.add(noteField);

        // Amount
        amountLabel = new JLabel("Amount:");
        amountLabel.setFont(UIFonts.TEXT_BOLD);
        amountLabel.setForeground(Color.WHITE);
        panel.add(amountLabel);

        amountField = new JTextField();
        amountField.setFont(UIFonts.TEXT);
        UIStyle.styleTextField(amountField);
        panel.add(amountField);

        // Status
        statusLabel = new JLabel("");
        statusLabel.setFont(UIFonts.TEXT_BOLD);
        statusLabel.setForeground(Color.RED);
        panel.add(statusLabel);

        // Submit
        submitButton = UIStyle.createPrimaryButton("Save");
        panel.add(submitButton);

        // Delete (only in edit mode)
        if (isEditMode) {
            deleteButton = UIStyle.createPrimaryButton("Delete");
            deleteButton.setBackground(new Color(220, 80, 80));
            panel.add(deleteButton);
        }
    }

    private static JRadioButton createToggle(String text, Color bg, Color fg) {
        JRadioButton btn = new JRadioButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(UIFonts.TEXT_BOLD);
        btn.setFocusPainted(false);
        return btn;
    }

    // Getters for controller/layout/preloader

    public JButton getBackButton() {
        return backButton;
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JRadioButton getIncomeBtn() {
        return incomeBtn;
    }

    public JRadioButton getExpenseBtn() {
        return expenseBtn;
    }

    public JComboBox<String> getCategoryDropdown() {
        return categoryDropdown;
    }

    public JComboBox<String> getPaymentTypeDropdown() {
        return paymentTypeDropdown;
    }

    public JSpinner getDatePicker() {
        return datePicker;
    }

    public JTextField getNoteField() {
        return noteField;
    }

    public JTextField getAmountField() {
        return amountField;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JLabel getTypeLabel() {
        return typeLabel;
    }

    public JLabel getDateLabel() {
        return dateLabel;
    }

    public JLabel getCategoryLabel() {
        return categoryLabel;
    }

    public JLabel getPaymentLabel() {
        return paymentLabel;
    }

    public JLabel getNoteLabel() {
        return noteLabel;
    }

    public JLabel getAmountLabel() {
        return amountLabel;
    }
}
