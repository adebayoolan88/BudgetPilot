package dev.adebayoolan.budgetpilot.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import dev.adebayoolan.budgetpilot.model.User;
import dev.adebayoolan.budgetpilot.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(String clerkId, String email, String firstName, String lastName) {
        User user = new User();
        user.setClerkId(clerkId);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setOnboardingComplete(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Optional<User> getUserByClerkId(String clerkId) {
        return userRepository.findByClerkId(clerkId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id"));
    }

    public User updateUser(UUID id, String firstName, String lastName) {
        User user = getUserById(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

}
