package dev.adebayoolan.budgetpilot.controller;

import dev.adebayoolan.budgetpilot.dto.CreateUser;
import dev.adebayoolan.budgetpilot.dto.UpdateUser;
import dev.adebayoolan.budgetpilot.model.User;
import dev.adebayoolan.budgetpilot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUser user) {
        User created = userService.createUser(
                user.getClerkId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/clerk/{clerkId}")
    public ResponseEntity<User> getUserByClerkId(@PathVariable String clerkId) {
        return userService.getUserByClerkId(clerkId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody UpdateUser request) {
        User updated = userService.updateUser(id, request.getFirstName(), request.getLastName());
        return ResponseEntity.ok(updated);
    }
}