package com.example.formativa.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfileTest {

    private Profile profile;
    private User user;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        user = new User("testuser", "password123", "test@example.com");
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(profile);
        assertNull(profile.getId());
        assertNull(profile.getUser());
        assertNull(profile.getAvatarUri());
        assertNull(profile.getFavoriteGames());
        assertTrue(profile.isEmailNotifications());
        assertTrue(profile.isPushNotifications());
    }

    @Test
    void testParameterizedConstructor() {
        Profile newProfile = new Profile(user);
        assertEquals(user, newProfile.getUser());
        assertEquals(newProfile, user.getProfile());
    }

    @Test
    void testGettersAndSetters() {
        profile.setId(1L);
        assertEquals(1L, profile.getId());

        profile.setUser(user);
        assertEquals(user, profile.getUser());
        assertEquals(profile, user.getProfile());

        profile.setAvatarUri("avatar.jpg");
        assertEquals("avatar.jpg", profile.getAvatarUri());

        profile.setFavoriteGames("Minecraft, Fortnite");
        assertEquals("Minecraft, Fortnite", profile.getFavoriteGames());

        profile.setEmailNotifications(false);
        assertFalse(profile.isEmailNotifications());

        profile.setPushNotifications(false);
        assertFalse(profile.isPushNotifications());
    }

    @Test
    void testSetUser() {
        profile.setUser(user);
        assertEquals(user, profile.getUser());
        assertEquals(profile, user.getProfile());

        profile.setUser(user);
        assertEquals(user, profile.getUser());
        assertEquals(profile, user.getProfile());

        User newUser = new User("newuser", "pass123", "new@example.com");
        profile.setUser(newUser);
        assertEquals(newUser, profile.getUser());
        assertEquals(profile, newUser.getProfile());
    }

    @Test
    void testEqualsAndHashCode() {
        Profile profile1 = new Profile();
        profile1.setId(1L);
        profile1.setAvatarUri("avatar1.jpg");
        profile1.setFavoriteGames("Game1");
        profile1.setEmailNotifications(true);
        profile1.setPushNotifications(true);

        Profile profile2 = new Profile();
        profile2.setId(1L);
        profile2.setAvatarUri("avatar1.jpg");
        profile2.setFavoriteGames("Game1");
        profile2.setEmailNotifications(true);
        profile2.setPushNotifications(true);

        Profile profile3 = new Profile();
        profile3.setId(2L);
        profile3.setAvatarUri("avatar2.jpg");

        assertEquals(profile1, profile2);
        assertNotEquals(profile1, profile3);
        assertNotEquals(profile1, null);
        assertNotEquals(profile1, new Object());

        assertEquals(profile1.hashCode(), profile2.hashCode());
        assertNotEquals(profile1.hashCode(), profile3.hashCode());
    }

    @Test
    void testToString() {
        profile.setId(1L);
        profile.setAvatarUri("avatar.jpg");
        profile.setFavoriteGames("Minecraft");
        profile.setEmailNotifications(true);
        profile.setPushNotifications(true);
        
        String toString = profile.toString();
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("avatar.jpg"));
        assertTrue(toString.contains("Minecraft"));
        assertTrue(toString.contains("true"));
    }
} 