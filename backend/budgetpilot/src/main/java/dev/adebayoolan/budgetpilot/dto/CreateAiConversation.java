package dev.adebayoolan.budgetpilot.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class CreateAiConversation {
    UUID userId;
    String message;
}
