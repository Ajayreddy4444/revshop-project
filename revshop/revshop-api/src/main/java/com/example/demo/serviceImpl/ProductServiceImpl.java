package com.example.demo.serviceImpl;

import com.example.demo.dto.ProductResponse;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponse> getAllProducts(){
        return productRepository.findAll()
                .stream()
                .map(this :: mapToDto)
                .toList();
    }

    @Override
    public ProductResponse getProductById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Product not found with id: " +id));

        return mapToDto(product);
    }

    private ProductResponse mapToDto(Product product){
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getMrp(),
                product.getQuantity(),
                product.getImageUrl()
        );
    }
}
