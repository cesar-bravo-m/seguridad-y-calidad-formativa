package com.example.formativa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.formativa.backend.JWTAuthenticationConfig;
import com.example.formativa.services.UserService;


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

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(
            @RequestParam("user") String username,
            @RequestParam("encryptedPass") String unencryptedPass) {

        final UserDetails userDetails = userService.loadUserByUsername(username);

        if (!passwordEncoder.matches(unencryptedPass, userDetails.getPassword())) {
            throw new RuntimeException("Invalid login");
        }

        String token = jwtAuthtenticationConfig.getJWTToken(username);

        return token;
    }
} 