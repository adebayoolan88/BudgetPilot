package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.AiConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import dev.adebayoolan.budgetpilot.model.User;

public interface AiConversationRepository extends JpaRepository<AiConversation, UUID> {
    List<AiConversation> findByUserOrderByCreatedAtDesc(User user);
}
