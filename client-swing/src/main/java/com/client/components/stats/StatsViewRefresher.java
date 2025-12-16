package com.client.components.stats;

import java.awt.Color;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.client.constants.StatsScreenConstants;
import com.client.core.AppState;
import com.client.model.DataEntry;
import com.client.screens.stats.StatsScreen;
import com.client.screens.stats.StatsState;

public class StatsViewRefresher {

    private final StatsScreen scr;
    private final StatsState state;
    private final StatsAggregator aggregator;
    private final LegendBuilder legendBuilder;

    public StatsViewRefresher(
        StatsScreen scr,
        StatsState state,
        StatsAggregator aggregator,
        LegendBuilder legendBuilder) {
        this.scr = scr;
        this.state = state;
        this.aggregator = aggregator;
        this.legendBuilder = legendBuilder;
    }

    public void refresh() {
        List<DataEntry> all = AppState.getInstance().getEntries();
        StatsAggregator.AggregationResult result = aggregator.aggregate(all, state);

        // Build slices sorted by amount descending
        List<Map.Entry<String, Long>> sorted = new ArrayList<>(result.categoryTotals.entrySet());
        sorted.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));

        List<PieSlice> slices = new ArrayList<>();
        int colorIndex = 0;
        for (Map.Entry<String, Long> e : sorted) {
            double percent = (result.totalAmount == 0)
                    ? 0
                    : (e.getValue() * 1.0 / result.totalAmount);
            Color color = PieChartPanel.SLICE_COLORS[colorIndex % PieChartPanel.SLICE_COLORS.length];
            String emoji = EmojiMapper.get(e.getKey());
            slices.add(new PieSlice(e.getKey(), emoji, e.getValue(), percent, color));
            colorIndex++;
        }

        // Pie chart center label
        String centerLabel = (state.isShowIncome() ? "Income" : "Exp.")
                + " " + StatsScreenConstants.AMOUNT_FMT.format(result.totalAmount);
        scr.pieChartPanel.setData(slices, centerLabel);

        // Legend
        legendBuilder.rebuildLegendList(scr.legendListPanel, slices, result.totalAmount);

        // Period label
        YearMonth selectedMonth = YearMonth.from(state.getCurrentDate());
        switch (state.getMode()) {
            case MONTH -> scr.periodLabel.setText(selectedMonth.format(StatsScreenConstants.MONTH_OUT));
            case YEAR  -> scr.periodLabel.setText(state.getCurrentDate().format(StatsScreenConstants.YEAR_OUT));
            case TOTAL -> scr.periodLabel.setText("Total");
        }

        // Enable/disable arrows
        boolean arrowsEnabled = (state.getMode() != StatsScreenConstants.AggregationMode.TOTAL);
        scr.prevPeriodBtn.setEnabled(arrowsEnabled);
        scr.nextPeriodBtn.setEnabled(arrowsEnabled);
        Color enabledColor = Color.WHITE;
        Color disabledColor = new Color(100, 100, 100);
        scr.prevPeriodBtn.setForeground(arrowsEnabled ? enabledColor : disabledColor);
        scr.nextPeriodBtn.setForeground(arrowsEnabled ? enabledColor : disabledColor);

        // Time-series chart
        String typeText = state.isShowIncome() ? "Income" : "Expense";
        String periodText = switch (state.getMode()) {
            case MONTH -> selectedMonth.format(StatsScreenConstants.MONTH_OUT);
            case YEAR  -> state.getCurrentDate().format(StatsScreenConstants.YEAR_OUT);
            case TOTAL -> "All Years";
        };
        String titleText = typeText + " \u2013 " + periodText;

        scr.timeSeriesChartPanel.setData(result.timeSeriesTotals, titleText);

        // Toggles + tab styles + visibility
        updateTypeToggleStyles();
        updateTabStyles();
        updateViewVisibility();
    }

    private void updateTypeToggleStyles() {
        // Ensure selection matches state
        scr.incomeToggleButton.setSelected(state.isShowIncome());
        scr.expenseToggleButton.setSelected(!state.isShowIncome());

        Color selectedColor = new Color(200, 200, 200);

        if (state.isShowIncome()) {
            scr.incomeToggleButton.setForeground(Color.WHITE);
            scr.expenseToggleButton.setForeground(selectedColor);
        } else {
            scr.expenseToggleButton.setForeground(Color.WHITE);
            scr.incomeToggleButton.setForeground(selectedColor);
        }
    }

    private void updateTabStyles() {
        Color activeBg = new Color(255, 100, 90);
        Color inactiveBg = new Color(30, 35, 48);
        Color inactiveFg = new Color(180, 180, 180);

        if (!state.isShowTimeSeries()) {
            scr.statsTabLabel.setBackground(activeBg);
            scr.statsTabLabel.setForeground(Color.WHITE);

            scr.timeSeriesTabLabel.setBackground(inactiveBg);
            scr.timeSeriesTabLabel.setForeground(inactiveFg);
        } else {
            scr.timeSeriesTabLabel.setBackground(activeBg);
            scr.timeSeriesTabLabel.setForeground(Color.WHITE);

            scr.statsTabLabel.setBackground(inactiveBg);
            scr.statsTabLabel.setForeground(inactiveFg);
        }
    }

    private void updateViewVisibility() {
        boolean statsMode = !state.isShowTimeSeries();

        scr.pieChartPanel.setVisible(statsMode);
        scr.legendScrollPane.setVisible(statsMode);

        scr.timeSeriesChartPanel.setVisible(!statsMode);

        scr.revalidate();
        scr.repaint();
    }
}
