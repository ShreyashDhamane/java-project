package com.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.server.model.Currency;

// Repository interface for Currency model
// its the bridge between the application and the database for Currency objects
// we use this object to perform CRUD operations on Currency collection in MongoDB
public interface CurrencyRepo extends MongoRepository<Currency, String> {
    Currency findByCodeIgnoreCase(String code);
}
