package dev.adebayoolan.budgetpilot.service;

import org.springframework.stereotype.Service;

import dev.adebayoolan.budgetpilot.repository.AiConversationRepository;
import dev.adebayoolan.budgetpilot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import dev.adebayoolan.budgetpilot.model.User;
import dev.adebayoolan.budgetpilot.model.AiConversation;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiConversationService {
    private final AiConversationRepository aiConversationRepository;
    private final UserRepository userRepository;

    public AiConversation createConversation(UUID userId, String message) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        AiConversation conversation = new AiConversation();

        conversation.setUser(user);
        conversation.setMessage(message);
        conversation.setCreatedAt(LocalDateTime.now());
        conversation.setResponse(""); // placeholder until Claude integration
        return aiConversationRepository.save(conversation);
    }

    public void deleteConversation(UUID id) {
        aiConversationRepository.deleteById(id);
    }

    public List<AiConversation> getConversationByUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return aiConversationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public AiConversation getConversationById(UUID id) {
        return aiConversationRepository.findById(id).orElseThrow(() -> new RuntimeException("Conversation not found"));
    }
}
