package com.client.components.category;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.client.constants.UIFonts;
import com.client.model.ExpenseCategory;
import com.client.screens.category.ManageCategories;

public class CategoryListRenderer {

    private final ManageCategories scr;

    public CategoryListRenderer(ManageCategories scr) {
        this.scr = scr;
    }

    public void rebuildList(java.util.List<ExpenseCategory> categories) {
        scr.listPanel.removeAll();

        if (categories.isEmpty()) {
            JLabel empty = new JLabel("No categories yet. Tap + to add.", SwingConstants.CENTER);
            empty.setForeground(Color.WHITE);
            empty.setFont(UIFonts.TEXT);
            empty.setBorder(new EmptyBorder(10,10,10,10));
            scr.listPanel.add(empty);
        } else {
            for (ExpenseCategory cat : categories) {
                CategoryRow row = new CategoryRow(scr, cat);
                scr.listPanel.add(row);
                scr.listPanel.add(Box.createVerticalStrut(10));
            }
        }

        scr.listPanel.revalidate();
        scr.listPanel.repaint();
    }
}
