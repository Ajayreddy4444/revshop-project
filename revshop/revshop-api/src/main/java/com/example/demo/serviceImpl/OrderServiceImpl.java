package com.example.demo.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.OrderItemResponseDTO;
import com.example.demo.dto.OrderResponseDTO;
import com.example.demo.dto.PlaceOrderRequestDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(UserRepository userRepository,
                            OrderRepository orderRepository,
                            AddressRepository addressRepository,
                            CartRepository cartRepository,
                            CartItemRepository cartItemRepository,
                            ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderResponseDTO placeOrder(PlaceOrderRequestDTO request) {

        // 1️⃣ Fetch User
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Fetch Address
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // 3️⃣ Fetch Cart
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cart.getItems();

        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 4️⃣ Create Order
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);

        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        // 5️⃣ Process Each Cart Item
        for (CartItem cartItem : cartItems) {

            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            int availableStock = product.getQuantity();
            int requestedQty = cartItem.getQuantity();

            // 🔒 Stock Validation
            if (availableStock < requestedQty) {
                throw new RuntimeException(
                        "Insufficient stock for product: " + product.getName());
            }

            double subtotal = product.getPrice() * requestedQty;

            // 🔥 Reduce Product Stock
            //product.setQuantity(availableStock - requestedQty);
            // No need to call save() — JPA auto flushes inside @Transactional

            // 🔥 Create Order Item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(requestedQty);
            orderItem.setPriceAtPurchase(product.getPrice());
            orderItem.setSubtotal(subtotal);

            totalAmount += subtotal;
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        // 6️⃣ Save Order (Cascade should save OrderItems)
        Order savedOrder = orderRepository.save(order);
//
//        // 7️⃣ Clear Cart
//        cartItemRepository.deleteAll(cartItems);

        return convertToDTO(savedOrder);
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return convertToDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getOrderByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = orderRepository.findByUser(user);

        List<OrderResponseDTO> responseList = new ArrayList<>();

        for (Order order : orders) {
            responseList.add(convertToDTO(order));
        }

        return responseList;
    }

    private OrderResponseDTO convertToDTO(Order order) {

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());

        List<OrderItemResponseDTO> itemDTOs = new ArrayList<>();

        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {

                OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();
                itemDTO.setProductId(item.getProduct().getId()); // ✅ CORRECT
                itemDTO.setProductName(item.getProduct().getName());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPriceAtPurchase(item.getPriceAtPurchase());
                itemDTO.setSubtotal(item.getSubtotal());

                itemDTOs.add(itemDTO);
            }
        }

        dto.setItems(itemDTOs);
        return dto;
    }
    
 // ============================================================
 // ✅ UPDATED: Check If User Purchased Specific Product
 // Used by review module to restrict reviews
 // ============================================================
 @Override
 public boolean hasUserPurchasedProduct(Long userId, Long productId) {

     User user = userRepository.findById(userId)
             .orElseThrow(() -> new RuntimeException("User not found"));

     // Get all orders of the user
     List<Order> orders = orderRepository.findByUser(user);

     // Check each order and its items
     for (Order order : orders) {

         for (OrderItem item : order.getItems()) {

             if (item.getProduct().getId().equals(productId)) {
                 return true; // Product found in any order
             }
         }
     }

     return false; // Product never purchased
 }
}
