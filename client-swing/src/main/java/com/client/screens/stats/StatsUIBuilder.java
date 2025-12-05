package com.client.screens.stats;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.client.components.ArrowButton;
import com.client.components.BottomNavigationBar;
import com.client.components.CustomScrollBar;
import com.client.components.stats.PieChartPanel;
import com.client.components.stats.StatsScreenComponents;
import com.client.components.timeseries.TimeSeriesChartPanel;
import com.client.constants.UIFonts;
import com.client.constants.UIStyle;

public class StatsUIBuilder {

    public void build(StatsScreen scr, StatsState state) {

        // Top tabs
        scr.statsTabLabel = StatsScreenComponents.createTopTabLabel("Stats", !state.isShowTimeSeries());
        scr.timeSeriesTabLabel = StatsScreenComponents.createTopTabLabel("Timeline", state.isShowTimeSeries());
        scr.add(scr.statsTabLabel);
        scr.add(scr.timeSeriesTabLabel);

        // Time period dropdown (Month / Year / Total)
        scr.timePeriodDropdown = new javax.swing.JComboBox<>(new String[]{"Month", "Year", "Total"});
        scr.timePeriodDropdown.setFont(UIFonts.TEXT);
        UIStyle.styleDarkDropdown(scr.timePeriodDropdown);
        scr.timePeriodDropdown.setFocusable(false);
        scr.timePeriodDropdown.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        scr.timePeriodDropdown.setBackground(new Color(35, 45, 65));
        scr.timePeriodDropdown.setForeground(Color.WHITE);

        // If editor exists, remove its border/opacity (safe-guard)
        if (scr.timePeriodDropdown.getEditor() != null &&
            scr.timePeriodDropdown.getEditor().getEditorComponent() instanceof JTextField textField) {

            textField.setOpaque(false);
            textField.setBorder(null);
        }

        scr.timePeriodDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                lbl.setOpaque(true);
                lbl.setForeground(Color.WHITE);
                lbl.setBackground(isSelected ? new Color(60, 70, 90) : new Color(35, 45, 65));
                lbl.setFont(UIFonts.TEXT);
                lbl.setBorder(new javax.swing.border.EmptyBorder(6, 12, 6, 12));

                return lbl;
            }
        });

        scr.add(scr.timePeriodDropdown);

        // Period selector arrows + label
        scr.prevPeriodBtn = new ArrowButton(true);
        scr.nextPeriodBtn = new ArrowButton(false);

        scr.periodLabel = new JLabel("", javax.swing.SwingConstants.CENTER);
        scr.periodLabel.setFont(UIFonts.TEXT_BOLD);
        scr.periodLabel.setForeground(Color.WHITE);

        scr.add(scr.prevPeriodBtn);
        scr.add(scr.nextPeriodBtn);
        scr.add(scr.periodLabel);

        // Income / Expense toggles
        scr.incomeToggleButton = com.client.components.stats.StatsScreenComponents.createToggleButton("Income");
        scr.expenseToggleButton = com.client.components.stats.StatsScreenComponents.createToggleButton("Expense");

        javax.swing.ButtonGroup typeGroup = new javax.swing.ButtonGroup();
        typeGroup.add(scr.incomeToggleButton);
        typeGroup.add(scr.expenseToggleButton);

        // Default: Expense
        scr.expenseToggleButton.setSelected(true);
        scr.incomeToggleButton.setSelected(false);

        scr.add(scr.incomeToggleButton);
        scr.add(scr.expenseToggleButton);

        // Pie chart
        scr.pieChartPanel = new PieChartPanel();
        scr.add(scr.pieChartPanel);

        // Time series chart
        scr.timeSeriesChartPanel = new TimeSeriesChartPanel();
        scr.add(scr.timeSeriesChartPanel);

        // Legend list
        scr.legendListPanel = new JPanel();
        scr.legendListPanel.setOpaque(false);
        scr.legendListPanel.setLayout(new BoxLayout(scr.legendListPanel, BoxLayout.Y_AXIS));

        scr.legendScrollPane = new JScrollPane(scr.legendListPanel);
        scr.legendScrollPane.setBorder(null);
        scr.legendScrollPane.setOpaque(false);
        scr.legendScrollPane.getViewport().setOpaque(false);
        scr.legendScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scr.legendScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scr.legendScrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());

        scr.add(scr.legendScrollPane);

        // Bottom navigation
        scr.bottomNav = new BottomNavigationBar("Charts");
        scr.add(scr.bottomNav);
    }
}
