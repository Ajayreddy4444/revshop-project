package com.example.demo.serviceImpl;

import com.example.demo.dto.Category;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductRequest;
import com.example.demo.service.ProductClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ProductClientServiceImpl implements ProductClientService {

    private final RestTemplate restTemplate;
    private final HttpServletRequest request;

    @Value("${backend.base-url}")
    private String baseUrl;

    public ProductClientServiceImpl(RestTemplate restTemplate,
                                    HttpServletRequest request) {
        this.restTemplate = restTemplate;
        this.request = request;
    }

    // ================= JWT HEADER =================
    private HttpHeaders getHeaders() {

        String token = (String) request.getSession().getAttribute("token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (token != null) {
            headers.setBearerAuth(token);
        }

        return headers;
    }

    // ================= GET ALL PRODUCTS =================
    @Override
    public List<ProductDto> getAllProducts() {

        HttpEntity<Void> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<List<ProductDto>> response =
                restTemplate.exchange(
                        baseUrl + "/products",
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<ProductDto>>() {}
                );

        return response.getBody();
    }

    // ================= GET PRODUCT BY ID =================
    @Override
    public ProductDto getProductById(Long id) {

        HttpEntity<Void> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ProductDto> response =
                restTemplate.exchange(
                        baseUrl + "/products/" + id,
                        HttpMethod.GET,
                        entity,
                        ProductDto.class
                );

        return response.getBody();
    }

    // ================= CREATE PRODUCT =================
    @Override
    public void createProduct(ProductRequest product, Long sellerId) {

        HttpEntity<ProductRequest> entity =
                new HttpEntity<>(product, getHeaders());

        restTemplate.exchange(
                baseUrl + "/products?sellerId=" + sellerId,
                HttpMethod.POST,
                entity,
                Void.class
        );
    }

    // ================= GET ALL CATEGORIES =================
    @Override
    public List<Category> getAllCategories() {

        HttpEntity<Void> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<Category[]> response =
                restTemplate.exchange(
                        baseUrl + "/categories",
                        HttpMethod.GET,
                        entity,
                        Category[].class
                );

        return Arrays.asList(response.getBody());
    }

    // ================= GET SELLER PRODUCTS =================
    @Override
    public List<ProductDto> getSellerProducts(Long sellerId) {

        HttpEntity<Void> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<List<ProductDto>> response =
                restTemplate.exchange(
                        baseUrl + "/seller/products/" + sellerId,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<ProductDto>>() {}
                );

        return response.getBody();
    }

    // ================= CREATE CATEGORY =================
    @Override
    public Category createCategory(String name, String description) {

        HttpEntity<Map<String, String>> entity =
                new HttpEntity<>(
                        Map.of("name", name, "description", description),
                        getHeaders()
                );

        try {
            ResponseEntity<Category> response =
                    restTemplate.exchange(
                            baseUrl + "/categories",
                            HttpMethod.POST,
                            entity,
                            Category.class
                    );

            return response.getBody();

        } catch (HttpClientErrorException ex) {
            throw new RuntimeException(ex.getResponseBodyAsString());
        }
    }

    // ================= UPDATE PRODUCT =================
    @Override
    public void updateProduct(Long id, ProductRequest product) {

        HttpEntity<ProductRequest> entity =
                new HttpEntity<>(product, getHeaders());

        restTemplate.exchange(
                baseUrl + "/products/" + id,
                HttpMethod.PUT,
                entity,
                Void.class
        );
    }

    // ================= UPDATE STOCK =================
    @Override
    public void updateStock(Long id, Integer quantity) {

        HttpEntity<Void> entity =
                new HttpEntity<>(getHeaders());

        restTemplate.exchange(
                baseUrl + "/products/" + id + "/stock?quantity=" + quantity,
                HttpMethod.PATCH,
                entity,
                Void.class
        );
    }

    // ================= DELETE PRODUCT =================
    @Override
    public void deleteProduct(Long id) {

        HttpEntity<Void> entity =
                new HttpEntity<>(getHeaders());

        restTemplate.exchange(
                baseUrl + "/products/" + id,
                HttpMethod.DELETE,
                entity,
                Void.class
        );
    }

    // ================= SEARCH PRODUCTS =================
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

        HttpEntity<Void> entity =
                new HttpEntity<>(getHeaders());

        ResponseEntity<ProductDto[]> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        ProductDto[].class
                );

        return Arrays.asList(response.getBody());
    }
}