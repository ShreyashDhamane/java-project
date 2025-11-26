package com.server.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

// DataEntry model representing income or expense entries
// just by creating this class we can store/retrieve data entry objects from MongoDB
@Document("data_entries")
public class DataEntry {
    @Id
    private String id;

    private String username;
    private String type;        // Income or Expense
    private LocalDate date;
    private String category; // expense category
    private String note;
    private int amount;
    private String paymentType; // Cash, Credit, Debit Card

    public DataEntry() {}

    public DataEntry(
        String username,
        String type,
        LocalDate date,
        String category,
        String note,
        int amount,
        String paymentType
    ) {
        this.username = username;
        this.type = type;
        this.date = date;
        this.category = category;
        this.note = note;
        this.amount = amount;
        this.paymentType = paymentType;
    }

    // Getters and Setters for all fields
    public String getId() { 
        return id;
    }

    public String getUsername() { 
        return username;
    }
    
    public void setUsername(String username) { 
        this.username = username;
    }

    public String getType() { 
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() { 
        return date;
    }
    
    public void setDate(LocalDate date) { 
        this.date = date;
    }

    public String getCategory() { 
        return category;
    }

    public void setCategory(String category) { 
        this.category = category;
    }

    public String getNote() { 
        return note;
    }
    
    public void setNote(String note) { 
        this.note = note;
    }

    public int getAmount() { 
        return amount;
    }
    
    public void setAmount(int amount) { 
        this.amount = amount; 
    }

    public String getPaymentType() { 
        return paymentType;
    }
    
    public void setPaymentType(String paymentType) { 
        this.paymentType = paymentType;
    }
}
