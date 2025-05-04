package com.example.formativa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.formativa.backend.JWTAuthenticationConfig;
import com.example.formativa.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTAuthenticationConfig jwtAuthtenticationConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(
            @RequestParam("user") String user,
            @RequestParam("encryptedPass") String encryptedPass) {

        final UserDetails userDetails = userService.loadUserByUsername(user);

        if (!passwordEncoder.matches(encryptedPass, userDetails.getPassword())) {
            throw new RuntimeException("Invalid login");
        }

        String token = jwtAuthtenticationConfig.getJWTToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.add("Location", "/");

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
} 