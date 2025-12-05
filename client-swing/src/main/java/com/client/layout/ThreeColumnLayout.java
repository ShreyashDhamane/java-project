package com.client.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;

public class ThreeColumnLayout implements LayoutManager2 {

    private final double col1Ratio;
    private final double col2Ratio;
    private final double col3Ratio;

    public ThreeColumnLayout() {
        this(0.50, 0.25, 0.25);
    }

    public ThreeColumnLayout(double col1Ratio, double col2Ratio, double col3Ratio) {
        this.col1Ratio = col1Ratio;
        this.col2Ratio = col2Ratio;
        this.col3Ratio = col3Ratio;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) { }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) { }

    @Override
    public void removeLayoutComponent(Component comp) { }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Insets in = parent.getInsets();
        int width = 0, height = 0;

        for (Component c : parent.getComponents()) {
            if (!c.isVisible()) continue;
            Dimension d = c.getPreferredSize();
            width = Math.max(width, d.width);
            height = Math.max(height, d.height);
        }

        return new Dimension(in.left + in.right + width, in.top + in.bottom + height);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        Dimension d = preferredLayoutSize(target);
        return new Dimension(Integer.MAX_VALUE, d.height);
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets in = parent.getInsets();
        Component[] comps = parent.getComponents();

        if (comps.length == 0) return;

        int totalWidth = parent.getWidth() - in.left - in.right;
        int totalHeight = parent.getHeight() - in.top - in.bottom;

        if (totalWidth <= 0 || totalHeight <= 0) return;

        int col1 = (int) Math.round(totalWidth * col1Ratio);
        int col2 = (int) Math.round(totalWidth * col2Ratio);
        int col3 = totalWidth - col1 - col2;

        int x = in.left, y = in.top;

        if (comps.length > 0) comps[0].setBounds(new Rectangle(x, y, col1, totalHeight));
        if (comps.length > 1) comps[1].setBounds(new Rectangle(x + col1, y, col2, totalHeight));
        if (comps.length > 2) comps[2].setBounds(new Rectangle(x + col1 + col2, y, col3, totalHeight));
    }

    @Override public float getLayoutAlignmentX(Container t) { return 0; }
    @Override public float getLayoutAlignmentY(Container t) { return 0; }
    @Override public void invalidateLayout(Container t) { }
}
