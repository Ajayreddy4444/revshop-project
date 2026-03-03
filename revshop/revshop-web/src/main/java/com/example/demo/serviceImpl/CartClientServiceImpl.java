package com.example.demo.serviceImpl;

import com.example.demo.dto.CartRequest;
import com.example.demo.dto.CartResponse;
import com.example.demo.service.CartClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CartClientServiceImpl implements CartClientService {

    private final RestTemplate restTemplate;
    private final HttpServletRequest request;

    @Value("${backend.base-url}")
    private String baseUrl;

    public CartClientServiceImpl(RestTemplate restTemplate,
                                 HttpServletRequest request) {
        this.restTemplate = restTemplate;
        this.request = request;
    }

    private String cartUrl(){
        return baseUrl.replace("/auth","") + "/cart";
    }

    // 🔥 Helper to add Authorization header
    private HttpHeaders getHeaders() {

        String token = (String) request.getSession().getAttribute("token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (token != null) {
            headers.setBearerAuth(token);
        }

        return headers;
    }

    // ================== ADD TO CART ==================
    @Override
    public CartResponse add(CartRequest requestBody) {

        HttpEntity<CartRequest> entity =
                new HttpEntity<>(requestBody, getHeaders());

        ResponseEntity<CartResponse> response =
                restTemplate.exchange(
                        cartUrl() + "/add",
                        HttpMethod.POST,
                        entity,
                        CartResponse.class
                );

        return response.getBody();
    }

    // ================== GET CART ==================
    @Override
    public List<CartResponse> getCart(Long userId) {

        HttpEntity<Void> entity =
                new HttpEntity<>(getHeaders());

        ResponseEntity<CartResponse[]> response =
                restTemplate.exchange(
                        cartUrl() + "/" + userId,
                        HttpMethod.GET,
                        entity,
                        CartResponse[].class
                );

        return Arrays.asList(response.getBody());
    }

    // ================== REMOVE ==================
    @Override
    public void remove(Long itemId) {

        HttpEntity<Void> entity =
                new HttpEntity<>(getHeaders());

        restTemplate.exchange(
                cartUrl() + "/" + itemId,
                HttpMethod.DELETE,
                entity,
                Void.class
        );
    }
}