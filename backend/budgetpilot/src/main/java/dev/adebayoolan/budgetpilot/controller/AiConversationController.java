package dev.adebayoolan.budgetpilot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.adebayoolan.budgetpilot.dto.CreateAiConversation;
import dev.adebayoolan.budgetpilot.model.AiConversation;
import dev.adebayoolan.budgetpilot.service.AiConversationService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class AiConversationController {

    private final AiConversationService aiConversationService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<AiConversation> createConversation(@PathVariable UUID userId,
            @RequestBody CreateAiConversation request) {
        AiConversation conversation = aiConversationService.createConversation(
                userId,
                request.getMessage());
        return ResponseEntity.status(HttpStatus.CREATED).body(conversation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AiConversation> getConversation(@PathVariable UUID id) {
        AiConversation conversation = aiConversationService.getConversationById(id);
        return ResponseEntity.ok(conversation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable UUID id) {
        aiConversationService.deleteConversation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AiConversation>> getUserConversations(@PathVariable UUID userId) {
        List<AiConversation> conversations = aiConversationService.getConversationByUser(userId);
        return ResponseEntity.ok(conversations);
    }

}
