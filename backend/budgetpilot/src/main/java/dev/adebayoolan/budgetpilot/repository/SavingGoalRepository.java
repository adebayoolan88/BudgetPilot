package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.SavingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import dev.adebayoolan.budgetpilot.model.User;

public interface SavingGoalRepository extends JpaRepository<SavingGoal, UUID> {
    List<SavingGoal> findByUser(User user);
}
