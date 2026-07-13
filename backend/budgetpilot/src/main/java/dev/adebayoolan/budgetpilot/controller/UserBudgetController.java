package dev.adebayoolan.budgetpilot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
import dev.adebayoolan.budgetpilot.security.AuthenticatedUserService;
import dev.adebayoolan.budgetpilot.service.UserBudgetService;
import lombok.RequiredArgsConstructor;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/user-budgets")
@RequiredArgsConstructor
public class UserBudgetController {
    private final UserBudgetService userBudgetService;
    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping("")
    public ResponseEntity<UserBudget> createUserBudget(@AuthenticationPrincipal Jwt jwt,
            @RequestBody CreateUserBudget request) {
        authenticatedUserService.requireOwnership(jwt, request.getUserId());

        UserBudget userBudget = userBudgetService.createUserBudget(
                request.getUserId(),
                request.getStrategyId(),
                request.getNeedsPercent(),
                request.getWantsPercent(),
                request.getSavingsPercent());
        return ResponseEntity.status(HttpStatus.CREATED).body(userBudget);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserBudget> getUserBudget(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID userId) {
        authenticatedUserService.requireOwnership(jwt, userId);
        return userBudgetService.getUserBudget(userId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserBudget> updateUserBudget(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID userId,
            @RequestBody UpdateUserBudget request) {
        authenticatedUserService.requireOwnership(jwt, userId);

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
