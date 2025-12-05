package com.client.screens.category;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.client.components.category.CategoryDialogs;
import com.client.constants.Constants;
import com.client.core.AppState;
import com.client.model.ExpenseCategory;
import com.client.utils.HttpClient;

public class CategoriesController {

    private static final String BASE_URL = Constants.BASE_URL + "/categories";
    private final ManageCategories scr;

    public CategoriesController(ManageCategories scr) {
        this.scr = scr;
    }

    // Load from backend
    public void loadCategoriesAsync() {
        new Thread(() -> {
            try {
                String username = AppState.getInstance().getUsername();
                String url = BASE_URL + "/" + username;

                String json = HttpClient.get(url);
                if (json == null) return;

                List<ExpenseCategory> list = HttpClient.fromJsonList(json, ExpenseCategory.class);

                SwingUtilities.invokeLater(() -> {
                    scr.categories.clear();
                    scr.categories.addAll(list);

                    AppState.getInstance().setCategories(new ArrayList<>(scr.categories));
                    scr.refreshCategoryList();
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    // Create Category
    public void createCategoryAsync(String name, String icon, String username) {
        new Thread(() -> {
            try {
                String body = """
                    {"name":"%s","icon":"%s","username":"%s"}
                    """.formatted(escape(name), escape(icon), escape(username));

                String res = HttpClient.post(BASE_URL, body);

                if ("OK".equalsIgnoreCase(res)) {
                    loadCategoriesAsync();
                } else {
                    SwingUtilities.invokeLater(() ->
                        CategoryDialogs.showError(scr, "Category already exists.")
                    );
                }

            } catch (Exception ex) { ex.printStackTrace(); }
        }).start();
    }

    // Update Category
    public void updateCategoryAsync(ExpenseCategory cat) {
        new Thread(() -> {
            try {
                String username = AppState.getInstance().getUsername();

                String body = """
                    {"name":"%s","icon":"%s","username":"%s"}
                    """.formatted(
                        escape(cat.getName()),
                        escape(cat.getIcon()),
                        escape(username)
                );

                String res = HttpClient.put(BASE_URL + "/" + cat.getId(), body);

                if ("OK".equalsIgnoreCase(res)) {
                    loadCategoriesAsync();
                } else {
                    SwingUtilities.invokeLater(() ->
                        CategoryDialogs.showError(scr, "Update failed.")
                    );
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        }).start();
    }

    private String escape(String s) { return s.replace("\"", "\\\""); }

    // For FAB Add Category button
    public void showAddDialog() {
        CategoryDialogs.showAddDialog(scr, this);
    }
}
