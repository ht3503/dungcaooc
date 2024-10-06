package com.javamongo.moviebooktickets.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javamongo.moviebooktickets.dto.account.LoginRequest;
import com.javamongo.moviebooktickets.dto.account.RegisterRequest;
import com.javamongo.moviebooktickets.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    
    Logger logger = Logger.getLogger(AccountController.class.getName());

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.Login(loginRequest));
        // return ResponseEntity.ok(loginRequest);
    }
    @PostMapping("/register")
    public ResponseEntity<?> Register(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.info("Register Request: " + registerRequest);
        return ResponseEntity.ok(userService.Register(registerRequest));
        // return ResponseEntity.ok(registerRequest);
    }
}
