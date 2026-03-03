package com.example.demo.serviceImpl;

import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.PlaceOrderRequest;
import com.example.demo.dto.SellerOrderResponse;
import com.example.demo.service.OrderClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderClientServiceImpl implements OrderClientService {

    private final RestTemplate restTemplate;
    private final HttpServletRequest request;

    @Value("${backend.base-url}")
    private String baseUrl;

    public OrderClientServiceImpl(RestTemplate restTemplate,
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

    // ================= PLACE ORDER =================
    @Override
    public OrderResponse placeOrder(PlaceOrderRequest requestBody) {

        HttpEntity<PlaceOrderRequest> entity =
                new HttpEntity<>(requestBody, getHeaders());

        ResponseEntity<OrderResponse> response =
                restTemplate.exchange(
                        baseUrl + "/orders/place",
                        HttpMethod.POST,
                        entity,
                        OrderResponse.class
                );

        return response.getBody();
    }

    // ================= GET ORDERS BY USER =================
    @Override
    public List<OrderResponse> getOrderByUser(Long userId) {

        HttpEntity<Void> entity =
                new HttpEntity<>(getHeaders());

        ResponseEntity<List<OrderResponse>> response =
                restTemplate.exchange(
                        baseUrl + "/orders/user/" + userId,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<OrderResponse>>() {}
                );

        return response.getBody();
    }

    // ================= HAS PURCHASED =================
    @Override
    public boolean hasUserPurchasedProduct(Long userId, Long productId) {

        HttpEntity<Void> entity =
                new HttpEntity<>(getHeaders());

        ResponseEntity<Boolean> response =
                restTemplate.exchange(
                        baseUrl + "/orders/has-purchased/" + userId + "/" + productId,
                        HttpMethod.GET,
                        entity,
                        Boolean.class
                );

        return Boolean.TRUE.equals(response.getBody());
    }

    // ================= SELLER ORDERS =================
    @Override
    public List<SellerOrderResponse> getSellerOrders(Long sellerId) {

        HttpEntity<Void> entity =
                new HttpEntity<>(getHeaders());

        ResponseEntity<List<SellerOrderResponse>> response =
                restTemplate.exchange(
                        baseUrl + "/orders/seller/" + sellerId,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<SellerOrderResponse>>() {}
                );

        return response.getBody();
    }
}