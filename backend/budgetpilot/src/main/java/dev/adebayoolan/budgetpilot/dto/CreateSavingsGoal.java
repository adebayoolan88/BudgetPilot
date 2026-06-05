package dev.adebayoolan.budgetpilot.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class CreateSavingsGoal {
    UUID userId;
    String name;
    BigDecimal targetAmount;
    BigDecimal currentAmount;
    LocalDate deadline;

}
