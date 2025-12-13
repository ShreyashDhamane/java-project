package com.client.screens.stats;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.client.constants.StatsScreenConstants;
import com.client.model.DataEntry;

public class StatsAggregator {

    public static class AggregationResult {
        public final Map<String, Long> categoryTotals;
        public final long totalAmount;
        public final Map<String, Long> timeSeriesTotals;

        public AggregationResult(
            Map<String, Long> categoryTotals,
            long totalAmount,
            Map<String, Long> timeSeriesTotals
        ) {
            this.categoryTotals = categoryTotals;
            this.totalAmount = totalAmount;
            this.timeSeriesTotals = timeSeriesTotals;
        }
    }

    public AggregationResult aggregate(List<DataEntry> all, StatsState state) {
        if (all == null) {
            all = Collections.emptyList();
        }

        Map<String, Long> categoryTotals = new LinkedHashMap<>();
        Map<String, Long> seriesData = new LinkedHashMap<>();

        YearMonth selectedMonth = YearMonth.from(state.getCurrentDate());
        int selectedYear = state.getCurrentDate().getYear();

        boolean showIncome = state.isShowIncome();
        var mode = state.getMode();

        for (DataEntry e : all) {
            if (e == null || e.getDate() == null) continue;

            boolean isIncome = "Income".equalsIgnoreCase(e.getType());
            if (showIncome && !isIncome) continue;
            if (!showIncome && isIncome) continue;

            LocalDate d;
            try {
                d = LocalDate.parse(e.getDate(), StatsScreenConstants.DATE_IN);
            } catch (Exception ex) {
                continue;
            }

            boolean matches = switch (mode) {
                case MONTH -> YearMonth.from(d).equals(selectedMonth);
                case YEAR  -> d.getYear() == selectedYear;
                case TOTAL -> true;
            };
            if (!matches) continue;

            // Category totals
            String category = (e.getCategory() != null) ? e.getCategory() : "Other";
            long amount = e.getAmount();
            categoryTotals.merge(category, amount, Long::sum);

            // Time-series grouping
            String key;
            if (mode == StatsScreenConstants.AggregationMode.MONTH) {
                key = String.valueOf(d.getDayOfMonth());
            } else if (mode == StatsScreenConstants.AggregationMode.YEAR) {
                key = d.getMonth().name().substring(0, 3);
            } else {
                key = String.valueOf(d.getYear());
            }

            seriesData.merge(key, amount, Long::sum);
        }

        long total = 0;
        for (Long v : categoryTotals.values()) {
            total += v;
        }

        return new AggregationResult(categoryTotals, total, seriesData);
    }
}
