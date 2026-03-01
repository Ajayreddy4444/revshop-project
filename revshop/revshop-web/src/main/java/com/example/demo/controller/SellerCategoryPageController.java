package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.Category;
import com.example.demo.dto.SellerOrderResponse;
import com.example.demo.service.OrderClientService;
import com.example.demo.service.ProductClientService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/seller/categories")
public class SellerCategoryPageController {
    private final ProductClientService productClientService;
    private final OrderClientService orderClientService;

    public SellerCategoryPageController(ProductClientService productClientService,
    		                              OrderClientService orderClientService) {
        this.productClientService = productClientService;
        this.orderClientService=orderClientService;
    }

    @GetMapping("/new")
    public String showAddCategoryPage(HttpSession session) {
        if (session.getAttribute("user") == null)
            return "redirect:/login";
        return "seller-add-category";
    }


    @PostMapping("/add")
    public String addCategory(@RequestParam String name,
                              @RequestParam(required = false) String description,
                              RedirectAttributes redirectAttributes) {

        try {
            productClientService.createCategory(name, description);
            return "redirect:/seller/products/new";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    (e.getMessage() == null || e.getMessage().isBlank())
                            ? "Category already exists"
                            : e.getMessage()
            );
            return "redirect:/seller/categories/new";
        }
    }
    @GetMapping("/seller/orders")
    public String viewSellerOrders(HttpSession session, Model model) {

        AuthResponse seller =
                (AuthResponse) session.getAttribute("user");

        if (seller == null) return "redirect:/login";

        // ✅ CORRECT CALL
        List<SellerOrderResponse> orders =
                orderClientService.getSellerOrders(seller.getId());

        model.addAttribute("orders", orders);

        return "seller-orders";
    }
}
