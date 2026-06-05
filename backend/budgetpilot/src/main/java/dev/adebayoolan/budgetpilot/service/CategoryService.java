package dev.adebayoolan.budgetpilot.service;

import org.springframework.stereotype.Service;

import dev.adebayoolan.budgetpilot.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import dev.adebayoolan.budgetpilot.model.Category;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category createCategory(String name, String type, String color, Boolean isDefault) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setColor(color);
        category.setIsDefault(isDefault);
        category.setCreatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getDefaultCategories() {
        return categoryRepository.findByIsDefaultTrue();
    }

    public Category updateCategory(UUID id, String name, String type, String color) {
        Category category = getCategoryById(id);
        category.setName(name);
        category.setType(type);
        category.setColor(color);

        return categoryRepository.save(category);
    }

    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }
}
