package io.nuwe.technical.api.services;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.repositories.*;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves every user in the db.
     * @return List of users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by its ID.
     * @param id User ID to search.
     * @return An optional of the user.
     */
    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    /**
     * Creates a user entry in the db.
     * @param user User to create.
     * @return Created user.
     */
    public User createUser(User user) {
        user.setSubscribed(true);
        return userRepository.save(user);
    }

    /**
     * Deletes the desired user from the db.
     * @param user User to delete.
     */
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    /**
     * Deletes every user from the db.
     */
    public void deleteAll() {
        userRepository.deleteAll();
    }

    /**
     * Checks if a user exists searching by email.
     * @param email Email to search.
     * @return True if the user exists, false otherwise.
     */
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}