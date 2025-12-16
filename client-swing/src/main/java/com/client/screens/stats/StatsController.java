package com.client.screens.stats;

import com.client.components.stats.StatsViewRefresher;
import com.client.constants.StatsScreenConstants;

public class StatsController {

    private final StatsScreen scr;
    private final StatsState state;
    private final StatsViewRefresher refresher;

    public StatsController(
        StatsScreen scr,
        StatsState state,
        StatsViewRefresher refresher
    ) {
        this.scr = scr;
        this.state = state;
        this.refresher = refresher;
    }

    public void wireEvents() {

        // Tabs
        scr.statsTabLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                state.setShowTimeSeries(false);
                refresher.refresh();
            }
        });

        scr.timeSeriesTabLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                state.setShowTimeSeries(true);
                refresher.refresh();
            }
        });

        // Mode dropdown
        scr.timePeriodDropdown.addActionListener(e -> {
            String selected = (String) scr.timePeriodDropdown.getSelectedItem();
            if (selected == null) return;

            switch (selected) {
                case "Month" -> state.setMode(StatsScreenConstants.AggregationMode.MONTH);
                case "Year"  -> state.setMode(StatsScreenConstants.AggregationMode.YEAR);
                case "Total" -> state.setMode(StatsScreenConstants.AggregationMode.TOTAL);
            }
            refresher.refresh();
        });

        // Period navigation
        scr.prevPeriodBtn.addActionListener(e -> {
            switch (state.getMode()) {
                case MONTH -> state.setCurrentDate(state.getCurrentDate().minusMonths(1));
                case YEAR  -> state.setCurrentDate(state.getCurrentDate().minusYears(1));
                case TOTAL -> { /* no-op */ }
            }
            refresher.refresh();
        });

        scr.nextPeriodBtn.addActionListener(e -> {
            switch (state.getMode()) {
                case MONTH -> state.setCurrentDate(state.getCurrentDate().plusMonths(1));
                case YEAR  -> state.setCurrentDate(state.getCurrentDate().plusYears(1));
                case TOTAL -> { /* no-op */ }
            }
            refresher.refresh();
        });

        // Income / Expense toggles
        scr.incomeToggleButton.addActionListener(e -> {
            state.setShowIncome(true);
            refresher.refresh();
        });

        scr.expenseToggleButton.addActionListener(e -> {
            state.setShowIncome(false);
            refresher.refresh();
        });
    }
}
