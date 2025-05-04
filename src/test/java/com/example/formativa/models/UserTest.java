package com.example.formativa.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

    private User user;
    private Profile profile;

    @BeforeEach
    void setUp() {
        user = new User();
        profile = new Profile();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getEmail());
        assertTrue(user.isEnabled());
        assertNull(user.getProfile());
    }

    @Test
    void testParameterizedConstructor() {
        User newUser = new User("testuser", "password123", "test@example.com");
        
        assertEquals("testuser", newUser.getUsername());
        assertEquals("password123", newUser.getPassword());
        assertEquals("test@example.com", newUser.getEmail());
        assertTrue(newUser.isEnabled());
        assertNull(newUser.getProfile());
    }

    @Test
    void testGettersAndSetters() {
        user.setId(1L);
        assertEquals(1L, user.getId());

        user.setUsername("testuser");
        assertEquals("testuser", user.getUsername());

        user.setPassword("password123");
        assertEquals("password123", user.getPassword());

        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());

        user.setEnabled(false);
        assertFalse(user.isEnabled());
    }

    @Test
    void testSetProfile() {
        user.setProfile(profile);
        assertEquals(profile, user.getProfile());
        assertEquals(user, profile.getUser());

        user.setProfile(null);
        assertNull(user.getProfile());

        user.setProfile(profile);
        user.setProfile(profile);
        assertEquals(profile, user.getProfile());
        assertEquals(user, profile.getUser());

        Profile newProfile = new Profile();
        user.setProfile(newProfile);
        assertEquals(newProfile, user.getProfile());
        assertEquals(user, newProfile.getUser());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User("user1", "pass1", "email1@test.com");
        User user2 = new User("user1", "pass1", "email1@test.com");
        User user3 = new User("user2", "pass2", "email2@test.com");

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user1, null);
        assertNotEquals(user1, new Object());

        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    void testToString() {
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        
        String toString = user.toString();
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("testuser"));
        assertTrue(toString.contains("test@example.com"));
    }
} 