package com.server.controller;

import com.server.model.ExpenseCategory;
import com.server.service.ExpenseCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// record class to hold category request data
record CategoryRequest(String name, String icon, String username) {}

// it maps all routers under /categories to this controller
// all functions are stored in service layer, this layer is just to map requests to service functions
@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
public class ExpenseCategoryController {

    private final ExpenseCategoryService service;

    public ExpenseCategoryController(ExpenseCategoryService service) {
        this.service = service;
    }

    @GetMapping("/{username}")
    public List<ExpenseCategory> getAll(@PathVariable String username) {
        // get all categories for a user
        return service.getAll(username);
    }

    @PostMapping
    public String create(@RequestBody CategoryRequest req) {
        // create a new category
        if(service.create(req.name(), req.icon(), req.username())){
            return "OK";
        }
        return "EXISTS";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable String id, @RequestBody CategoryRequest req) {
        // update category by id
        if(service.update(id, req.name(), req.icon(), req.username())){
            return "OK";
        }
        return "NOT_FOUND";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        // delete category by id
        if(service.delete(id)){
            return "OK";
        }
        return "NOT_FOUND";
    }
}
