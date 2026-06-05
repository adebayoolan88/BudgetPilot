package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.BudgetingStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BudgetingStrategyRepository extends JpaRepository<BudgetingStrategy, UUID> {

}
