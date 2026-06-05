package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import dev.adebayoolan.budgetpilot.model.User;
import dev.adebayoolan.budgetpilot.model.Category;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUser(User user);

    List<Transaction> findByUserAndCategory(User user, Category category);

    List<Transaction> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);
}
