package dev.adebayoolan.budgetpilot.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class CreateTransaction {
    UUID userId;
    UUID categoryId;
    BigDecimal amount;
    String description;
    LocalDate date;
}
