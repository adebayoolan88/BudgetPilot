package dev.adebayoolan.budgetpilot.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class UpdateExpense {
    String name;
    BigDecimal amount;
    String frequency;
    LocalDate dueDate;
    Boolean isRecurring;
}
