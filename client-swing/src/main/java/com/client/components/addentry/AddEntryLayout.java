package com.client.components.addentry;

import com.client.core.BasePanel;

public class AddEntryLayout {

    private AddEntryLayout() {
        // utility class
    }

    public static void applyLayout(BasePanel panel, AddEntryForm form) {
        int w = panel.getWidth();
        int labelWidth = 120;
        int margin = 30;
        int fieldH = 40;
        int spacing = 20;

        // Back button
        form.getBackButton().setBounds(margin, margin, 100, fieldH);

        int y = margin + 60;

        // Type
        form.getTypeLabel().setBounds(margin, y, labelWidth, fieldH);
        form.getIncomeBtn().setBounds(margin + labelWidth, y, 120, fieldH);
        form.getExpenseBtn().setBounds(margin + labelWidth + 130, y, 120, fieldH);

        y += fieldH + spacing;

        // Date
        form.getDateLabel().setBounds(margin, y, labelWidth, fieldH);
        form.getDatePicker().setBounds(margin + labelWidth, y, w - margin * 2 - labelWidth, fieldH);

        y += fieldH + spacing;

        // Category
        form.getCategoryLabel().setBounds(margin, y, labelWidth, fieldH);
        form.getCategoryDropdown().setBounds(margin + labelWidth, y, w - margin * 2 - labelWidth, fieldH);

        y += fieldH + spacing;

        // Payment Type
        form.getPaymentLabel().setBounds(margin, y, labelWidth, fieldH);
        form.getPaymentTypeDropdown().setBounds(margin + labelWidth, y, w - margin * 2 - labelWidth, fieldH);

        y += fieldH + spacing;

        // Note
        form.getNoteLabel().setBounds(margin, y, labelWidth, fieldH);
        form.getNoteField().setBounds(margin + labelWidth, y, w - margin * 2 - labelWidth, fieldH);

        y += fieldH + spacing;

        // Amount
        form.getAmountLabel().setBounds(margin, y, labelWidth, fieldH);
        form.getAmountField().setBounds(margin + labelWidth, y, w - margin * 2 - labelWidth, fieldH);

        y += fieldH + spacing;

        // Submit
        form.getSubmitButton().setBounds(margin, y, w - 2 * margin, fieldH);

        // Status
        y += fieldH + 10;
        form.getStatusLabel().setBounds(margin, y, w - 2 * margin, fieldH);

        // Delete (if present – edit mode)
        if (form.getDeleteButton() != null) {
            int deleteY = y; // matches original logic
            form.getDeleteButton().setBounds(margin, deleteY, w - 2 * margin, fieldH);
        }
    }
}
