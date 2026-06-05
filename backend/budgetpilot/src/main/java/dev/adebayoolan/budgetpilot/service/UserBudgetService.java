package dev.adebayoolan.budgetpilot.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import dev.adebayoolan.budgetpilot.model.User;
import dev.adebayoolan.budgetpilot.repository.UserBudgetRepository;
import dev.adebayoolan.budgetpilot.repository.BudgetingStrategyRepository;
import dev.adebayoolan.budgetpilot.repository.UserRepository;
import dev.adebayoolan.budgetpilot.model.UserBudget;
import dev.adebayoolan.budgetpilot.model.BudgetingStrategy;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBudgetService {
    private final UserRepository userRepository;
    private final BudgetingStrategyRepository budgetingStrategyRepository;
    private final UserBudgetRepository userBudgetRepository;

    public UserBudget createUserBudget(UUID userId, UUID strategyId, BigDecimal needsPercent, BigDecimal wantsPercent,
            BigDecimal savingsPercent) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BudgetingStrategy strategy = budgetingStrategyRepository
                .findById(strategyId).orElseThrow(() -> new RuntimeException("Strategy not found"));

        UserBudget userBudget = new UserBudget();

        userBudget.setUser(user);
        userBudget.setStrategy(strategy);
        userBudget.setNeedsPercent(needsPercent);
        userBudget.setWantsPercent(wantsPercent);
        userBudget.setSavingsPercent(savingsPercent);
        userBudget.setCreatedAt(LocalDateTime.now());
        userBudget.setUpdatedAt(LocalDateTime.now());

        return userBudgetRepository.save(userBudget);
    }

    public Optional<UserBudget> getUserBudget(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userBudgetRepository.findByUser(user);
    }

    public UserBudget updateUserBudget(UUID userId, BigDecimal needsPercent, BigDecimal wantsPercent,
            BigDecimal savingsPercent) {
        UserBudget userBudget = getUserBudget(userId)
                .orElseThrow(() -> new RuntimeException("User budget not found"));

        userBudget.setWantsPercent(wantsPercent);
        userBudget.setNeedsPercent(needsPercent);
        userBudget.setSavingsPercent(savingsPercent);
        userBudget.setUpdatedAt(LocalDateTime.now());

        return userBudgetRepository.save(userBudget);
    }

    public List<BudgetingStrategy> getAvailableStrategies() {
        return budgetingStrategyRepository.findByIsCustomFalse();
    }
}
