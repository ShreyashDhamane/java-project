package com.client.model;

// model to hold expense/income data entries from backend APIs
public class DataEntry {
    private String id;
    private String username;
    private String type;
    private String date;
    private String category;
    private String note;
    private int amount;
    private String paymentType;
    private static int tempIdCounter = -1; // for generating temporary IDs for new entries and adding them to UI before backend assigns real IDs

    // Jackson ObjectMapper requires a default constructor
    // jackson object mapper is used to serialize/deserialize JSON data from backend APIs
    public DataEntry() {}

    public DataEntry(
        String username,
        String type,
        String date, 
        String category,
        String note,
        int amount,
        String paymentType
    ) {
        this.id = String.valueOf(tempIdCounter--);
        this.username = username;
        this.date = date;
        this.paymentType = paymentType;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.note = note;
    }

    // Getters and Setters
    public String getId() { 
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDate() { 
        return date; 
    }

    public void setDate(String date) { 
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
