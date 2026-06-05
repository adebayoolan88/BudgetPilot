package dev.adebayoolan.budgetpilot.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class UpdateIncomeStream {
    String name;
    BigDecimal amount;
    String frequency;

}
