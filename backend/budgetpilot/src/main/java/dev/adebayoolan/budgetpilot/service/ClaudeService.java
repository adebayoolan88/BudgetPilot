package dev.adebayoolan.budgetpilot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.adebayoolan.budgetpilot.model.AiConversation;

@Service
public class ClaudeService {

    private final RestClient restClient;
    private final String model;
    private final boolean configured;

    public ClaudeService(@Value("${anthropic.api.key}") String apiKey,
            @Value("${anthropic.model}") String model) {
        this.model = model;
        this.configured = apiKey != null && !apiKey.isBlank();
        this.restClient = RestClient.builder()
                .baseUrl("https://api.anthropic.com/v1")
                .defaultHeader("x-api-key", apiKey)
                .defaultHeader("anthropic-version", "2023-06-01")
                .build();
    }

    public String sendMessage(String systemPrompt, List<AiConversation> history, String userMessage) {
        if (!configured) {
            return "The AI Assistant isn't configured yet — set ANTHROPIC_API_KEY in the backend .env to enable real responses.";
        }

        List<Message> messages = new ArrayList<>();
        for (AiConversation turn : history) {
            messages.add(new Message("user", turn.getMessage()));
            messages.add(new Message("assistant", turn.getResponse()));
        }
        messages.add(new Message("user", userMessage));

        MessageRequest request = new MessageRequest(model, 1024, systemPrompt, messages);

        MessageResponse response = restClient.post()
                .uri("/messages")
                .body(request)
                .retrieve()
                .body(MessageResponse.class);

        if (response == null || response.content() == null || response.content().isEmpty()) {
            return "I couldn't generate a response right now. Please try again in a moment.";
        }

        return response.content().get(0).text();
    }

    private record MessageRequest(
            String model,
            @JsonProperty("max_tokens") int maxTokens,
            String system,
            List<Message> messages) {
    }

    private record Message(String role, String content) {
    }

    private record MessageResponse(List<ContentBlock> content) {
    }

    private record ContentBlock(String type, String text) {
    }
}
