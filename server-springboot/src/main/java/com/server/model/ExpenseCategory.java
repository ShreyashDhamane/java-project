package com.server.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("expense_categories")
public class ExpenseCategory {

    @Id
    private String id;
    private String name;
    private String icon;
    private String username;

    public ExpenseCategory() {}

    public ExpenseCategory(String name, String icon, String username) {
        this.name = name;
        this.icon = icon;
        this.username = username;
    }

    // Getters and Setters for all fields
    public String getId() { return id; }

    public String getName() { 
        return name;
    }
    
    public void setName(String name) { 
        this.name = name;
    }

    public String getIcon() { 
        return icon;
    }
    
    public void setIcon(String icon) { 
        this.icon = icon;
    }

    public String getUsername() { 
        return username;
    }
    
    public void setUsername(String username) { 
        this.username = username;
    }
}
