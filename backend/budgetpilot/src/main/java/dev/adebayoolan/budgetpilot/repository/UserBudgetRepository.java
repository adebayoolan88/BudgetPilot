package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.UserBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;
import dev.adebayoolan.budgetpilot.model.User;

public interface UserBudgetRepository extends JpaRepository<UserBudget, UUID> {
    Optional<UserBudget> findByUser(User user);
}
