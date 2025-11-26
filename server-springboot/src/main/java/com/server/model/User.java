package com.server.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public class User {
    @Id
    private String id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String passwordHash;
    private String salt;

    public User() {}

    public User(
        String email,
        String username,
        String firstName,
        String lastName,
        String passwordHash,
        String salt
    ) {

        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    // Getters and setters for all fields
    public String getId() { return id; }

    public String getEmail() { 
        return email;
    }

    public void setEmail(String email) { 
        this.email = email;
    }

    public String getUsername() { 
        return username;
    }

    public void setUsername(String username) { 
        this.username = username;
    }

    public String getFirstName() { 
        return firstName;
    }

    public void setFirstName(String firstName) { 
        this.firstName = firstName;
    }

    public String getLastName() { 
        return lastName;
    }

    public void setLastName(String lastName) { 
        this.lastName = lastName;
    }

    public String getPasswordHash() { 
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) { 
        this.passwordHash = passwordHash;
    }

    public String getSalt() { 
        return salt;
    }

    public void setSalt(String salt) { 
        this.salt = salt;
    }
}
