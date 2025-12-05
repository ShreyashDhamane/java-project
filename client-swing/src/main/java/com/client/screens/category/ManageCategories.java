package com.client.screens.category;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.client.components.BottomNavigationBar;
import com.client.components.category.CategoryListRenderer;
import com.client.core.BasePanel;
import com.client.model.ExpenseCategory;

public class ManageCategories extends BasePanel {

    // UI elements populated via builder
    public JLabel titleLabel;
    public JScrollPane scrollPane;
    public JPanel listPanel;
    public JButton addButton;
    public BottomNavigationBar bottomNav;

    // Data
    public final List<ExpenseCategory> categories = new ArrayList<>();

    // Subcomponents
    private CategoriesController controller;
    private CategoryListRenderer renderer;

    public ManageCategories() {
        setLayout(null);
        setOpaque(false);

        // Build UI
        new CategoriesUIBuilder().build(this);

        // Controller + Renderer
        controller = new CategoriesController(this);
        renderer = new CategoryListRenderer(this);

        // Load categories initially
        controller.loadCategoriesAsync();

        // FAB click
        addButton.addActionListener(e -> controller.showAddDialog());
    }

    @Override
    public void doLayout() {
        super.doLayout();
        new CategoriesUIBuilder().layout(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(new GradientPaint(
            0, 0, new Color(75,108,183),
            0, getHeight(), new Color(24,40,72)
        ));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    // Called by controller when categories are refreshed
    public void refreshCategoryList() {
        renderer.rebuildList(categories);
    }
}
