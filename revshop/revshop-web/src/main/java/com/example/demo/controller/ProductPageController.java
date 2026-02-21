package com.example.demo.controller;

import com.example.demo.service.ProductClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductPageController {

    private final ProductClientService productClientService;

    public ProductPageController(ProductClientService productClientService){
        this.productClientService = productClientService;
    }

    @GetMapping
    public String productList(HttpSession session, Model model){
        if(session.getAttribute("user") == null)
            return "redirect:/login";
        model.addAttribute("products", productClientService.getAllProducts());
        return "products";
    }

    @GetMapping("/{id}")
    public String productDetails(@PathVariable Long id,
                                 HttpSession session,
                                 Model model){
        if(session.getAttribute("user") == null)
            return "redirect:/login";
        model.addAttribute("product", productClientService.getProductById(id));
        return "product-details";
    }
}
