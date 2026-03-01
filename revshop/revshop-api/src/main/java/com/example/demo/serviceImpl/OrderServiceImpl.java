package com.example.demo.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.OrderItemResponseDTO;
import com.example.demo.dto.OrderResponseDTO;
import com.example.demo.dto.PlaceOrderRequestDTO;
import com.example.demo.dto.SellerOrderResponseDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.OrderService;
import com.example.demo.service.NotificationService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final NotificationService notificationService;

    public OrderServiceImpl(UserRepository userRepository,
                            OrderRepository orderRepository,
                            ProductRepository productRepository,
                            NotificationService notificationService,
                            AddressRepository addressRepository,
                            CartRepository cartRepository,
                            CartItemRepository cartItemRepository,
                            OrderItemRepository orderItemRepository) {

        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.notificationService = notificationService;
        this.addressRepository = addressRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderItemRepository = orderItemRepository;
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

        // 5️⃣ Process Cart Items
        for (CartItem cartItem : cartItems) {

            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            int availableStock = product.getQuantity();
            int requestedQty = cartItem.getQuantity();

            if (availableStock < requestedQty) {
                throw new RuntimeException(
                        "Insufficient stock for product: " + product.getName());
            }

            double subtotal = product.getPrice() * requestedQty;

            // 🔥 Reduce stock (optional if needed)
            // product.setQuantity(availableStock - requestedQty);

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

        // 6️⃣ Save Order
        Order savedOrder = orderRepository.save(order);

        // 7️⃣ Notify Buyer
        notificationService.createOrderNotification(
                user,
                savedOrder.getId(),
                "Order successfully placed"
        );

        // 8️⃣ Notify Sellers
        for (OrderItem item : savedOrder.getItems()) {
            User seller = item.getProduct().getSeller();
            if (seller != null) {
                notificationService.createSellerOrderNotification(
                        seller,
                        savedOrder.getId(),
                        item.getProduct().getName()
                );
            }
        }

        // 9️⃣ Clear Cart
        cartItemRepository.deleteAll(cartItems);

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
                itemDTO.setProductId(item.getProduct().getId());
                itemDTO.setProductName(item.getProduct().getName());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPriceAtPurchase(item.getPriceAtPurchase());
                itemDTO.setSubtotal(item.getSubtotal());
                itemDTO.setImageUrl(item.getProduct().getImageUrl());
                itemDTOs.add(itemDTO);
            }
        }

        dto.setItems(itemDTOs);
        return dto;
    }

    @Override
    public boolean hasUserPurchasedProduct(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = orderRepository.findByUser(user);

        for (Order order : orders) {
            for (OrderItem item : order.getItems()) {
                if (item.getProduct().getId().equals(productId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<SellerOrderResponseDTO> getOrdersForSeller(Long sellerId) {
        List<OrderItem> orderItems =
                orderItemRepository.findByProduct_Seller_Id(sellerId);

        List<SellerOrderResponseDTO> response = new ArrayList<>();

        for (OrderItem item : orderItems) {
            SellerOrderResponseDTO dto = new SellerOrderResponseDTO();
            dto.setOrderId(item.getOrder().getId());
            dto.setOrderDate(item.getOrder().getOrderDate());
            dto.setStatus(item.getOrder().getStatus());
            dto.setImageUrl(item.getProduct().getImageUrl());
            dto.setProductName(item.getProduct().getName());
            dto.setQuantity(item.getQuantity());
            dto.setSubtotal(item.getSubtotal());
            dto.setBuyerName(item.getOrder().getUser().getName());
            dto.setBuyerEmail(item.getOrder().getUser().getEmail());
            response.add(dto);
        }

        return response;
    }
}