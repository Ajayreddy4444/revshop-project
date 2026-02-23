package com.example.demo.serviceImpl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.PlaceOrderRequest;
import com.example.demo.service.OrderClientService;

@Service
public class OrderClientServiceImpl implements OrderClientService {
 
	private final RestTemplate restTemplate;
	
	
	private final String BASE_URL = "http://localhost:8080/api/orders";

    public OrderClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public OrderResponse placeOrder(PlaceOrderRequest request) {

        return restTemplate.postForObject(
                BASE_URL + "/place",
                request,
                OrderResponse.class
        );
    }

    @Override
    public List<OrderResponse> getOrderByUser(Long userId) {

        OrderResponse[] responseArray =
                restTemplate.getForObject(
                        BASE_URL + "/user/" + userId,
                        OrderResponse[].class
                );

        return Arrays.asList(responseArray);
    }
}
