package com.server.controller;

import com.server.model.DataEntry;
import com.server.service.DataEntryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// record class to hold data entry request data
record DataEntryRequest(String username, String type, String date, String category, String note, int amount, String paymentType) {}

// it maps all routers under /api/data-entries to this controller
// all functions are stored in service layer, this layer is just to map requests to service functions
@RestController
@RequestMapping("/api/data-entries")
@CrossOrigin(origins = "*")
public class DataEntryController {
    private final DataEntryService service;

    public DataEntryController(DataEntryService service) {
        this.service = service;
    }

    @PostMapping
    public String addEntry(@RequestBody DataEntryRequest req) {
        if (
            req.username() == null ||
            req.username().isEmpty() ||
            req.type() == null ||
            req.type().isEmpty() ||
            req.date() == null ||
            req.date().isEmpty() ||
            req.category() == null ||
            req.category().isEmpty() ||
            req.amount() <= 0 ||
            req.paymentType() == null ||
            req.paymentType().isEmpty()
        ) {
            return "INVALID";
        }

        DataEntry entry = new DataEntry(
            req.username(),
            req.type(),
            java.time.LocalDate.parse(req.date()),
            req.category(),
            req.note(),
            req.amount(),
            req.paymentType()
        );
        // save entry iff valid
        service.save(entry);
        return "OK";
    }

    @GetMapping
    public List<DataEntry> getAll() {
        // get all data entries
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Optional<DataEntry> getById(@PathVariable String id) {
        // get entry by id
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        // delete entry by id
        service.delete(id);
        return "OK";
    }

    @GetMapping("/user/{username}")
    public List<DataEntry> getByUser(@PathVariable String username) {
        return service.getByUsername(username);
    }

    @PutMapping("/{id}")
    public String updateEntry(@PathVariable String id, @RequestBody DataEntryRequest req) {

        Optional<DataEntry> existing = service.getById(id);
        if (existing.isEmpty()) {
            return "NOT_FOUND";
        }

        DataEntry entry = existing.get();

        // update fields
        entry.setType(req.type());
        entry.setDate(java.time.LocalDate.parse(req.date()));
        entry.setCategory(req.category());
        entry.setNote(req.note());
        entry.setAmount(req.amount());
        entry.setPaymentType(req.paymentType());

        // save updated entry
        service.save(entry);
        return "OK";
    }


}
