package com.server.service;

import org.springframework.stereotype.Service;
import com.server.repository.DataEntryRepo;
import com.server.model.DataEntry;

import java.util.List;
import java.util.Optional;

@Service
public class DataEntryService {
    private final DataEntryRepo dataEntryRepo;

    public DataEntryService(DataEntryRepo dataEntryRepo) {
        this.dataEntryRepo = dataEntryRepo;
    }

    public DataEntry save(DataEntry entry) {
        // save data entry to database
        return dataEntryRepo.save(entry);
    }

    public List<DataEntry> getAll() {
        // get all data entries from database
        return dataEntryRepo.findAll();
    }

    public Optional<DataEntry> getById(String id) {
        
        // get data entry by id
        return dataEntryRepo.findById(id);
    }

    public void delete(String id) {
        if (!dataEntryRepo.existsById(id)) {
            return; // entry with given id does not exist
        }
        // delete data entry by id
        dataEntryRepo.deleteById(id);
    }

    public List<DataEntry> getByUsername(String username) {
        
        // get data entries by username
        return dataEntryRepo.findByUsername(username);
    }
}
