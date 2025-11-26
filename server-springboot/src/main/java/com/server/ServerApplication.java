package com.server;

import com.server.model.ExpenseCategory;
import com.server.repository.CurrencyRepo;
import com.server.repository.ExpenseCategoryRepo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.server.model.Currency;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    // when application runs for first time, pre-populate some database entries, as its a local db
    @Bean
    CommandLineRunner initDB(ExpenseCategoryRepo categoryRepo, CurrencyRepo currencyRepo) {
        return args -> {
            if (currencyRepo.count() == 0) {
                // if repo is empty, add default currencies
                currencyRepo.save(new Currency("US Dollar", "USD", "$"));
                currencyRepo.save(new Currency("Indian Rupee", "INR", "₹"));
                currencyRepo.save(new Currency("Euro", "EUR", "€"));
                currencyRepo.save(new Currency("British Pound", "GBP", "£"));
                currencyRepo.save(new Currency("Japanese Yen", "JPY", "¥"));
                currencyRepo.save(new Currency("Korean Won", "KRW", "₩"));
                System.out.println("✔ Default currencies inserted.");
            }
        };
    }
}
