package com.server.service;

import org.springframework.stereotype.Service;

import com.server.repository.UserRepo;
import com.server.security.JWTUtil;
import com.server.model.DataEntry;
import com.server.model.ExpenseCategory;
import com.server.model.User;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.security.MessageDigest;

import com.server.repository.DataEntryRepo;
import com.server.repository.ExpenseCategoryRepo;

import io.github.cdimascio.dotenv.Dotenv;


// this layer contains all business logic related to authentication
@Service
public class AuthService {
    // loading all required repositories, to interact with database
    private final UserRepo userRepo;
    private final ExpenseCategoryRepo expenseCategoryRepo; // expense category are specific to user
    private final DataEntryRepo dataEntryRepo;
    private final String pepper;
    private final JWTUtil jwtUtil;

    public AuthService(
        UserRepo userRepo,
        ExpenseCategoryRepo expenseCategoryRepo,
        DataEntryRepo dataEntryRepo,
        Dotenv dotenv,
        JWTUtil jwtUtil
    ) { 
        this.userRepo = userRepo; 
        this.expenseCategoryRepo = expenseCategoryRepo;
        this.dataEntryRepo = dataEntryRepo;
        this.jwtUtil = jwtUtil;
        this.pepper = dotenv.get("SECURITY_PEPPER");
    }

    // Generate random salt, 16 bytes long
    // its used to hash passwords securely, and prevent rainbow table attacks
    private String generateSalt() {
        byte[] salt = new byte[16];

        new SecureRandom().nextBytes(salt);
        // return salt as base64 string
        return Base64.getEncoder().encodeToString(salt);
    }

    // Hash password + salt using SHA-256
    private String hashPassword(String password, String salt) throws Exception {
        // Combine password and salt, then hash
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        // Convert combined string to bytes and update the message digest
        String combined = password + salt + pepper;
        System.out.println("Hashing password with pepper string: " + pepper);
        md.update(combined.getBytes());
        // Compute the final hash value
        return Base64.getEncoder().encodeToString(md.digest());
    }

    public boolean register(
        String firstName,
        String lastName,
        String email,
        String username,
        String password
    ) {
        if (
            userRepo.findByEmail(email) != null ||
            userRepo.findByUsername(username) != null
        ) {
            return false; // user already exists
        }

        try {
            String salt = generateSalt();
            String hash = hashPassword(password, salt);
            User newUser = new User(email, username, firstName, lastName, hash, salt);
            // save user to database
            userRepo.save(newUser);

            // for eac huser we will create some default expense categories
            List<ExpenseCategory> defaults = List.of(
                new ExpenseCategory("Food",
                "🍜", username
                ),
                new ExpenseCategory("Grocery",
                "🛒", username
                ),
                new ExpenseCategory("Transport",
                "🚗", username
                ),
                new ExpenseCategory("Shopping",
                "🛍️", username
                ),
                new ExpenseCategory("Health",
                "💊", username
                ),
                new ExpenseCategory("Medical",
                "🏥", username
                ),
                new ExpenseCategory("Entertainment",
                "🎬", username
                ),
                new ExpenseCategory("Rent",
                "🏠", username
                ),
                new ExpenseCategory("Investment",
                "📈", username
                ),
                new ExpenseCategory("Other",
                "💸", username
                )
            );
            // save all default categories to database for the user created
            expenseCategoryRepo.saveAll(defaults);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public String login(String username, String password) {
        User u = userRepo.findByUsername(username);
        if (u == null) return null;

        try {
            String hash = hashPassword(password, u.getSalt());
            if (!hash.equals(u.getPasswordHash())) return null;

            return jwtUtil.generateToken(username);

        } catch (Exception e) {
            return null;
        }
    }


    // Delete user account and all associated data
    public boolean deleteAccount(String username) {
        // check if user exists
        User u = userRepo.findByUsername(username);
        
        if (u == null) {
            return false; // user not found
        }

        // delte all categories and data entries associated with the user
        List<ExpenseCategory> cats = expenseCategoryRepo.findByUsername(username);

        if (cats != null && !cats.isEmpty()) {
            expenseCategoryRepo.deleteAll(cats);
        }

        // delte all transactions associated with the user
        List<DataEntry> entries = dataEntryRepo.findByUsername(username);

        if (entries != null && !entries.isEmpty()) {
            dataEntryRepo.deleteAll(entries);
        }

        // delte user from database
        userRepo.delete(u);

        return true;
    }

}
