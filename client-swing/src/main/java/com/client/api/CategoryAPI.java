package com.client.api;

import java.util.List;

import com.client.constants.Constants;
import com.client.model.ExpenseCategory;
import com.client.utils.HttpClient;

// category is same as expense category like Food, Rent, etc.
public class CategoryAPI {
    public static List<ExpenseCategory> fetchCategories() {
        try {
            String json = HttpClient.get(Constants.BASE_URL + "/categories");

            if (json == null) {
                // Return an empty list if the response is null
                return List.of();
            }
            // else return the parsed list
            return HttpClient.fromJsonList(json, ExpenseCategory.class);
        
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
