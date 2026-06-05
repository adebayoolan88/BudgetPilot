package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.UserBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserBudgetRepository extends JpaRepository<UserBudget, UUID> {

}
