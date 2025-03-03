package io.nuwe.technical.api.controllers;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.services.*;
import io.nuwe.technical.api.grpc.GrpcClientService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GrpcClientService grpcClientService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        if (isInvalidId(id)) {
            return createBadRequestResponseForUser();
        }

        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(this::createNotFoundResponseForUser);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (isInvalidUser(user) || userService.existsByEmail(user.getEmail())) {
            return createBadRequestResponseForUser();
        }

        User createdUser = userService.createUser(user);
        return createCreatedResponse(createdUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") long id) {
        if (isInvalidId(id)) {
            return createBadRequestResponseForVoid();
        }

        return userService.getUserById(id)
                .map(this::deleteUserAndCreateOkResponse)
                .orElseGet(this::createNotFoundResponseForVoid);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllUsers() {
        userService.deleteAll();
        return ResponseEntity.ok().build();
    }

    private boolean isInvalidId(long id) {
        return id <= 0;
    }

    private boolean isInvalidUser(User user) {
        return user.getEmail().isEmpty() || user.getName().isEmpty() || user.getAge() <= 0;
    }

    private ResponseEntity<Void> createBadRequestResponseForVoid() {
        return ResponseEntity.badRequest().build();
    }

    private ResponseEntity<User> createBadRequestResponseForUser() {
        return ResponseEntity.badRequest().build();
    }

    private ResponseEntity<Void> createNotFoundResponseForVoid() {
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<User> createNotFoundResponseForUser() {
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<User> createCreatedResponse(User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    private ResponseEntity<Void> deleteUserAndCreateOkResponse(User user) {
        userService.deleteUser(user);
        return ResponseEntity.ok().build();
    }
}
