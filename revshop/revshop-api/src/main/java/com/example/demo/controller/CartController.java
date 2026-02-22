package com.example.demo.controller;

import com.example.demo.dto.CartRequest;
import com.example.demo.dto.CartResponse;
import com.example.demo.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public CartResponse add(@RequestBody CartRequest request){
        return cartService.addToCart(request);
    }

    @GetMapping("/{userId}")
    public List<CartResponse> get(@PathVariable Long userId){
        return cartService.getCartByUser(userId);
    }

    @DeleteMapping("/{itemId}")
    public void remove(@PathVariable Long itemId){
        cartService.removeItem(itemId);
    }
}