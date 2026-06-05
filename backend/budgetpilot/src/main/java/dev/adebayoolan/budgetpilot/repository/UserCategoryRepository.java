package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserCategoryRepository extends JpaRepository<UserCategory, UUID> {

}
