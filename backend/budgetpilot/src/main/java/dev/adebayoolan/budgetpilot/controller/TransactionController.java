package dev.adebayoolan.budgetpilot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.adebayoolan.budgetpilot.dto.CreateTransaction;
import dev.adebayoolan.budgetpilot.model.Transaction;
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

    @PostMapping("")
    public ResponseEntity<Transaction> createTransaction(@RequestBody CreateTransaction request) {
        Transaction transation = transactionService.createTransaction(
                request.getUserId(),
                request.getCategoryId(),
                request.getAmount(),
                request.getDescription(),
                request.getDate());

        return ResponseEntity.status(HttpStatus.CREATED).body(transation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable UUID id) {
        Transaction transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionByUser(@PathVariable UUID userId) {
        List<Transaction> transaction = transactionService.getTransactionsByUser(userId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<List<Transaction>> getTransactionByUserAndCategory(@PathVariable UUID userId,
            @PathVariable UUID categoryId) {
        List<Transaction> transaction = transactionService.getTransactionsByUserAndCategory(userId, categoryId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<Transaction>> getTransactionByDate(@PathVariable UUID userId,
            @RequestParam LocalDate start, @RequestParam LocalDate end) {
        List<Transaction> transactions = transactionService.getTransactionsByDateRange(userId, start, end);
        return ResponseEntity.ok(transactions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
