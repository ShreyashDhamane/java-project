package com.client.components.timeseries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TimeSeriesDataSorter {

    private static final List<String> MONTHS =
            Arrays.asList("Jan","Feb","Mar","Apr","May","Jun",
                          "Jul","Aug","Sep","Oct","Nov","Dec");

    public static List<Entry<String, Long>> sort(Map<String, Long> data) {

        List<Entry<String, Long>> list = new ArrayList<>(data.entrySet());

        if (list.isEmpty()) return list;

        boolean allNumeric = list.stream().allMatch(e -> e.getKey().matches("\\d+"));
        if (allNumeric) {
            list.sort(Comparator.comparingInt(e -> Integer.parseInt(e.getKey())));
            return list;
        }

        boolean allMonths = list.stream().allMatch(e -> MONTHS.contains(e.getKey()));
        if (allMonths) {
            list.sort(Comparator.comparingInt(e -> MONTHS.indexOf(e.getKey())));
            return list;
        }

        list.sort(Comparator.comparing(Entry::getKey));
        return list;
    }
}
