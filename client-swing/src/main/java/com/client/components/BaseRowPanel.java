package com.client.components;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.client.layout.ThreeColumnLayout;

public class BaseRowPanel extends JPanel {

    private final int fixedHeight;

    public BaseRowPanel(int fixedHeight, boolean opaque, Color background) {
        super(new ThreeColumnLayout());
        this.fixedHeight = fixedHeight;

        setOpaque(opaque);
        if (background != null) setBackground(background);

        setBorder(new EmptyBorder(4, 10, 4, 10));
        setAlignmentX(LEFT_ALIGNMENT);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.height = fixedHeight;
        return d;
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension d = getPreferredSize();
        d.width = Integer.MAX_VALUE;
        return d;
    }
}
