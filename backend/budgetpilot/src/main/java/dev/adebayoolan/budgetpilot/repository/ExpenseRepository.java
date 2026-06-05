package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import dev.adebayoolan.budgetpilot.model.User;
import dev.adebayoolan.budgetpilot.model.Category;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    List<Expense> findByUser(User user);

    List<Expense> findByUserAndCategory(User user, Category category);
}
