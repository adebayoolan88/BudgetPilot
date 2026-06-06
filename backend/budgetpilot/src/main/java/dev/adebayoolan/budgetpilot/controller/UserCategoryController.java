package dev.adebayoolan.budgetpilot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.adebayoolan.budgetpilot.dto.CreateUserCategory;
import dev.adebayoolan.budgetpilot.model.Category;
import dev.adebayoolan.budgetpilot.model.UserCategory;
import dev.adebayoolan.budgetpilot.service.UserCategoryService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-categories")
@RequiredArgsConstructor
public class UserCategoryController {

    private final UserCategoryService userCategoryService;

    @PostMapping
    public ResponseEntity<UserCategory> createUserCategory(@RequestBody CreateUserCategory request) {
        UserCategory userCategory = userCategoryService.addUserCategory(
                request.getUserId(),
                request.getCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userCategory);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserCategory>> getUserCategories(@PathVariable UUID userId) {
        List<UserCategory> userCategories = userCategoryService.getUserCategories(userId);
        return ResponseEntity.ok(userCategories);
    }

    @GetMapping("/defaults")
    public ResponseEntity<List<Category>> getDefaultCategories() {
        List<Category> categories = userCategoryService.getDefaultCategories();
        return ResponseEntity.ok(categories);
    }

    @DeleteMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<Void> removeUserCategory(@PathVariable UUID userId, @PathVariable UUID categoryId) {
        userCategoryService.removeUserCategory(userId, categoryId);
        return ResponseEntity.noContent().build();
    }
}