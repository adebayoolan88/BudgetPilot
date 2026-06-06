package dev.adebayoolan.budgetpilot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.adebayoolan.budgetpilot.model.BudgetingStrategy;
import dev.adebayoolan.budgetpilot.service.BudgetingStrategyService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budgeting-strategies")
@RequiredArgsConstructor
public class BudgetStrategyController {
    private final BudgetingStrategyService budgetingStrategyService;

    @GetMapping("")
    public ResponseEntity<List<BudgetingStrategy>> getAllStrategies() {
        List<BudgetingStrategy> strategies = budgetingStrategyService.getAllStrategies();
        return ResponseEntity.ok(strategies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetingStrategy> getStrategy(@PathVariable UUID id) {
        BudgetingStrategy strategy = budgetingStrategyService.getStrategyById(id);
        return ResponseEntity.ok(strategy);
    }
}
