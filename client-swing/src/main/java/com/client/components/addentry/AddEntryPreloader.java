package com.client.components.addentry;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.client.model.DataEntry;

public class AddEntryPreloader {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    private AddEntryPreloader() {
        // utility
    }

    public static void preload(AddEntryForm form, DataEntry e) {
        if (e == null) {
            return;
        }

        // Type
        if ("Income".equalsIgnoreCase(e.getType())) {
            form.getIncomeBtn().setSelected(true);
        } else {
            form.getExpenseBtn().setSelected(true);
        }

        // Date
        try {
            Date d = SDF.parse(e.getDate());
            form.getDatePicker().setValue(d);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Category & Payment
        form.getCategoryDropdown().setSelectedItem(e.getCategory());
        form.getPaymentTypeDropdown().setSelectedItem(e.getPaymentType());

        // Note & Amount
        form.getNoteField().setText(e.getNote());
        form.getAmountField().setText(String.valueOf(e.getAmount()));

        // Button text
        form.getSubmitButton().setText("Update");
    }
}
