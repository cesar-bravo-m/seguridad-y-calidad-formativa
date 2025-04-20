package com.example.formativa.repositories;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.formativa.models.Profile;
import com.example.formativa.models.User;

@DataJpaTest
@ActiveProfiles("test")
public class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private Profile profile1;
    private Profile profile2;

    @BeforeEach
    void setUp() {
        profileRepository.deleteAll();
        userRepository.deleteAll();

        user1 = new User("usuario1", "password1", "usuario1@example.com");
        user1 = userRepository.save(user1);

        user2 = new User("usuario2", "password2", "usuario2@example.com");
        user2 = userRepository.save(user2);

        profile1 = new Profile(user1);
        profile1.setAvatarUri("avatar1.jpg");
        profile1.setFavoriteGames("Juego1, Juego2");
        profile1.setEmailNotifications(true);
        profile1.setPushNotifications(false);
        profile1 = profileRepository.save(profile1);

        profile2 = new Profile(user2);
        profile2.setAvatarUri("avatar2.jpg");
        profile2.setFavoriteGames("Juego3, Juego4");
        profile2.setEmailNotifications(false);
        profile2.setPushNotifications(true);
        profile2 = profileRepository.save(profile2);
    }

    @Test
    @DisplayName("Debería encontrar un perfil por su usuario")
    void findByUser_WhenProfileExists_ShouldReturnProfile() {
        Optional<Profile> result = profileRepository.findByUser(user1);

        assertTrue(result.isPresent());
        assertEquals(profile1.getId(), result.get().getId());
        assertEquals(user1.getId(), result.get().getUser().getId());
        assertEquals("usuario1", result.get().getUser().getUsername());
    }

    @Test
    @DisplayName("Debería devolver Optional vacío cuando no existe un perfil para el usuario")
    void findByUser_WhenProfileDoesNotExist_ShouldReturnEmptyOptional() {
        User nonExistentUser = new User("usuario3", "password3", "usuario3@example.com");
        nonExistentUser.setId(999L); // Set a non-existent ID

        Optional<Profile> result = profileRepository.findByUser(nonExistentUser);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Debería encontrar un perfil por el nombre de usuario")
    void findByUserUsername_WhenProfileExists_ShouldReturnProfile() {
        Optional<Profile> result = profileRepository.findByUserUsername("usuario1");

        assertTrue(result.isPresent());
        assertEquals(profile1.getId(), result.get().getId());
        assertEquals(user1.getId(), result.get().getUser().getId());
        assertEquals("usuario1", result.get().getUser().getUsername());
    }

    @Test
    @DisplayName("Debería devolver Optional vacío cuando no existe un perfil para el nombre de usuario")
    void findByUserUsername_WhenProfileDoesNotExist_ShouldReturnEmptyOptional() {
        Optional<Profile> result = profileRepository.findByUserUsername("usuario3");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Debería establecer correctamente la relación bidireccional entre usuario y perfil")
    void shouldEstablishBidirectionalRelationship() {
        User newUser = new User("usuario3", "password3", "usuario3@example.com");
        newUser = userRepository.save(newUser);

        Profile newProfile = new Profile(newUser);
        newProfile.setAvatarUri("avatar3.jpg");
        newProfile = profileRepository.save(newProfile);

        Optional<Profile> foundProfile = profileRepository.findByUser(newUser);
        Optional<User> foundUser = userRepository.findById(newUser.getId());

        assertTrue(foundProfile.isPresent());
        assertTrue(foundUser.isPresent());
        
        assertEquals(newUser.getId(), foundProfile.get().getUser().getId());
        assertEquals(newProfile.getId(), foundUser.get().getProfile().getId());
    }

    @Test
    @DisplayName("Debería actualizar correctamente un perfil existente")
    void shouldUpdateExistingProfile() {
        profile1.setAvatarUri("updated-avatar.jpg");
        profile1.setFavoriteGames("UpdatedGame1, UpdatedGame2");
        profile1.setEmailNotifications(false);
        profile1.setPushNotifications(true);
        
        Profile updatedProfile = profileRepository.save(profile1);
        
        assertNotNull(updatedProfile);
        assertEquals("updated-avatar.jpg", updatedProfile.getAvatarUri());
        assertEquals("UpdatedGame1, UpdatedGame2", updatedProfile.getFavoriteGames());
        assertEquals(false, updatedProfile.isEmailNotifications());
        assertEquals(true, updatedProfile.isPushNotifications());
        
        assertEquals(user1.getId(), updatedProfile.getUser().getId());
    }
} 