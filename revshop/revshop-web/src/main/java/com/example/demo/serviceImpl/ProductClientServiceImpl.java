package com.example.demo.serviceImpl;

import com.example.demo.dto.ProductDto;
import com.example.demo.service.ProductClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProductClientServiceImpl implements ProductClientService {
    private final RestTemplate restTemplate;

    @Value("${backend.product-url}")
    private String productUrl;

    public ProductClientServiceImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        ResponseEntity<List<ProductDto>> response =
                restTemplate.exchange(
                        productUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<ProductDto>>() {}
                );
        return response.getBody();
    }

    @Override
    public ProductDto getProductById(Long id) {
        return restTemplate.getForObject(productUrl + "/" + id, ProductDto.class);
    }
}
