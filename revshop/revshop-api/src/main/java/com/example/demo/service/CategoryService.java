package com.example.demo.service;

import com.example.demo.dto.CategoryResponse;
import com.example.demo.entity.Category;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    Category createCategory(String name, String description);
}
