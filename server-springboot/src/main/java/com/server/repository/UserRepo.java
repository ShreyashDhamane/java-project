package com.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.server.model.User;

// Repository interface for User model
// its the bridge between the application and the database for User objects
// we use this object to perform CRUD operations on User collection in MongoDB
public interface UserRepo extends MongoRepository<User, String> {
    // Find User by email, function is implemented by MongoDB automatically
    User findByEmail(String email);
    User findByUsername(String username);
}
