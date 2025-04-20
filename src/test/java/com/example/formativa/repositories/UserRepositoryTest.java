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

import com.example.formativa.models.User;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        user1 = new User("usuario1", "password1", "usuario1@example.com");
        user1 = userRepository.save(user1);

        user2 = new User("usuario2", "password2", "usuario2@example.com");
        user2 = userRepository.save(user2);

        user3 = new User("usuario3", "password3", "usuario3@example.com");
        user3 = userRepository.save(user3);
    }

    @Test
    @DisplayName("Debería encontrar un usuario por su nombre de usuario")
    void findByUsername_WhenUserExists_ShouldReturnUser() {
        Optional<User> result = userRepository.findByUsername("usuario1");

        assertTrue(result.isPresent());
        assertEquals(user1.getId(), result.get().getId());
        assertEquals("usuario1", result.get().getUsername());
        assertEquals("password1", result.get().getPassword());
        assertEquals("usuario1@example.com", result.get().getEmail());
    }

    @Test
    @DisplayName("Debería devolver Optional vacío cuando no existe un usuario con ese nombre de usuario")
    void findByUsername_WhenUserDoesNotExist_ShouldReturnEmptyOptional() {
        Optional<User> result = userRepository.findByUsername("usuario4");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Debería verificar que existe un usuario con ese nombre de usuario")
    void existsByUsername_WhenUserExists_ShouldReturnTrue() {
        boolean result = userRepository.existsByUsername("usuario1");

        assertTrue(result);
    }

    @Test
    @DisplayName("Debería verificar que no existe un usuario con ese nombre de usuario")
    void existsByUsername_WhenUserDoesNotExist_ShouldReturnFalse() {
        boolean result = userRepository.existsByUsername("usuario4");

        assertFalse(result);
    }

    @Test
    @DisplayName("Debería verificar que existe un usuario con ese email")
    void existsByEmail_WhenUserExists_ShouldReturnTrue() {
        boolean result = userRepository.existsByEmail("usuario1@example.com");

        assertTrue(result);
    }

    @Test
    @DisplayName("Debería verificar que no existe un usuario con ese email")
    void existsByEmail_WhenUserDoesNotExist_ShouldReturnFalse() {
        boolean result = userRepository.existsByEmail("usuario4@example.com");

        assertFalse(result);
    }

    @Test
    @DisplayName("Debería guardar un nuevo usuario correctamente")
    void shouldSaveNewUser() {
        User newUser = new User("usuario4", "password4", "usuario4@example.com");

        User savedUser = userRepository.save(newUser);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("usuario4", savedUser.getUsername());
        assertEquals("password4", savedUser.getPassword());
        assertEquals("usuario4@example.com", savedUser.getEmail());
    }

    @Test
    @DisplayName("Debería actualizar un usuario existente correctamente")
    void shouldUpdateExistingUser() {
        user1.setEmail("usuario1_actualizado@example.com");
        user1.setPassword("password1_actualizado");

        User updatedUser = userRepository.save(user1);

        assertNotNull(updatedUser);
        assertEquals(user1.getId(), updatedUser.getId());
        assertEquals("usuario1", updatedUser.getUsername());
        assertEquals("password1_actualizado", updatedUser.getPassword());
        assertEquals("usuario1_actualizado@example.com", updatedUser.getEmail());
    }

    @Test
    @DisplayName("Debería eliminar un usuario correctamente")
    void shouldDeleteUser() {
        userRepository.delete(user1);

        assertFalse(userRepository.existsByUsername("usuario1"));
        assertFalse(userRepository.existsByEmail("usuario1@example.com"));
    }

    @Test
    @DisplayName("Debería encontrar todos los usuarios")
    void shouldFindAllUsers() {
        var users = userRepository.findAll();

        assertNotNull(users);
        assertEquals(3, users.size());
    }
} 