package com.example.formativa.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class PasswordConfigTest {

    private final PasswordConfig passwordConfig = new PasswordConfig();

    @Test
    @DisplayName("Debería crear un bean BCryptPasswordEncoder")
    void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = passwordConfig.passwordEncoder();
        
        assertNotNull(passwordEncoder);
        
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }
    
    @Test
    @DisplayName("Debería codificar y coincidir con las contraseñas correctamente")
    void testPasswordEncodingAndMatching() {
        PasswordEncoder passwordEncoder = passwordConfig.passwordEncoder();
        
        String rawPassword = "testPassword123";
        
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        assertTrue(!rawPassword.equals(encodedPassword));
        
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        
        assertTrue(!passwordEncoder.matches("differentPassword", encodedPassword));
    }
} 