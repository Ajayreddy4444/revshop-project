package com.example.demo.serviceImpl;

import com.example.demo.dto.CartRequest;
import com.example.demo.dto.CartResponse;
import com.example.demo.service.CartClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CartClientServiceImpl implements CartClientService {

    private final RestTemplate restTemplate;

    @Value("${backend.base-url}")
    private String baseUrl;

    public CartClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String cartUrl(){
        // SAFE for your current project setup
        return baseUrl.replace("/auth","") + "/cart";
    }

    @Override
    public CartResponse add(CartRequest request) {

        HttpEntity<CartRequest> entity = new HttpEntity<>(request);

        return restTemplate.postForObject(
                cartUrl()+"/add",
                entity,
                CartResponse.class
        );
    }

    @Override
    public List<CartResponse> getCart(Long userId) {

        ResponseEntity<CartResponse[]> response =
                restTemplate.getForEntity(cartUrl()+"/"+userId, CartResponse[].class);

        return Arrays.asList(response.getBody());
    }

    @Override
    public void remove(Long itemId) {
        restTemplate.delete(cartUrl()+"/"+itemId);
    }
}