package dev.adebayoolan.budgetpilot.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class CreateIncomeStream {
    UUID userId;
    String name;
    BigDecimal amount;
    String frequency;

}
