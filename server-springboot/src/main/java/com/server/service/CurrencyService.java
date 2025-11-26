package com.server.service;

import com.server.model.Currency;
import com.server.repository.CurrencyRepo;
import org.springframework.stereotype.Service;

import java.util.List;

// Service layer for Currency operations
@Service
public class CurrencyService {
    private final CurrencyRepo currencyRepo;

    public CurrencyService(CurrencyRepo currencyRepo) {
        this.currencyRepo = currencyRepo;
    }

    public List<Currency> getAll() {
        return currencyRepo.findAll();
    }

    public boolean create(String name, String code, String symbol) {
        if (currencyRepo.findByCodeIgnoreCase(code) != null) {
            return false; // currency with same code exists
        }

        // create and save new currency
        currencyRepo.save(new Currency(name, code, symbol));
        return true;
    }

    public boolean delete(String id) {
        if (!currencyRepo.existsById(id)) {
            return false; // currency with given id does not exist
        }
        // delete currency by id
        currencyRepo.deleteById(id);
        return true;
    }

    public boolean update(String id, String name, String code, String symbol) {
        // find existing currency by id
        Currency existingCurrency = currencyRepo.findById(id).orElse(null);
        
        if (existingCurrency == null) {
            return false; // currency with given id does not exist
        }

        // update fields
        existingCurrency.setName(name);
        existingCurrency.setCode(code);
        existingCurrency.setSymbol(symbol);
        
        // save updated currency

        currencyRepo.save(existingCurrency);
        return true;
    }
}
