package com.client.components.dashboard;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.client.components.BaseRowPanel;
import com.client.constants.UIColors;
import com.client.constants.UIFonts;
import com.client.core.ScreenManager;
import com.client.model.DataEntry;
import com.client.screens.addentry.AddDataEntryScreen;

public class DashboardRowFactory {

    private static final java.time.format.DateTimeFormatter DATE_OUT =
            java.time.format.DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");

    // DAY HEADER
    public static BaseRowPanel createDayHeaderRow(DayGroup g) {

        Color bg = new Color(20, 28, 45, 220);
        BaseRowPanel row = new BaseRowPanel(34, true, bg);

        JLabel left = new JLabel(g.date.format(DATE_OUT));
        left.setFont(UIFonts.TEXT_BOLD);
        left.setForeground(Color.WHITE);
        left.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel mid = new JLabel("Income: " + g.totalIncome);
        mid.setFont(UIFonts.TEXT);
        mid.setForeground(UIColors.PRIMARY);
        mid.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel right = new JLabel("Expense: " + g.totalExpense);
        right.setFont(UIFonts.TEXT);
        right.setForeground(new Color(255, 80, 80));
        right.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(left);
        row.add(mid);
        row.add(right);

        return row;
    }

    // TRANSACTION ROW
    public static BaseRowPanel createTransactionRow(DataEntry e) {

        BaseRowPanel row = new BaseRowPanel(28, false, null);

        Color hoverColor = new Color(255, 255, 255, 30);
        Color normalColor = new Color(0, 0, 0, 0);

        row.setBackground(normalColor);
        row.setOpaque(false);

        StringBuilder sb = new StringBuilder();
        if (e.getCategory() != null) sb.append(e.getCategory());
        if (e.getNote() != null && !e.getNote().isEmpty()) {
            if (sb.length() > 0) sb.append(" — ");
            sb.append(e.getNote());
        }

        JLabel left = new JLabel(sb.toString());
        left.setFont(UIFonts.TEXT);
        left.setForeground(Color.WHITE);
        left.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel mid = new JLabel(e.getPaymentType() != null ? e.getPaymentType() : "");
        mid.setFont(UIFonts.TEXT);
        mid.setForeground(new Color(200, 200, 200));
        mid.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel right = new JLabel(String.valueOf(e.getAmount()));
        right.setFont(UIFonts.TEXT_BOLD);
        right.setHorizontalAlignment(SwingConstants.RIGHT);

        if ("Income".equalsIgnoreCase(e.getType()))
            right.setForeground(UIColors.PRIMARY);
        else
            right.setForeground(new Color(255, 80, 80));

        row.add(left);
        row.add(mid);
        row.add(right);

        row.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent ev) {
                row.setOpaque(true);
                row.setBackground(hoverColor);
                row.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                row.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent ev) {
                row.setOpaque(false);
                row.setBackground(normalColor);
                row.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                row.repaint();
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent ev) {
                ScreenManager.show(new AddDataEntryScreen(e));
            }
        });

        return row;
    }
}
