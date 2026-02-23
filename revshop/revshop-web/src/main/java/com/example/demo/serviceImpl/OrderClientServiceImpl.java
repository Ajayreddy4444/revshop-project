package com.example.demo.serviceImpl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value; 
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.PlaceOrderRequest;
import com.example.demo.service.OrderClientService;

@Service
public class OrderClientServiceImpl implements OrderClientService {

    private final RestTemplate restTemplate;

    @Value("${backend.base-url}")
    private String baseUrl;   

    public OrderClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public OrderResponse placeOrder(PlaceOrderRequest request) {

        String url = baseUrl + "/orders/place";

        return restTemplate.postForObject(
                url,
                request,
                OrderResponse.class
        );
    }

    @Override
    public List<OrderResponse> getOrderByUser(Long userId) {

        String url = baseUrl + "/orders/user/" + userId;

        OrderResponse[] response =
                restTemplate.getForObject(
                        url,
                        OrderResponse[].class
                );

        return Arrays.asList(response);
    }
}