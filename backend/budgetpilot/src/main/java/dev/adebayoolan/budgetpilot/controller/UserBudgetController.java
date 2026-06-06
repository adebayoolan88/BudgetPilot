package dev.adebayoolan.budgetpilot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.adebayoolan.budgetpilot.dto.CreateUserBudget;
import dev.adebayoolan.budgetpilot.dto.UpdateUserBudget;
import dev.adebayoolan.budgetpilot.model.BudgetingStrategy;
import dev.adebayoolan.budgetpilot.model.UserBudget;
import dev.adebayoolan.budgetpilot.service.UserBudgetService;
import lombok.RequiredArgsConstructor;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/user-budgets")
@RequiredArgsConstructor
public class UserBudgetController {
    private final UserBudgetService userBudgetService;

    @PostMapping("")
    public ResponseEntity<UserBudget> createUserBudget(@RequestBody CreateUserBudget request) {
        UserBudget userBudget = userBudgetService.createUserBudget(
                request.getUserId(),
                request.getStrategyId(),
                request.getNeedsPercent(),
                request.getWantsPercent(),
                request.getSavingsPercent());
        return ResponseEntity.status(HttpStatus.CREATED).body(userBudget);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserBudget> getUserBudget(@PathVariable UUID userId) {
        return userBudgetService.getUserBudget(userId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserBudget> updateUserBudget(@PathVariable UUID userId,
            @RequestBody UpdateUserBudget request) {
        UserBudget userBudget = userBudgetService.updateUserBudget(
                userId,
                request.getNeedsPercent(),
                request.getWantsPercent(),
                request.getSavingsPercent());
        return ResponseEntity.ok(userBudget);
    }

    @GetMapping("/strategies")
    public ResponseEntity<List<BudgetingStrategy>> getAvailableStrategies() {
        List<BudgetingStrategy> strategies = userBudgetService.getAvailableStrategies();
        return ResponseEntity.ok(strategies);
    }

}