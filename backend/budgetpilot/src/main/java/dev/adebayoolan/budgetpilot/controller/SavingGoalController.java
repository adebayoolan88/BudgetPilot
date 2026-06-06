package dev.adebayoolan.budgetpilot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.adebayoolan.budgetpilot.dto.CreateSavingsGoal;
import dev.adebayoolan.budgetpilot.dto.UpdateSavingsGoal;
import dev.adebayoolan.budgetpilot.model.SavingGoal;
import dev.adebayoolan.budgetpilot.service.SavingGoalService;
import lombok.RequiredArgsConstructor;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/saving-goals")
@RequiredArgsConstructor
public class SavingGoalController {
    private final SavingGoalService savingGoalService;

    @PostMapping("")
    public ResponseEntity<SavingGoal> createSavingGoal(@RequestBody CreateSavingsGoal request) {
        SavingGoal savingGoal = savingGoalService.createSavingGoal(
                request.getUserId(),
                request.getName(),
                request.getTargetAmount(),
                request.getCurrentAmount(),
                request.getDeadline());
        return ResponseEntity.status(HttpStatus.CREATED).body(savingGoal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSavingGoal(@PathVariable UUID id) {
        savingGoalService.deleteSavingGoal(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SavingGoal> updateSavingGoal(@PathVariable UUID id, @RequestBody UpdateSavingsGoal request) {
        SavingGoal savingGoal = savingGoalService.updateSavingGoal(
                id,
                request.getName(),
                request.getTargetAmount(),
                request.getCurrentAmount(),
                request.getDeadline());

        return ResponseEntity.ok(savingGoal);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavingGoal> getSavingGoalById(@PathVariable UUID id) {
        SavingGoal savingGoal = savingGoalService.getSavingGoalById(id);
        return ResponseEntity.ok(savingGoal);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SavingGoal>> getSavingGoalByUser(@PathVariable UUID userId) {
        List<SavingGoal> savingGoal = savingGoalService.getSavingGoalsByUser(userId);
        return ResponseEntity.ok(savingGoal);
    }

}
