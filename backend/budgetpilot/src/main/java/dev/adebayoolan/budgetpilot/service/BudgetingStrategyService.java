package dev.adebayoolan.budgetpilot.service;

import org.springframework.stereotype.Service;

import dev.adebayoolan.budgetpilot.repository.BudgetingStrategyRepository;
import dev.adebayoolan.budgetpilot.model.BudgetingStrategy;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetingStrategyService {
    private final BudgetingStrategyRepository budgetingStrategyRepository;

    public List<BudgetingStrategy> getAllStrategies() {
        return budgetingStrategyRepository.findByIsCustomFalse();
    }

    public BudgetingStrategy getStrategyById(UUID id) {
        return budgetingStrategyRepository.findById(id).orElseThrow(() -> new RuntimeException("Strategy not found"));
    }

}
