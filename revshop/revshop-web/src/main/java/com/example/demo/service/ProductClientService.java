package com.example.demo.service;

import com.example.demo.dto.ProductDto;

import java.util.List;

public interface ProductClientService {

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);
}
