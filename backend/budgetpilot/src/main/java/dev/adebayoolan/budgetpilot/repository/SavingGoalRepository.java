package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.SavingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SavingGoalRepository extends JpaRepository<SavingGoal, UUID> {

}
