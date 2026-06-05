package dev.adebayoolan.budgetpilot.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import dev.adebayoolan.budgetpilot.repository.SavingGoalRepository;
import dev.adebayoolan.budgetpilot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import dev.adebayoolan.budgetpilot.model.User;
import dev.adebayoolan.budgetpilot.model.SavingGoal;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavingGoalService {
    private final SavingGoalRepository savingGoalRepository;
    private final UserRepository userRepository;

    public SavingGoal createSavingGoal(UUID userId, String name, BigDecimal targetAmount, BigDecimal currentAmount,
            LocalDate deadline) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SavingGoal savingGoal = new SavingGoal();
        savingGoal.setUser(user);
        savingGoal.setName(name);
        savingGoal.setTargetAmount(targetAmount);
        savingGoal.setCurrentAmount(currentAmount);
        savingGoal.setDeadline(deadline);
        savingGoal.setCreatedAt(LocalDateTime.now());
        savingGoal.setUpdatedAt(LocalDateTime.now());
        return savingGoalRepository.save(savingGoal);

    }

    public void deleteSavingGoal(UUID id) {
        savingGoalRepository.deleteById(id);
    }

    public SavingGoal updateSavingGoal(UUID id, String name, BigDecimal targetAmount, BigDecimal currentAmount,
            LocalDate deadline) {
        SavingGoal savingGoal = getSavingGoalById(id);
        savingGoal.setName(name);
        savingGoal.setTargetAmount(targetAmount);
        savingGoal.setCurrentAmount(currentAmount);
        savingGoal.setDeadline(deadline);
        savingGoal.setUpdatedAt(LocalDateTime.now());
        return savingGoalRepository.save(savingGoal);
    }

    public SavingGoal getSavingGoalById(UUID id) {
        return savingGoalRepository.findById(id).orElseThrow(() -> new RuntimeException("Saving goal not found"));
    }

    public List<SavingGoal> getSavingGoalsByUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return savingGoalRepository.findByUser(user);

    }
}
