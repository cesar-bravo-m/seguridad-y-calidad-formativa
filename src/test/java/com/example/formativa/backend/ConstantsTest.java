package com.example.formativa.backend;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConstantsTest {

    @Test
    @DisplayName("Las constantes deben tener los valores correctos")
    void testConstants() {
        assertEquals("/login", Constants.LOGIN_URL);
        assertEquals("Authorization", Constants.HEADER_AUTHORIZACION_KEY);
        assertEquals("Bearer ", Constants.TOKEN_BEARER_PREFIX);
        assertEquals("https://www.duocuc.cl/", Constants.ISSUER_INFO);
        assertEquals(864_000_000, Constants.TOKEN_EXPIRATION_TIME);
    }

    @Test
    @DisplayName("getSigningKeyB64 debe devolver una clave válida")
    void testGetSigningKeyB64() {
        String secret = "dGVzdF9zZWNyZXRfa2V5X2Zvcl90ZXN0aW5nX3B1cnBvc2Vz";
        Key key = Constants.getSigningKeyB64(secret);
        assertNotNull(key);
    }

    @Test
    @DisplayName("getSigningKey debe devolver una clave válida")
    void testGetSigningKey() {
        String secret = "test_secret_key_for_testing_purposes";
        Key key = Constants.getSigningKey(secret);
        assertNotNull(key);
    }
} 