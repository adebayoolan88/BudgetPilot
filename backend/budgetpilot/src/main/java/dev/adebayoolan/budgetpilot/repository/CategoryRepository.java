package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByIsDefaultTrue();
}
