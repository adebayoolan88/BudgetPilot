package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.IncomeStream;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import dev.adebayoolan.budgetpilot.model.User;

public interface IncomeStreamRepository extends JpaRepository<IncomeStream, UUID> {
    List<IncomeStream> findByUser(User user);
}
