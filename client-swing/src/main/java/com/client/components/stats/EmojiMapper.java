package com.client.components.stats;

import java.util.Map;
import java.util.TreeMap;

public class EmojiMapper {
    public static final Map<String, String> MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    static {
        MAP.put("Food", "🍜");
        MAP.put("Grocery", "🛒");
        MAP.put("Groceries", "🛒");
        MAP.put("Transport", "🚗");
        MAP.put("Travel", "✈️");
        MAP.put("Shopping", "🛍️");
        MAP.put("Salary", "💼");
        MAP.put("Investment", "📈");
        MAP.put("Medical", "🏥");
        MAP.put("Health", "💊");
        MAP.put("Household", "🪑");
        MAP.put("Rent", "🏠");
        MAP.put("Cloth", "👕");
        MAP.put("Clothes", "👕");
        MAP.put("Entertainment", "🎬");
        MAP.put("Other", "💸");
    }

    public static String get(String category) {
        return MAP.getOrDefault(category, "💸");
    }
}
