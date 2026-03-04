package com.example.demo.serviceImplTest;

import com.example.demo.dto.CartRequest;
import com.example.demo.dto.CartResponse;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.serviceImpl.CartServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addToCart_WhenCartDoesNotExist_ShouldCreateNewCart() {

        CartRequest request = new CartRequest();
        request.setUserId(1L);
        request.setProductId(1L);
        request.setQuantity(2);

        when(cartRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        Cart newCart = new Cart();
        newCart.setUserId(1L);
        newCart.setItems(new ArrayList<>());

        when(cartRepository.save(any())).thenReturn(newCart);

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setMrp(120.0);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        CartResponse response = cartService.addToCart(request);

        assertNotNull(response);
        assertEquals(1L, response.getProductId());
        assertEquals(2, response.getQuantity());
    }

    @Test
    void getCartByUser_WhenCartNotFound_ShouldReturnEmptyList() {

        when(cartRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        assertTrue(cartService.getCartByUser(1L).isEmpty());
    }

    @Test
    void removeItem_ShouldCallRepositoryDelete() {

        cartService.removeItem(1L);

        verify(cartItemRepository, times(1)).deleteById(1L);
    }
}