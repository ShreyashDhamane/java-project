package com.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.server.model.DataEntry;
import java.util.List;

// Repository interface for DataEntry model
// its the bridge between the application and the database for DataEntry objects
// we use this object to perform CRUD operations on DataEntry collection in MongoDB
public interface DataEntryRepo extends MongoRepository<DataEntry, String> {
    
    List<DataEntry> findByUsername(String username);
}
