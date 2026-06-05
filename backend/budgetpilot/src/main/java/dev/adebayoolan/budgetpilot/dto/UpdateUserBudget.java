package dev.adebayoolan.budgetpilot.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UpdateUserBudget {
    BigDecimal needsPercent;
    BigDecimal wantsPercent;
    BigDecimal savingsPercent;

}
