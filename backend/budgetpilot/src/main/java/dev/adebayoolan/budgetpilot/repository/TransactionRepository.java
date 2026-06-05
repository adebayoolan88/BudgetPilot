package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

}
