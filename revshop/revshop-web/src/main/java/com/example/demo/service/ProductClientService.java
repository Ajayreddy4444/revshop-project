package com.example.demo.service;

import com.example.demo.dto.Category;
import com.example.demo.dto.ProductDto;

import java.util.List;

public interface ProductClientService {

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);

    void createProduct(ProductDto product, Long sellerId);

    List<Category> getAllCategories();

    List<ProductDto> getSellerProducts(Long sellerId);

    Category createCategory(String name, String description);
}
