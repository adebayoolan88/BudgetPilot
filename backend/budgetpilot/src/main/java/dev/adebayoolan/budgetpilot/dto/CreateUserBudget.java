package dev.adebayoolan.budgetpilot.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class CreateUserBudget {
    UUID userId;
    UUID strategyId;
    BigDecimal needsPercent;
    BigDecimal wantsPercent;
    BigDecimal savingsPercent;

}
