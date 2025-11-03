// src/main/java/com/example/demo/controller/AuthController.java
package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${admin.signup.code:}")
    private String adminSignupCode;

    public AuthController(UserRepository userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> body) {
        String username  = (body.getOrDefault("username","")).trim();
        String password  = body.getOrDefault("password","");
        String role      = body.getOrDefault("role","STUDENT");
        String adminCode = body.get("adminCode");

        if (username.isEmpty() || password.isBlank()) {
            return ResponseEntity.badRequest().body("Username and password are required.");
        }

        if ("ADMIN".equalsIgnoreCase(role)) {
            if (adminSignupCode == null || adminSignupCode.isBlank()
                || adminCode == null || !adminCode.equals(adminSignupCode)) {
                return ResponseEntity.status(403).body("Invalid admin signup code.");
            }
        }

        if (userRepo.existsByUsername(username)) {
            return ResponseEntity.status(409).body("Username already exists.");
        }

        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password));
        u.setRole("ADMIN".equalsIgnoreCase(role) ? "ADMIN" : "STUDENT");

        try {
            userRepo.save(u);
            return ResponseEntity.status(201).body("Registered " + u.getRole());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not register: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.getOrDefault("username", "").trim();
        String password = credentials.getOrDefault("password", "");

        return userRepo.findByUsername(username)
            .filter(u -> passwordEncoder.matches(password, u.getPassword()))
            .map(u -> ResponseEntity.ok("Login successful for " + u.getUsername() + " (" + u.getRole() + ")"))
            .orElseGet(() -> ResponseEntity.status(401).body("Invalid username or password."));
    }
}
