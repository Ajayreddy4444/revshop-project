package com.example.demo.service;

import com.example.demo.dto.Category;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductRequest;

import java.util.List;

public interface ProductClientService {

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);

    void createProduct(ProductRequest product, Long sellerId);

    List<Category> getAllCategories();

    List<ProductDto> getSellerProducts(Long sellerId);

    Category createCategory(String name, String description);

    void updateProduct(Long id, ProductRequest product);

    void updateStock(Long id, Integer quantity);

    void deleteProduct(Long id);

    List<ProductDto> searchProducts(String keyword,
                                    Long categoryId,
                                    Double minPrice,
                                    Double maxPrice);
}
