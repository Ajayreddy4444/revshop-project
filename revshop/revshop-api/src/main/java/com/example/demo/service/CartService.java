package com.example.demo.service;

import com.example.demo.dto.CartRequest;
import com.example.demo.dto.CartResponse;
import java.util.List;

public interface CartService {

    CartResponse addToCart(CartRequest request);

    List<CartResponse> getCartByUser(Long userId);

    void removeItem(Long itemId);
}