package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.PlaceOrderRequestDTO;
import com.example.demo.dto.OrderResponseDTO;
import com.example.demo.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 🔥 Place Order
    @PostMapping("/place")
    public ResponseEntity<OrderResponseDTO> placeOrder(
            @RequestBody PlaceOrderRequestDTO request) {

        OrderResponseDTO response = orderService.placeOrder(request);
        return ResponseEntity.ok(response);
    }

    // 🔥 Get Order By ID
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable Long orderId) {

        OrderResponseDTO response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    // 🔥 Get Orders By User
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUser(
            @PathVariable Long userId) {

        List<OrderResponseDTO> response =
                orderService.getOrderByUser(userId);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // ✅ UPDATED: Check If User Purchased Specific Product
    // Used by review module in revshop-web
    // Endpoint: /api/orders/has-purchased/{userId}/{productId}
    // =========================================================
    @GetMapping("/has-purchased/{userId}/{productId}")
    public ResponseEntity<Boolean> hasUserPurchased(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        boolean result =
                orderService.hasUserPurchasedProduct(userId, productId);

        return ResponseEntity.ok(result);
    }
}