package com.example.demo.serviceImpl;

import com.example.demo.dto.WishlistResponse;
import com.example.demo.service.WishlistClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WishlistClientServiceImpl implements WishlistClientService {

    private final RestTemplate restTemplate;
    private final HttpServletRequest request;

    @Value("${backend.base-url}")
    private String backendBaseUrl;

    public WishlistClientServiceImpl(RestTemplate restTemplate,
                                     HttpServletRequest request) {
        this.restTemplate = restTemplate;
        this.request = request;
    }

    // 🔥 JWT HEADER
    private HttpHeaders getHeaders() {

        String token = (String) request.getSession().getAttribute("token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (token != null) {
            headers.setBearerAuth(token);
        }

        return headers;
    }

    // ================= TOGGLE =================
    @Override
    public boolean toggleWishlist(Long userId, Long productId) {

        HttpEntity<Void> entity =
                new HttpEntity<>(getHeaders());

        ResponseEntity<WishlistResponse> response =
                restTemplate.exchange(
                        backendBaseUrl + "/wishlist/toggle/{userId}/{productId}",
                        HttpMethod.POST,
                        entity,
                        WishlistResponse.class,
                        userId,
                        productId
                );

        return response.getBody() != null &&
               response.getBody().isAdded();
    }

    // ================= GET WISHLIST IDS =================
    @Override
    public List<Long> getWishlistProductIds(Long userId) {

        HttpEntity<Void> entity =
                new HttpEntity<>(getHeaders());

        ResponseEntity<List<Long>> response =
                restTemplate.exchange(
                        backendBaseUrl + "/wishlist/my/{userId}",
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<Long>>() {},
                        userId
                );

        return response.getBody();
    }

    // ================= REMOVE =================
    @Override
    public void removeFromWishlist(Long userId, Long productId) {

        HttpEntity<Void> entity =
                new HttpEntity<>(getHeaders());

        restTemplate.exchange(
                backendBaseUrl + "/wishlist/remove/{userId}/{productId}",
                HttpMethod.DELETE,
                entity,
                Void.class,
                userId,
                productId
        );
    }
}