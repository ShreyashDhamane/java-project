package com.client.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.client.model.Currency;
import com.client.model.DataEntry;
import com.client.model.ExpenseCategory;

// app state is a singleton that holds global state like logged in user, entries, settings, etc.
// it similart to a redux store in react
// centralized state management for the application
public class AppState {
    private static AppState instance;

    private List<Currency> currencies = new ArrayList<>(); // list of supported currencies
    private String username; // logged in username
    private List<DataEntry> entries; // all expense/income entries for the user
    private String currencyCode = "USD";   // e.g. "INR" // default currency code
    private String currencySymbol = "$";   // e.g. "₹" // default currency symbol
    private List<ExpenseCategory> categories = new ArrayList<>(); // list of expense categories
    private String jwtToken;

    // Map of currency codes, currently limited to few common ones
    private static final Map<String, String> CURRENCY_MAP = Map.of(
        "USD", "$",
        "EUR", "€",
        "INR", "₹",
        "GBP", "£",
        "JPY", "¥",
        "KRW", "₩"
    );

    private AppState() {
        entries = new ArrayList<>();
    }

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    // Getters and Setters for all properties
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<DataEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<DataEntry> entries) {
        this.entries = entries;
    }

    public void addEntry(DataEntry entry) {
        this.entries.add(entry);
    }

    // Update an existing entry based on its ID
    public void updateEntry(DataEntry updatedEntry) {
        for (int i = 0; i < entries.size(); i++) {
            // same id find and update
            if (entries.get(i).getId().equals(updatedEntry.getId())) {
                entries.set(i, updatedEntry);
                break;
            }
        }
    }

// Delete an entry by its ID
    public void deleteEntry(String id) {
        // short hand syntax to remove if id matches
        entries.removeIf(e -> e.getId().equals(id));
    }

    // when user logs out, reset the state
    public void reset() {
        this.username = null;

        if (entries != null) {
            entries.clear();
        }
    }

    // default currency is USD    
    public String getCurrencyCode() {
        return currencyCode;
    }

    // set default currency code and symbol
    public void setCurrencyCode(String code) {
        this.currencyCode = code;
        this.currencySymbol = CURRENCY_MAP.getOrDefault(code, code);
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public Map<String, String> getCurrencyMap() {
        return CURRENCY_MAP;
    }

    public List<ExpenseCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ExpenseCategory> categories) {
        this.categories = categories;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }


    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
