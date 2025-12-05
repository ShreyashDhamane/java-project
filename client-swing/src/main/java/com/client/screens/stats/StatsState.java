package com.client.screens.stats;

import java.time.LocalDate;

import com.client.constants.StatsScreenConstants;

public class StatsState {

    private StatsScreenConstants.AggregationMode mode;
    private LocalDate currentDate;
    private boolean showIncome;
    private boolean showTimeSeries;

    public StatsState() {
        this.mode = StatsScreenConstants.AggregationMode.MONTH;
        this.currentDate = LocalDate.now();
        this.showIncome = false;      // false => Expense
        this.showTimeSeries = false;  // false => Stats tab
    }

    public StatsScreenConstants.AggregationMode getMode() {
        return mode;
    }

    public void setMode(StatsScreenConstants.AggregationMode mode) {
        this.mode = mode;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public boolean isShowIncome() {
        return showIncome;
    }

    public void setShowIncome(boolean showIncome) {
        this.showIncome = showIncome;
    }

    public boolean isShowTimeSeries() {
        return showTimeSeries;
    }

    public void setShowTimeSeries(boolean showTimeSeries) {
        this.showTimeSeries = showTimeSeries;
    }
}
