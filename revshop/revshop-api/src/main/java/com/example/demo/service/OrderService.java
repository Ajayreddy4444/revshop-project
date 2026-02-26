package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.OrderResponseDTO;
import com.example.demo.dto.PlaceOrderRequestDTO;
import com.example.demo.entity.OrderStatus;

public interface OrderService {

	OrderResponseDTO placeOrder(PlaceOrderRequestDTO request);
	OrderResponseDTO getOrderById(Long orderId);
	List<OrderResponseDTO> getOrderByUser(Long userId);
	void updateOrderStatus(Long orderId, OrderStatus status);
	//updated
	boolean hasUserPurchasedProduct(Long userId, Long productId);

}