package dev.adebayoolan.budgetpilot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.adebayoolan.budgetpilot.dto.CreateExpense;
import dev.adebayoolan.budgetpilot.dto.UpdateExpense;
import dev.adebayoolan.budgetpilot.model.Expense;
import dev.adebayoolan.budgetpilot.security.AuthenticatedUserService;
import dev.adebayoolan.budgetpilot.service.ExpenseService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping("")
    public ResponseEntity<Expense> createExpense(@AuthenticationPrincipal Jwt jwt, @RequestBody CreateExpense request) {
        authenticatedUserService.requireOwnership(jwt, request.getUserId());

        Expense expense = expenseService.createExpense(
                request.getUserId(),
                request.getCategoryId(),
                request.getName(),
                request.getAmount(),
                request.getFrequency(),
                request.getDueDate(),
                request.getIsRecurring());

        return ResponseEntity.status(HttpStatus.CREATED).body(expense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        Expense expense = expenseService.getExpenseById(id);
        authenticatedUserService.requireOwnership(jwt, expense.getUser().getId());

        expenseService.deleteExpenseById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpense(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        Expense expense = expenseService.getExpenseById(id);
        authenticatedUserService.requireOwnership(jwt, expense.getUser().getId());
        return ResponseEntity.ok(expense);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Expense>> getUserExpenses(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID userId) {
        authenticatedUserService.requireOwnership(jwt, userId);
        List<Expense> expense = expenseService.getExpenseByUser(userId);
        return ResponseEntity.ok(expense);
    }

    @GetMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<List<Expense>> getUserExpensesByCategory(@AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID userId,
            @PathVariable UUID categoryId) {
        authenticatedUserService.requireOwnership(jwt, userId);
        List<Expense> expense = expenseService.getExpenseByUserAndCategory(userId, categoryId);
        return ResponseEntity.ok(expense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id,
            @RequestBody UpdateExpense request) {
        Expense existing = expenseService.getExpenseById(id);
        authenticatedUserService.requireOwnership(jwt, existing.getUser().getId());

        Expense expense = expenseService.updateExpense(
                id,
                request.getName(),
                request.getAmount(),
                request.getFrequency(),
                request.getIsRecurring(),
                request.getDueDate());

        return ResponseEntity.ok(expense);
    }

}
