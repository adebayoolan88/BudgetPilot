package dev.adebayoolan.budgetpilot.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import dev.adebayoolan.budgetpilot.model.IncomeStream;
import dev.adebayoolan.budgetpilot.repository.IncomeStreamRepository;
import dev.adebayoolan.budgetpilot.repository.UserRepository;
import dev.adebayoolan.budgetpilot.model.User;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeStreamService {
    private final IncomeStreamRepository incomeStreamRepository;
    private final UserRepository userRepository;

    public IncomeStream createIncomeStream(UUID userId, String name, BigDecimal amount, String frequency) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        IncomeStream incomeStream = new IncomeStream();
        incomeStream.setUser(user);
        incomeStream.setName(name);
        incomeStream.setAmount(amount);
        incomeStream.setFrequency(frequency);
        incomeStream.setCreatedAt(LocalDateTime.now());
        incomeStream.setUpdatedAt(LocalDateTime.now());

        return incomeStreamRepository.save(incomeStream);
    }

    public IncomeStream getIncomeStreamById(UUID id) {
        return incomeStreamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income stream not found"));
    }

    public IncomeStream updateIncomeStream(UUID id, String name, BigDecimal amount, String frequency) {
        IncomeStream incomeStream = getIncomeStreamById(id);
        incomeStream.setName(name);
        incomeStream.setAmount(amount);
        incomeStream.setFrequency(frequency);
        incomeStream.setUpdatedAt(LocalDateTime.now());
        return incomeStreamRepository.save(incomeStream);
    }

    public List<IncomeStream> getIncomeStreamsByUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return incomeStreamRepository.findByUser(user);
    }

    public void deleteIncomeStream(UUID id) {
        incomeStreamRepository.deleteById(id);
    }
}
