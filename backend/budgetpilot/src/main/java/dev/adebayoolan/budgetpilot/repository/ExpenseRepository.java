package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

}
