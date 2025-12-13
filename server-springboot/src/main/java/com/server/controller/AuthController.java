package com.server.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.*;
import com.server.service.AuthService;

// record class to hold auth request data
record AuthRequest(String firstName, String lastName, String email, String username, String password){}

// it maps all routers under /auth to this controller
// all functions are stored in service layer, this layer is just to map requests to service functions
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;
    
    public AuthController(AuthService s) { 
        this.service = s;
    }

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest req){
        if(service.register(req.firstName(), req.lastName(), req.email(), req.username(), req.password())){
            return "OK";
        }
        return "EXISTS";
    }


    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody AuthRequest req) {

        // AuthService.login(...) now returns a token or null
        String token = service.login(req.username(), req.password());

        if (token == null) {
            return Map.of(
                "success", false,
                "message", "Invalid credentials"
            );
        }

        return Map.of(
            "success", true,
            "token", token
        );
    }

    @DeleteMapping("/{username}")
    public String deleteAccount(@PathVariable String username) {
        if(service.deleteAccount(username)){
            return "OK";
        }
        return "ERROR";
    }

}
