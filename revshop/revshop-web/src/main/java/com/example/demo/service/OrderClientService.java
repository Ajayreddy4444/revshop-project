package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.PlaceOrderRequest;



public interface OrderClientService {
	
	OrderResponse placeOrder(PlaceOrderRequest request);
	 List<OrderResponse> getOrderByUser(Long userId);
	 

	    // ✅ ADD THIS METHOD
	 boolean hasUserPurchasedProduct(Long userId, Long productId);

}
