package dev.adebayoolan.budgetpilot.dto;

import lombok.Data;

@Data
public class CreateCategory {
    String name;
    String type;
    String color;
    Boolean isDefault;
}
