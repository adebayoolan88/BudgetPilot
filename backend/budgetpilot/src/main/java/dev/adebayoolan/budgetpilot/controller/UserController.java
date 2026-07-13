package dev.adebayoolan.budgetpilot.controller;

import dev.adebayoolan.budgetpilot.dto.CreateUser;
import dev.adebayoolan.budgetpilot.dto.UpdateUser;
import dev.adebayoolan.budgetpilot.model.User;
import dev.adebayoolan.budgetpilot.security.AuthenticatedUserService;
import dev.adebayoolan.budgetpilot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping
    public ResponseEntity<User> createUser(@AuthenticationPrincipal Jwt jwt, @RequestBody CreateUser user) {
        User created = userService.createUser(
                jwt.getSubject(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        authenticatedUserService.requireOwnership(jwt, id);
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/clerk/{clerkId}")
    public ResponseEntity<User> getUserByClerkId(@AuthenticationPrincipal Jwt jwt, @PathVariable String clerkId) {
        if (!clerkId.equals(jwt.getSubject())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized for this user");
        }
        return userService.getUserByClerkId(clerkId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id,
            @RequestBody UpdateUser request) {
        authenticatedUserService.requireOwnership(jwt, id);
        User updated = userService.updateUser(id, request.getFirstName(), request.getLastName());
        return ResponseEntity.ok(updated);
    }
}
