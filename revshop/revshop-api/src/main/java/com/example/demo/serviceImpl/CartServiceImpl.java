package com.example.demo.serviceImpl;

import com.example.demo.dto.CartRequest;
import com.example.demo.dto.CartResponse;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public CartResponse addToCart(CartRequest request) {

        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUserId(request.getUserId());
                    c.setItems(new ArrayList<>());
                    return cartRepository.save(c);
                });

        
        CartItem existingItem = null;

        if (cart.getItems() != null) {
            for (CartItem i : cart.getItems()) {
                if (i.getProductId().equals(request.getProductId())) {
                    existingItem = i;
                    break;
                }
            }
        }

        if (existingItem != null) {

            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);

            Product product = productRepository.findById(existingItem.getProductId())
                    .orElse(null);

            return new CartResponse(
                    existingItem.getId(),
                    existingItem.getProductId(),
                    product != null ? product.getName() : "Unknown",
                    existingItem.getQuantity()
            );
        }

   
        CartItem item = new CartItem();
        item.setProductId(request.getProductId());
        item.setQuantity(request.getQuantity());
        item.setCart(cart);

        cartItemRepository.save(item);

        Product product = productRepository.findById(item.getProductId())
                .orElse(null);

        return new CartResponse(
                item.getId(),
                item.getProductId(),
                product != null ? product.getName() : "Unknown",
                item.getQuantity()
        );
    }

    @Override
    public List<CartResponse> getCartByUser(Long userId) {

        Cart cart = cartRepository.findByUserId(userId).orElse(null);

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            return new ArrayList<>();
        }

        return cart.getItems()
                .stream()
                .map(i -> {
                    Product product = productRepository.findById(i.getProductId())
                            .orElse(null);

                    return new CartResponse(
                            i.getId(),
                            i.getProductId(),
                            product != null ? product.getName() : "Unknown",
                            i.getQuantity()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public void removeItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }
}