package com.client.components.category;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.client.core.AppState;
import com.client.model.ExpenseCategory;
import com.client.screens.category.CategoriesController;
import com.client.screens.category.ManageCategories;

public class CategoryDialogs {

    public static void showAddDialog(ManageCategories scr, CategoriesController controller) {

        JTextField nameField = new JTextField();
        JTextField iconField = new JTextField();

        Object[] msg = { "Name:", nameField, "Icon (emoji):", iconField };

        int option = JOptionPane.showConfirmDialog(
            scr, msg, "Add Category",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String icon = iconField.getText().trim();
            if (name.isEmpty()) {
                showError(scr, "Name is required.");
                return;
            }

            controller.createCategoryAsync(
                name,
                icon.isEmpty() ? "💸" : icon,
                AppState.getInstance().getUsername()
            );
        }
    }

    public static void showEditDialog(
        ManageCategories scr,
        CategoriesController controller,
        ExpenseCategory cat
    ) {

        JTextField nameField = new JTextField(cat.getName());
        JTextField iconField = new JTextField(cat.getIcon());

        Object[] msg = { "Name:", nameField, "Icon (emoji):", iconField };

        int option = JOptionPane.showConfirmDialog(
                scr, msg, "Edit Category",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            cat.setName(nameField.getText().trim());
            cat.setIcon(iconField.getText().trim());
            controller.updateCategoryAsync(cat);
        }
    }

    public static void showError(ManageCategories scr, String text) {
        JOptionPane.showMessageDialog(scr, text, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
