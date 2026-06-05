package dev.adebayoolan.budgetpilot.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Table(name = "budgeting_strategies")
public class BudgetingStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "needs_percent")
    private BigDecimal needsPercent;

    @Column(name = "wants_percent")
    private BigDecimal wantsPercent;

    @Column(name = "savings_percent")
    private BigDecimal savingsPercent;

    @Column(name = "is_custom")
    private Boolean isCustom;
}