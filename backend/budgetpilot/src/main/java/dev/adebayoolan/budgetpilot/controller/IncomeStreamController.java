package dev.adebayoolan.budgetpilot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.adebayoolan.budgetpilot.dto.CreateIncomeStream;
import dev.adebayoolan.budgetpilot.dto.UpdateIncomeStream;
import dev.adebayoolan.budgetpilot.model.IncomeStream;
import dev.adebayoolan.budgetpilot.service.IncomeStreamService;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.UUID;

@RestController
@RequestMapping("/api/income-streams")
@RequiredArgsConstructor
public class IncomeStreamController {

    private final IncomeStreamService incomeStreamService;

    @PostMapping("")
    public ResponseEntity<IncomeStream> createIncomeStream(@RequestBody CreateIncomeStream request) {
        IncomeStream incomeStream = incomeStreamService.createIncomeStream(
                request.getUserId(),
                request.getName(),
                request.getAmount(),
                request.getFrequency());
        return ResponseEntity.status(HttpStatus.CREATED).body(incomeStream);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeStream> getIncomeStreamById(@PathVariable UUID id) {
        IncomeStream incomeStream = incomeStreamService.getIncomeStreamById(id);

        return ResponseEntity.ok(incomeStream);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeStream> updateIncomeStream(@PathVariable UUID id,
            @RequestBody UpdateIncomeStream request) {
        IncomeStream incomeStream = incomeStreamService.updateIncomeStream(
                id,
                request.getName(),
                request.getAmount(),
                request.getFrequency());

        return ResponseEntity.ok(incomeStream);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<IncomeStream>> getIncomeStreamsByUser(@PathVariable UUID userId) {

        List<IncomeStream> incomeStream = incomeStreamService.getIncomeStreamsByUser(userId);
        return ResponseEntity.ok(incomeStream);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncomeStream(@PathVariable UUID id) {
        incomeStreamService.deleteIncomeStream(id);
        return ResponseEntity.noContent().build();
    }

}
