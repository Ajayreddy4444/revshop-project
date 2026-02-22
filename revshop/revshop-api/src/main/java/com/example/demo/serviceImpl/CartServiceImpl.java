package com.example.demo.serviceImpl;

import com.example.demo.dto.CartRequest;
import com.example.demo.dto.CartResponse;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    
    @Override
    public CartResponse addToCart(CartRequest request) {

        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUserId(request.getUserId());
                    return cartRepository.save(c);
                });

        CartItem item = new CartItem();
        item.setProductId(request.getProductId());
        item.setQuantity(request.getQuantity());
        item.setCart(cart);

        cartItemRepository.save(item);

        return new CartResponse(item.getId(), item.getProductId(), item.getQuantity());
    }

    
    @Override
    public List<CartResponse> getCartByUser(Long userId) {

        Cart cart = cartRepository.findByUserId(userId).orElse(null);

        
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            return List.of();
        }

        return cart.getItems()
                .stream()
                .map(i -> new CartResponse(i.getId(), i.getProductId(), i.getQuantity()))
                .collect(Collectors.toList());
    }

    @Override
    public void removeItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }
}