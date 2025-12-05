package com.client.components.category;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.client.constants.UIFonts;
import com.client.model.ExpenseCategory;
import com.client.screens.category.CategoriesController;
import com.client.screens.category.ManageCategories;

public class CategoryRow extends JPanel {

    private final ExpenseCategory category;
    private final JButton editBtn;

    public CategoryRow(ManageCategories scr, ExpenseCategory cat) {
        this.category = cat;

        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(8,12,8,12));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));

        JLabel iconLabel = new JLabel(cat.getIcon());
        iconLabel.setFont(UIFonts.TITLE);
        iconLabel.setForeground(Color.WHITE);

        JLabel nameLabel = new JLabel("  " + cat.getName());
        nameLabel.setFont(UIFonts.TEXT_BOLD);
        nameLabel.setForeground(Color.WHITE);

        left.add(iconLabel);
        left.add(nameLabel);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.X_AXIS));

        editBtn = smallButton("Edit");
        right.add(editBtn);

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);

        setButtonsVisible(false);

        var hover = new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { setButtonsVisible(true); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { setButtonsVisible(false); }
        };

        this.addMouseListener(hover);
        left.addMouseListener(hover);
        right.addMouseListener(hover);
        editBtn.addMouseListener(hover);

        editBtn.addActionListener(e ->
            CategoryDialogs.showEditDialog(scr, new CategoriesController(scr), category)
        );
    }

    private JButton smallButton(String text) {
        JButton btn = new JButton(text);

        btn.setFont(UIFonts.TEXT);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(60,70,90));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(70,28));

        return btn;
    }

    private void setButtonsVisible(boolean b) { editBtn.setVisible(b); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(25,30,45,220));
        g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
        g2.dispose();
        super.paintComponent(g);
    }
}
