package com.example.demo.serviceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.PlaceOrderRequest;
import com.example.demo.dto.SellerOrderResponse;
import com.example.demo.service.OrderClientService;

@Service
public class OrderClientServiceImpl implements OrderClientService {

    private final RestTemplate restTemplate;

    @Value("${backend.base-url}")
    private String baseUrl;

    public OrderClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // PLACE ORDER 
    @Override
    public OrderResponse placeOrder(PlaceOrderRequest request) {

        try {
            return restTemplate.postForObject(
                    baseUrl + "/orders/place",
                    request,
                    OrderResponse.class
            );

        } catch (HttpStatusCodeException ex) {

            String errorMessage = ex.getResponseBodyAsString();

            throw new RuntimeException(
                    errorMessage != null ? errorMessage : "Order API failed"
            );
        }
    }
    

    //  GET ORDERS BY USER 
    @Override
    public List<OrderResponse> getOrderByUser(Long userId) {

        try {
            String url = baseUrl + "/orders/user/" + userId;

            OrderResponse[] response =
                    restTemplate.getForObject(url, OrderResponse[].class);

            if (response == null) {
                return Collections.emptyList();
            }

            return Arrays.asList(response);

        } catch (RestClientException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    //  CHECK IF USER PURCHASED PRODUCT
    @Override
    public boolean hasUserPurchasedProduct(Long userId, Long productId) {

        try {
            String url = baseUrl + "/orders/has-purchased/"
                    + userId + "/" + productId;

            Boolean result =
                    restTemplate.getForObject(url, Boolean.class);

            return Boolean.TRUE.equals(result);

        } catch (RestClientException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    @Override
    public List<SellerOrderResponse> getSellerOrders(Long sellerId) {

        String url = baseUrl + "/orders/seller/" + sellerId;
    

        SellerOrderResponse[] response =
                restTemplate.getForObject(url, SellerOrderResponse[].class);

        if (response == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(response);
    }
}