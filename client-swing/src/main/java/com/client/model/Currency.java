package com.client.model;

// Currency model to validate and hold currency data from backend APIs
public class Currency {
    private String id;
    private String name;
    private String code;
    private String symbol;

    public Currency() {
    }

    public Currency(
        String id,
        String name,
        String code,
        String symbol
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.symbol = symbol;
    }
    // Getters and Setters
    public String getId() { 
        return id;
    }

    public String getName() { 
        return name;
    }

    public String getCode() { 
        return code;
    }

    public String getSymbol() { 
        return symbol;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
