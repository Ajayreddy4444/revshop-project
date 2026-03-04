package com.example.demo.serviceImplTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.PlaceOrderRequestDTO;
import com.example.demo.dto.OrderResponseDTO;
import com.example.demo.entity.*;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.serviceImpl.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private AddressRepository addressRepository;
    @Mock private CartRepository cartRepository;
    @Mock private ProductRepository productRepository;
    @Mock private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    // SUCCESS CASE 

    @Test
    void placeOrder_success() {

        Long userId = 1L;
        Long addressId = 2L;
        Long productId = 10L;

        PlaceOrderRequestDTO request = new PlaceOrderRequestDTO();
        request.setUserId(userId);
        request.setAddressId(addressId);

        User user = new User();
        user.setId(userId);

        Address address = new Address();
     

        Product product = new Product();
     
        product.setName("Laptop");
        product.setPrice(50000.0);
        product.setQuantity(5);

        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        cartItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setItems(List.of(cartItem));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponseDTO response = orderService.placeOrder(request);

        assertNotNull(response);
        assertEquals(OrderStatus.CREATED, response.getStatus());
        assertEquals(100000.0, response.getTotalAmount());
        assertEquals(1, response.getItems().size());

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    //  USER NOT FOUND

    @Test
    void placeOrder_userNotFound() {

        PlaceOrderRequestDTO request = new PlaceOrderRequestDTO();
        request.setUserId(1L);
        request.setAddressId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(request));
    }

    //  CART EMPTY 

    @Test
    void placeOrder_cartEmpty() {

        Long userId = 1L;
        Long addressId = 2L;

        PlaceOrderRequestDTO request = new PlaceOrderRequestDTO();
        request.setUserId(userId);
        request.setAddressId(addressId);

        User user = new User();
        user.setId(userId);

        Address address = new Address();
       

        Cart cart = new Cart();
        cart.setItems(Collections.emptyList());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(request));
    }

    //INSUFFICIENT STOCK

    @Test
    void placeOrder_insufficientStock() {

        Long userId = 1L;
        Long addressId = 2L;
        Long productId = 10L;

        PlaceOrderRequestDTO request = new PlaceOrderRequestDTO();
        request.setUserId(userId);
        request.setAddressId(addressId);

        User user = new User();
        user.setId(userId);

        Address address = new Address();
     

        Product product = new Product();
       
        product.setName("Laptop");
        product.setPrice(50000.0);
        product.setQuantity(1); // available stock less than requested

        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        cartItem.setQuantity(2); // requested > available

        Cart cart = new Cart();
        cart.setItems(List.of(cartItem));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(request));
    }
    @Test
    void updateOrderStatus_success() {

        Long orderId = 1L;

        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        orderService.updateOrderStatus(orderId, OrderStatus.PAID);

        assertEquals(OrderStatus.PAID, order.getStatus());
    }
    @Test
    void updateOrderStatus_orderNotFound() {

        Long orderId = 1L;

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> orderService.updateOrderStatus(orderId, OrderStatus.PAID));

        verify(orderRepository, times(1)).findById(orderId);
    }
    @Test
    void getOrderById_success() {

        Long orderId = 1L;

        // Product
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(50000.0);
        product.setQuantity(5);

        // OrderItem
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
        orderItem.setPriceAtPurchase(50000.0);
        orderItem.setSubtotal(100000.0);

        // Order
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(100000.0);
        order.setItems(List.of(orderItem));

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        OrderResponseDTO response = orderService.getOrderById(orderId);

        assertNotNull(response);
        assertEquals(OrderStatus.CREATED, response.getStatus());
        assertEquals(100000.0, response.getTotalAmount());
        assertEquals(1, response.getItems().size());

        verify(orderRepository, times(1)).findById(orderId);
    }
}