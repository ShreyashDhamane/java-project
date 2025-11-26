package com.client.constants;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class StatsScreenConstants {
    public static final DateTimeFormatter DATE_IN   = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter MONTH_OUT = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);
    public static final DateTimeFormatter YEAR_OUT  = DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH);
    
    public static final DecimalFormat AMOUNT_FMT    = new DecimalFormat("₹ #,##0.00");
    public static final DecimalFormat PERCENT_FMT   = new DecimalFormat("0'%'");

    // aggregation modes
    public enum AggregationMode { MONTH, YEAR, TOTAL }
    
}
