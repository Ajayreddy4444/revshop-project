package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.CartResponse;
import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.PlaceOrderRequest;
import com.example.demo.service.CartClientService;
import com.example.demo.service.OrderClientService;
import com.example.demo.service.AddressClientService;

@Controller
@RequestMapping("/orders")
public class OrderpageController {

    private final CartClientService cartClientService;
    private final OrderClientService orderClientService;
    private final AddressClientService addressClientService;

    public OrderpageController(CartClientService cartClientService,
                               OrderClientService orderClientService,
                               AddressClientService addressClientService) {
        this.cartClientService = cartClientService;
        this.orderClientService = orderClientService;
        this.addressClientService = addressClientService;
    }

    private Long getLoggedInUserId(HttpSession session) {
        AuthResponse user = (AuthResponse) session.getAttribute("user");
        return user != null ? user.getId() : null;
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpSession session) {

        Long userId = getLoggedInUserId(session);
        if (userId == null) return "redirect:/login";

        List<CartResponse> cartItems = cartClientService.getCart(userId);

        if (cartItems == null || cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("addresses",
                addressClientService.getAddressesByUser(userId));
        model.addAttribute("userId", userId);

        return "checkout";
    }

    @PostMapping("/place")
    public String placeOrder(@RequestParam Long addressId,
                             HttpSession session,
                             Model model) {

        Long userId = getLoggedInUserId(session);
        if (userId == null) return "redirect:/login";

        PlaceOrderRequest request = new PlaceOrderRequest();
        request.setUserId(userId);
        request.setAddressId(addressId);

        try {

            OrderResponse response = orderClientService.placeOrder(request);

            return "redirect:/payment?orderId="
                    + response.getOrderId()
                    + "&amount="
                    + response.getTotalAmount();

        } catch (RuntimeException ex) {

            List<CartResponse> cartItems = cartClientService.getCart(userId);

            double totalAmount = cartItems.stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();

            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("addresses",
                    addressClientService.getAddressesByUser(userId));
            model.addAttribute("userId", userId);

            model.addAttribute("cancelError", ex.getMessage());

            return "checkout";
        }
    }

    @GetMapping("/my-orders")
    public String viewOrders(Model model, HttpSession session) {

        Long userId = getLoggedInUserId(session);
        if (userId == null) return "redirect:/login";

        List<OrderResponse> orders =
                orderClientService.getOrderByUser(userId);

        model.addAttribute("orders", orders);

        return "orders";
    }
}