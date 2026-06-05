package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.AiConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AiConversationRepository extends JpaRepository<AiConversation, UUID> {

}
