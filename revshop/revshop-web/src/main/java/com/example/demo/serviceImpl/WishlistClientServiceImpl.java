package com.example.demo.serviceImpl;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.WishlistResponse;
import com.example.demo.service.WishlistClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WishlistClientServiceImpl implements WishlistClientService {

    private final RestTemplate restTemplate;

    @Value("${backend.base-url}")
    private String backendBaseUrl;

    public WishlistClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean toggleWishlist(Long userId, Long productId) {

        String url = backendBaseUrl +
                "/wishlist/toggle/{userId}/{productId}";

        WishlistResponse response =
                restTemplate.postForObject(
                        url,
                        null,
                        WishlistResponse.class,
                        userId,
                        productId
                );

        return response != null && response.isAdded();
    }

    @Override
    public List<Long> getWishlistProductIds(Long userId) {

        String url = backendBaseUrl + "/wishlist/my/{userId}";

        ResponseEntity<List<Long>> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Long>>() {
                        },
                        userId
                );

        return response.getBody();
    }

    @Override
    public void removeFromWishlist(Long userId, Long productId) {

        String url = backendBaseUrl +
                "/wishlist/remove/{userId}/{productId}";

        restTemplate.delete(url, userId, productId);
    }

}