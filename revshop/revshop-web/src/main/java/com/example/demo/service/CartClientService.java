package com.example.demo.service;

import com.example.demo.dto.CartRequest;
import com.example.demo.dto.CartResponse;
import java.util.List;

public interface CartClientService {

    CartResponse add(CartRequest request);

    List<CartResponse> getCart(Long userId);

    void remove(Long itemId);
}