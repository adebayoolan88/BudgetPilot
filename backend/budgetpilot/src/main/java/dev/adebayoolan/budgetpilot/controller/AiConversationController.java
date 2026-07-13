package dev.adebayoolan.budgetpilot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.adebayoolan.budgetpilot.dto.CreateAiConversation;
import dev.adebayoolan.budgetpilot.model.AiConversation;
import dev.adebayoolan.budgetpilot.security.AuthenticatedUserService;
import dev.adebayoolan.budgetpilot.service.AiConversationService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class AiConversationController {

    private final AiConversationService aiConversationService;
    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<AiConversation> createConversation(@AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID userId,
            @RequestBody CreateAiConversation request) {
        authenticatedUserService.requireOwnership(jwt, userId);

        AiConversation conversation = aiConversationService.createConversation(
                userId,
                request.getMessage());
        return ResponseEntity.status(HttpStatus.CREATED).body(conversation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AiConversation> getConversation(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        AiConversation conversation = aiConversationService.getConversationById(id);
        authenticatedUserService.requireOwnership(jwt, conversation.getUser().getId());
        return ResponseEntity.ok(conversation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConversation(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        AiConversation existing = aiConversationService.getConversationById(id);
        authenticatedUserService.requireOwnership(jwt, existing.getUser().getId());

        aiConversationService.deleteConversation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AiConversation>> getUserConversations(@AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID userId) {
        authenticatedUserService.requireOwnership(jwt, userId);
        List<AiConversation> conversations = aiConversationService.getConversationByUser(userId);
        return ResponseEntity.ok(conversations);
    }

}
