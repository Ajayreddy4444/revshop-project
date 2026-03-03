package com.example.demo.serviceImplTest;

import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.PlaceOrderRequest;
import com.example.demo.dto.SellerOrderResponse;
import com.example.demo.serviceImpl.OrderClientServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderClientServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderClientServiceImpl orderClientService;

    private final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(orderClientService, "baseUrl", BASE_URL);
    }

    // =========================
    // PLACE ORDER
    // =========================

    @Test
    void placeOrder_success() {

        PlaceOrderRequest request = new PlaceOrderRequest();
        OrderResponse response = new OrderResponse();

        when(restTemplate.postForObject(
                BASE_URL + "/orders/place",
                request,
                OrderResponse.class))
                .thenReturn(response);

        OrderResponse result = orderClientService.placeOrder(request);

        assertNotNull(result);

        verify(restTemplate).postForObject(
                BASE_URL + "/orders/place",
                request,
                OrderResponse.class);
    }

    @Test
    void placeOrder_shouldThrowRuntimeException_onRestError() {

        PlaceOrderRequest request = new PlaceOrderRequest();

        when(restTemplate.postForObject(
                BASE_URL + "/orders/place",
                request,
                OrderResponse.class))
                .thenThrow(RestClientException.class);

        assertThrows(RuntimeException.class,
                () -> orderClientService.placeOrder(request));
    }

    // =========================
    // GET ORDER BY USER
    // =========================

    @Test
    void getOrderByUser_success() {

        OrderResponse[] responseArray = {
                new OrderResponse(),
                new OrderResponse()
        };

        when(restTemplate.getForObject(
                BASE_URL + "/orders/user/1",
                OrderResponse[].class))
                .thenReturn(responseArray);

        List<OrderResponse> result =
                orderClientService.getOrderByUser(1L);

        assertEquals(2, result.size());
    }

    @Test
    void getOrderByUser_shouldReturnEmptyList_whenNullResponse() {

        when(restTemplate.getForObject(
                BASE_URL + "/orders/user/1",
                OrderResponse[].class))
                .thenReturn(null);

        List<OrderResponse> result =
                orderClientService.getOrderByUser(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getOrderByUser_shouldReturnEmptyList_onException() {

        when(restTemplate.getForObject(
                BASE_URL + "/orders/user/1",
                OrderResponse[].class))
                .thenThrow(RestClientException.class);

        List<OrderResponse> result =
                orderClientService.getOrderByUser(1L);

        assertTrue(result.isEmpty());
    }

    // =========================
    // GET SELLER ORDERS
    // =========================

    @Test
    void getSellerOrders_success() {

        SellerOrderResponse[] responseArray = {
                new SellerOrderResponse(),
                new SellerOrderResponse()
        };

        when(restTemplate.getForObject(
                BASE_URL + "/orders/seller/5",
                SellerOrderResponse[].class))
                .thenReturn(responseArray);

        List<SellerOrderResponse> result =
                orderClientService.getSellerOrders(5L);

        assertEquals(2, result.size());
    }

    @Test
    void getSellerOrders_shouldReturnEmptyList_whenNull() {

        when(restTemplate.getForObject(
                BASE_URL + "/orders/seller/5",
                SellerOrderResponse[].class))
                .thenReturn(null);

        List<SellerOrderResponse> result =
                orderClientService.getSellerOrders(5L);

        assertTrue(result.isEmpty());
    }
}