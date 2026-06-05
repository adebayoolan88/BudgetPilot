package dev.adebayoolan.budgetpilot.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UpdateSavingsGoal {
    String name;
    BigDecimal targetAmount;
    BigDecimal currentAmount;
    LocalDate deadline;

}
