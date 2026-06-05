package dev.adebayoolan.budgetpilot.dto;

import java.util.UUID;
import java.time.LocalDate;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateExpense {
    UUID userId;
    UUID categoryId;
    String name;
    BigDecimal amount;
    String frequency;
    LocalDate dueDate;
    Boolean isRecurring;
}
