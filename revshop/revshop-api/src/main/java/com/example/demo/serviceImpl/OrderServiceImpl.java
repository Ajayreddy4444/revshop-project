package com.example.demo.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.PlaceOrderRequestDTO;
import com.example.demo.dto.OrderItemResponseDTO;
import com.example.demo.dto.OrderResponseDTO;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(UserRepository userRepository,
                            CartRepository cartRepository,
                            OrderRepository orderRepository,
                            ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.productRepository=productRepository;
    }

    @Override
    @Transactional
    public OrderResponseDTO placeOrder(PlaceOrderRequestDTO request) {

       
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));
//        Cart cart = cartRepository.findByUserId(request.getUserId());
//        if (cart == null || cart.getItems().isEmpty()) {
//            throw new RuntimeException("Cart is empty");
//        }

      
        Order order = new Order();
        order.setUser(user);
        order.setStatus("PLACED");

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

       
        for (CartItem cartItem : cart.getItems()) {
        	
        	Product product = productRepository.findById(cartItem.getProductId())
        			.orElseThrow(()-> new RuntimeException("product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());

          
            double price = product.getPrice();
            orderItem.setPriceAtPurchase(price);

            double subtotal = price * cartItem.getQuantity();
            orderItem.setSubtotal(subtotal);

            totalAmount += subtotal;

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        
        Order savedOrder = orderRepository.save(order);

    
        cart.getItems().clear();
        cartRepository.save(cart);

        return convertToDTO(savedOrder);
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

        for (OrderItem item : order.getItems()) {
            OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();
            itemDTO.setProductName(item.getProduct().getName());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPriceAtPurchase(item.getPriceAtPurchase());
            itemDTO.setSubtotal(item.getSubtotal());

            itemDTOs.add(itemDTO);
        }

        dto.setItems(itemDTOs);

        return dto;
    }
}