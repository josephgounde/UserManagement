package com.example.userManagement.controller;

import com.example.userManagement.model.User;
import com.example.userManagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "users", description = "Endpoints for managing users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "user registration!")
    public User registerUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    @Operation(summary = "Get all users or filter by emails")
    public List<User> getAllUsers(@RequestParam(required = false) String email) {
        if(email != null){
            return userService.getUserByEmail(email);
        }
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get users by ID")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
