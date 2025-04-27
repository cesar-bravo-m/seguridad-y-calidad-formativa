package com.example.formativa.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JWTAuthenticationConfigTest {
    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("getJWTToken debe lanzar una excepción si no se proporciona un nombre de usuario")
    void testIsJWTValid() throws Exception {
        JWTAuthenticationConfig config = new JWTAuthenticationConfig();
        assertThrows(NullPointerException.class, () -> {
            config.getJWTToken(null);
        });
    }
    
}
