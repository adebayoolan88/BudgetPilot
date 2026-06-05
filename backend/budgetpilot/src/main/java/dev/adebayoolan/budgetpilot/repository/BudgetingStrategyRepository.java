package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.BudgetingStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface BudgetingStrategyRepository extends JpaRepository<BudgetingStrategy, UUID> {
    List<BudgetingStrategy> findByIsCustomFalse();
}
