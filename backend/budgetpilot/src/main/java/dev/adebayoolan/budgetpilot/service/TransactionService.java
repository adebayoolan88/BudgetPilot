package dev.adebayoolan.budgetpilot.service;

import org.springframework.stereotype.Service;

import dev.adebayoolan.budgetpilot.repository.CategoryRepository;
import dev.adebayoolan.budgetpilot.repository.TransactionRepository;
import dev.adebayoolan.budgetpilot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import dev.adebayoolan.budgetpilot.model.Transaction;
import dev.adebayoolan.budgetpilot.model.User;
import dev.adebayoolan.budgetpilot.model.Category;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public Transaction createTransaction(UUID userId, UUID categoryId, BigDecimal amount, String description,
            LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setDate(date);
        transaction.setCreatedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(UUID id) {
        return transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public List<Transaction> getTransactionsByUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return transactionRepository.findByUser(user);
    }

    public List<Transaction> getTransactionsByUserAndCategory(UUID userId, UUID categoryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return transactionRepository.findByUserAndCategory(user, category);
    }

    public List<Transaction> getTransactionsByDateRange(UUID userId, LocalDate start, LocalDate end) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return transactionRepository.findByUserAndDateBetween(user, start, end);
    }

    public Transaction updateTransaction(UUID id, UUID categoryId, BigDecimal amount, String description,
            LocalDate date) {
        Transaction transaction = getTransactionById(id);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        transaction.setCategory(category);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setDate(date);
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(UUID id) {
        transactionRepository.deleteById(id);
    }

}
