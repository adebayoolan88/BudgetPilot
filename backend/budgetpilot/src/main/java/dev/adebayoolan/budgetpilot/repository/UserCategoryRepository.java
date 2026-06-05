package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import dev.adebayoolan.budgetpilot.model.User;

public interface UserCategoryRepository extends JpaRepository<UserCategory, UUID> {
    List<UserCategory> findByUser(User user);
}
