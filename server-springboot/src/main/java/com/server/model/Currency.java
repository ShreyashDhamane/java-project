package com.server.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Currency model representing different currencies
// just by creating this class we can store/retrieve currency objects from MongoDB

@Document("currencies")
public class Currency {

    @Id
    private String id;

    private String name;
    private String code;
    private String symbol;

    public Currency() {}

    public Currency(String name, String code, String symbol) {
        this.name = name;
        this.code = code;
        this.symbol = symbol;
    }

    // getters and setters for all fields
    public String getId() { 
        return id;
    }

    public String getName() { 
        return name;
    }

    public void setName(String name) { 
        this.name = name;
    }

    public String getCode() { 
        return code;
    }

    public void setCode(String code) { 
        this.code = code;
    }

    public String getSymbol() { 
        return symbol;
    }

    public void setSymbol(String symbol) { 
        this.symbol = symbol;
    }
}
