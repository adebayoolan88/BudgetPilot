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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.adebayoolan.budgetpilot.dto.CreateTransaction;
import dev.adebayoolan.budgetpilot.dto.UpdateTransaction;
import dev.adebayoolan.budgetpilot.model.Transaction;
import dev.adebayoolan.budgetpilot.security.AuthenticatedUserService;
import dev.adebayoolan.budgetpilot.service.TransactionService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping("")
    public ResponseEntity<Transaction> createTransaction(@AuthenticationPrincipal Jwt jwt,
            @RequestBody CreateTransaction request) {
        authenticatedUserService.requireOwnership(jwt, request.getUserId());

        Transaction transation = transactionService.createTransaction(
                request.getUserId(),
                request.getCategoryId(),
                request.getAmount(),
                request.getDescription(),
                request.getDate());

        return ResponseEntity.status(HttpStatus.CREATED).body(transation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        Transaction transaction = transactionService.getTransactionById(id);
        authenticatedUserService.requireOwnership(jwt, transaction.getUser().getId());
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionByUser(@AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID userId) {
        authenticatedUserService.requireOwnership(jwt, userId);
        List<Transaction> transaction = transactionService.getTransactionsByUser(userId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<List<Transaction>> getTransactionByUserAndCategory(@AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID userId,
            @PathVariable UUID categoryId) {
        authenticatedUserService.requireOwnership(jwt, userId);
        List<Transaction> transaction = transactionService.getTransactionsByUserAndCategory(userId, categoryId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<Transaction>> getTransactionByDate(@AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID userId,
            @RequestParam LocalDate start, @RequestParam LocalDate end) {
        authenticatedUserService.requireOwnership(jwt, userId);
        List<Transaction> transactions = transactionService.getTransactionsByDateRange(userId, start, end);
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id,
            @RequestBody UpdateTransaction request) {
        Transaction existing = transactionService.getTransactionById(id);
        authenticatedUserService.requireOwnership(jwt, existing.getUser().getId());

        Transaction transaction = transactionService.updateTransaction(
                id,
                request.getCategoryId(),
                request.getAmount(),
                request.getDescription(),
                request.getDate());

        return ResponseEntity.ok(transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        Transaction existing = transactionService.getTransactionById(id);
        authenticatedUserService.requireOwnership(jwt, existing.getUser().getId());

        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
