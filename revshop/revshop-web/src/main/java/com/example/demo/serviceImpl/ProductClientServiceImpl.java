package com.example.demo.serviceImpl;

import com.example.demo.dto.Category;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductRequest;
import com.example.demo.service.ProductClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductClientServiceImpl implements ProductClientService {
    private final RestTemplate restTemplate;

    @Value("${backend.base-url}")
    private String baseUrl;

    public ProductClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        ResponseEntity<List<ProductDto>> response =
                restTemplate.exchange(
                        baseUrl + "/products",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<ProductDto>>() {
                        }
                );
        return response.getBody();
    }

    @Override
    public ProductDto getProductById(Long id) {
        return restTemplate.getForObject(baseUrl + "/products/" + id, ProductDto.class);
    }

    @Override
    public void createProduct(ProductRequest product, Long sellerId) {
        restTemplate.postForEntity(
                baseUrl + "/products?sellerId=" + sellerId,
                product,
                Void.class
        );
    }

    @Override
    public List<Category> getAllCategories() {
        ResponseEntity<Category[]> response =
                restTemplate.getForEntity(baseUrl + "/categories", Category[].class);
        return Arrays.asList(response.getBody());
    }

    @Override
    public List<ProductDto> getSellerProducts(Long sellerId) {
        ResponseEntity<List<ProductDto>> response =
                restTemplate.exchange(
                        baseUrl + "/seller/products/" + sellerId,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<ProductDto>>() {
                        }
                );
        return response.getBody();
    }

    @Override
    public Category createCategory(String name, String description) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(
                Map.of("name", name, "description", description), headers);

        try {
            ResponseEntity<Category> response =
                    restTemplate.postForEntity(baseUrl + "/categories", request, Category.class);

            return response.getBody();
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException(ex.getResponseBodyAsString());
        }
    }

    @Override
    public void updateProduct(Long id, ProductRequest product) {
        restTemplate.put(baseUrl + "/products/" + id, product);
    }

    @Override
    public void updateStock(Long id, Integer quantity) {

        restTemplate.patchForObject(
                baseUrl + "/products/" + id + "/stock?quantity=" + quantity,
                null,
                Void.class
        );
    }

    @Override
    public void deleteProduct(Long id) {
        restTemplate.delete(baseUrl + "/products/" + id);
    }

    @Override
    public List<ProductDto> searchProducts(String keyword,
                                           Long categoryId,
                                           Double minPrice,
                                           Double maxPrice) {

        String url = baseUrl + "/products/search?"
                + "keyword=" + (keyword != null ? keyword : "")
                + "&categoryId=" + (categoryId != null ? categoryId : "")
                + "&minPrice=" + (minPrice != null ? minPrice : "")
                + "&maxPrice=" + (maxPrice != null ? maxPrice : "");

        ResponseEntity<ProductDto[]> response =
                restTemplate.getForEntity(url, ProductDto[].class);

        return Arrays.asList(response.getBody());
    }
}
