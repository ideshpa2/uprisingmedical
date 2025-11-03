 package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


    public class JoinRequestDTO {
        private String username;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; 
        
        }
        
    }

