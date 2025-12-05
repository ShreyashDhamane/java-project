package com.client.components.dashboard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.client.model.DataEntry;

public class DayGroup {

    public LocalDate date;
    public int totalIncome = 0;
    public int totalExpense = 0;

    public List<DataEntry> entries = new ArrayList<>();

    public DayGroup(LocalDate date) {
        this.date = date;
    }
}
