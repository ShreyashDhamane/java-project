package com.server.service;

import com.server.model.ExpenseCategory;
import com.server.repository.ExpenseCategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseCategoryService {
    private final ExpenseCategoryRepo expenseCategoryRepo;

    public ExpenseCategoryService(ExpenseCategoryRepo repo) {
        this.expenseCategoryRepo = repo;
    }

    public List<ExpenseCategory> getAll(String username) {
        // get all categories for a user
        return expenseCategoryRepo.findByUsername(username);
    }

    public boolean create(String name, String icon, String username) {
        if (expenseCategoryRepo.findByNameIgnoreCaseAndUsername(name, username) != null) {
            return false; // category with same name exists for user
        }
        // create and save new category
        expenseCategoryRepo.save(new ExpenseCategory(name, icon, username));
        return true;
    }

    public boolean delete(String id) {
        if (!expenseCategoryRepo.existsById(id)) {
            return false; // category with given id does not exist
        }
        // delete category by id s
        expenseCategoryRepo.deleteById(id);
        return true;
    }

    public boolean update(String id, String name, String icon, String username) {
        // Only update categories owned by user
        ExpenseCategory existing = expenseCategoryRepo.findById(id).orElse(null);
        if (existing == null){
             return false;
        }

        // Prevent editing categories owned by another user
        if (!existing.getUsername().equals(username)){
             return false;
        }
        // update fields
        existing.setName(name);
        existing.setIcon(icon);

        expenseCategoryRepo.save(existing);
        return true;
    }

}
