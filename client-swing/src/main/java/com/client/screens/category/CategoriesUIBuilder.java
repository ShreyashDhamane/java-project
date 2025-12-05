package com.client.screens.category;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.client.components.BottomNavigationBar;
import com.client.components.CustomScrollBar;
import com.client.components.category.CategoryUIFactory;
import com.client.constants.UIFonts;

public class CategoriesUIBuilder {

    public void build(ManageCategories scr) {

        // Title
        scr.titleLabel = new JLabel("Categories");
        scr.titleLabel.setFont(UIFonts.TITLE);
        scr.titleLabel.setForeground(new Color(245,245,255));
        scr.add(scr.titleLabel);

        // List panel
        scr.listPanel = new JPanel();
        scr.listPanel.setOpaque(false);
        scr.listPanel.setLayout(new BoxLayout(scr.listPanel, BoxLayout.Y_AXIS));

        scr.scrollPane = new JScrollPane(scr.listPanel);
        scr.scrollPane.setOpaque(false);
        scr.scrollPane.getViewport().setOpaque(false);
        scr.scrollPane.setBorder(null);
        scr.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scr.scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scr.scrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());
        scr.add(scr.scrollPane);

        // Floating Add Button
        scr.addButton = CategoryUIFactory.createAddButton();
        scr.add(scr.addButton);

        // Bottom Navigation
        scr.bottomNav = new BottomNavigationBar("Settings");
        scr.add(scr.bottomNav);
    }

    public void layout(ManageCategories scr) {

        int w = scr.getWidth();
        int h = scr.getHeight();
        int padding = 20;
        int navHeight = 60;

        scr.titleLabel.setBounds(20, 20, 400, 40);

        int listTop = 100;
        int listHeight = h - listTop - navHeight - 90;
        if (listHeight < 80) listHeight = 80;

        scr.scrollPane.setBounds(padding, listTop, w - 2 * padding, listHeight);

        int fabSize = 52;
        scr.addButton.setBounds(w - padding - fabSize, h - navHeight - padding - fabSize, fabSize, fabSize);

        scr.bottomNav.setBounds(0, h - navHeight, w, navHeight);
        scr.bottomNav.doLayout();
    }
}
