package com.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.server.model.ExpenseCategory;

// Repository interface for ExpenseCategory model
// its the bridge between the application and the database for ExpenseCategory objects
// we use this object to perform CRUD operations on ExpenseCategory collection in MongoDB
public interface ExpenseCategoryRepo extends MongoRepository<ExpenseCategory, String> {
    // Find ExpenseCategory by name (case insensitive)
    ExpenseCategory findByNameIgnoreCase(String name);
    // Find all ExpenseCategories for a specific user
    java.util.List<ExpenseCategory> findByUsername(String username);
    // Find ExpenseCategory by name and username (both case insensitive)
    // already implemented function to check if category exists for a user
    ExpenseCategory findByNameIgnoreCaseAndUsername(String name, String username);

}
