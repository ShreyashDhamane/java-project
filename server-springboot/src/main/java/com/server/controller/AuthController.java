package com.server.controller;

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
    public String login(@RequestBody AuthRequest req){
        if(service.login(req.username(), req.password())){
            return "OK";
        }
        return "INVALID";
    }

    @DeleteMapping("/{username}")
    public String deleteAccount(@PathVariable String username) {
        if(service.deleteAccount(username)){
            return "OK";
        }
        return "ERROR";
    }

}
