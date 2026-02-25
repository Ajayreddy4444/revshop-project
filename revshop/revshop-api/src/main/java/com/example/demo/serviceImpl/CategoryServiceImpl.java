package com.example.demo.serviceImpl;

import com.example.demo.dto.CategoryResponse;
import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::mapToCategory)
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        return mapToCategory(category);
    }

    @Override
    public Category createCategory(String name, String description) {
        Category exists = categoryRepository.findByNameIgnoreCase(name);

        if (exists != null) {
            throw new RuntimeException("Category already exists");
        }
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);

        return categoryRepository.save(category);
    }

    private CategoryResponse mapToCategory(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }
}
