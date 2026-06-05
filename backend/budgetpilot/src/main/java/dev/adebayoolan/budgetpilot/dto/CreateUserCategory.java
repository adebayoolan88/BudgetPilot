package dev.adebayoolan.budgetpilot.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class CreateUserCategory {
    UUID userId;
    UUID categoryId;

}
