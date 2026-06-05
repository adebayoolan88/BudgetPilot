package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import dev.adebayoolan.budgetpilot.model.User;
import dev.adebayoolan.budgetpilot.model.Category;;

public interface UserCategoryRepository extends JpaRepository<UserCategory, UUID> {
    List<UserCategory> findByUser(User user);

    void deleteByUserAndCategory(User user, Category category);
}
