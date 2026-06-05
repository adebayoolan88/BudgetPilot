package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}
