package dev.adebayoolan.budgetpilot.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import dev.adebayoolan.budgetpilot.repository.CategoryRepository;
import dev.adebayoolan.budgetpilot.repository.UserCategoryRepository;
import dev.adebayoolan.budgetpilot.repository.UserRepository;
import dev.adebayoolan.budgetpilot.model.User;
import dev.adebayoolan.budgetpilot.model.Category;
import dev.adebayoolan.budgetpilot.model.UserCategory;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserCategoryService {
    private final UserCategoryRepository userCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public UserCategory addUserCategory(UUID userId, UUID categoryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        UserCategory userCategory = new UserCategory();
        userCategory.setUser(user);
        userCategory.setCategory(category);
        userCategory.setCreatedAt(LocalDateTime.now());

        return userCategoryRepository.save(userCategory);
    }

    public List<UserCategory> getUserCategories(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userCategoryRepository.findByUser(user);
    }

    public void removeUserCategory(UUID userId, UUID categoryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository
                .findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));

        userCategoryRepository.deleteByUserAndCategory(user, category);
    }

    public List<Category> getDefaultCategories() {
        return categoryRepository.findByIsDefaultTrue();
    }

}
