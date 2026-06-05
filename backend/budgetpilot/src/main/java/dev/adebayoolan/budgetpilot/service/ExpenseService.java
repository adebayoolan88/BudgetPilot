package dev.adebayoolan.budgetpilot.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import dev.adebayoolan.budgetpilot.repository.UserRepository;
import dev.adebayoolan.budgetpilot.repository.ExpenseRepository;
import dev.adebayoolan.budgetpilot.repository.CategoryRepository;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import dev.adebayoolan.budgetpilot.model.User;
import dev.adebayoolan.budgetpilot.model.Expense;
import dev.adebayoolan.budgetpilot.model.Category;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ExpenseService {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public Expense createExpense(UUID userId, UUID categoryId, String name, BigDecimal amount, String frequency,
            LocalDate dueDate, Boolean isRecurring) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Expense expense = new Expense();
        expense.setUser(user);
        expense.setCategory(category);
        expense.setName(name);
        expense.setAmount(amount);
        expense.setFrequency(frequency);
        expense.setDueDate(dueDate);
        expense.setIsRecurring(isRecurring);
        expense.setCreatedAt(LocalDateTime.now());
        expense.setUpdatedAt(LocalDateTime.now());

        return expenseRepository.save(expense);
    }

    public void deleteExpenseById(UUID id) {
        expenseRepository.deleteById(id);
    }

    public Expense getExpenseById(UUID id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

    }

    public List<Expense> getExpenseByUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return expenseRepository.findByUser(user);

    }

    public List<Expense> getExpenseByUserAndCategory(UUID userId, UUID categoryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return expenseRepository.findByUserAndCategory(user, category);
    }

    public Expense updateExpense(UUID id, String name, BigDecimal amount, String frequency, Boolean isRecurring,
            LocalDate dueDate) {
        Expense expense = getExpenseById(id);
        expense.setName(name);
        expense.setAmount(amount);
        expense.setFrequency(frequency);
        expense.setIsRecurring(isRecurring);
        expense.setDueDate(dueDate);
        expense.setUpdatedAt(LocalDateTime.now());

        return expenseRepository.save(expense);
    }

}
