package com.server.controller;

import com.server.model.Currency;
import com.server.service.CurrencyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// record class to hold currency request data
record CurrencyRequest(String name, String code, String symbol) {}

// it maps all routers under /currency to this controller
// all functions are stored in service layer, this layer is just to map requests to service functions
@RestController
@RequestMapping("/currency")
@CrossOrigin(origins = "*")
public class CurrencyController {

    private final CurrencyService service;

    public CurrencyController(CurrencyService service) {
        this.service = service;
    }

    @GetMapping
    public List<Currency> getAll() {
        return service.getAll();
    }

    @PostMapping
    public String create(@RequestBody CurrencyRequest req) {
        if(service.create(req.name(), req.code(), req.symbol())){
            return "OK";
        }
        return "EXISTS";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable String id, @RequestBody CurrencyRequest req) {
        if(service.update(id, req.name(), req.code(), req.symbol())){
            return "OK";
        }
        return "NOT_FOUND";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        if(service.delete(id)){
            return "OK";
        }
        return "NOT_FOUND";
    }
}
