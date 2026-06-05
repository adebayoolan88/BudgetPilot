package dev.adebayoolan.budgetpilot.repository;

import dev.adebayoolan.budgetpilot.model.IncomeStream;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface IncomeStreamRepository extends JpaRepository<IncomeStream, UUID> {

}
