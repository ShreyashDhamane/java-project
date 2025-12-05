package com.client.screens.addentry;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.client.components.addentry.AddEntryForm;
import com.client.components.addentry.AddEntryLayout;
import com.client.components.addentry.AddEntryPreloader;
import com.client.core.BasePanel;
import com.client.model.DataEntry;

// screen to add or edit a data entry (expense/income)
public class AddDataEntryScreen extends BasePanel {

    private final DataEntry editingEntry;

    private final AddEntryForm form;
    private final AddEntryController controller;

    // default constructor for adding new entry
    public AddDataEntryScreen() {
        this(null);
    }

    // constructor for editing existing entry
    public AddDataEntryScreen(DataEntry entry) {
        this.editingEntry = entry;

        setLayout(null); // manual layout
        setOpaque(false); // gradient background

        boolean isEditMode = (editingEntry != null);

        // 1) Build form (all components)
        this.form = new AddEntryForm();
        this.form.build(this, isEditMode);

        // 2) Preload existing entry (if editing)
        if (isEditMode) {
            AddEntryPreloader.preload(form, editingEntry);
        }

        // 3) Wire all actions (back, submit, delete)
        this.controller = new AddEntryController(this, form, editingEntry);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        AddEntryLayout.applyLayout(this, form);
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
