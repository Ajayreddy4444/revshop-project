package com.example.demo.service;


import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    ProductResponse createProduct(ProductRequest productRequest, Long sellerId);

    List<ProductResponse> getProductsBySeller(Long sellerId);

    ProductResponse updateProduct(Long id, ProductRequest request);

    ProductResponse updateStock(Long id, Integer quantity);

    void deleteProduct(Long id);

    List<ProductResponse> searchProducts(String keyword,
                                         Long categoryId,
                                         Double minPrice,
                                         Double maxPrice);
}
