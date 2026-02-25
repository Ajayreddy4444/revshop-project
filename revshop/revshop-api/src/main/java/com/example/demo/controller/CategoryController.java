package com.example.demo.controller;

import com.example.demo.dto.CategoryRequest;
import com.example.demo.dto.CategoryResponse;
import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest request) {
        try {
            Category category = categoryService.createCategory(request.getName(), request.getDescription());
            CategoryResponse response = new CategoryResponse(category.getId(), category.getName());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
