package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByClerkId(String clerkId);

    Optional<User> findByEmail(String email);

}
