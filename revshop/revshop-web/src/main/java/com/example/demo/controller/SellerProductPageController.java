package com.example.demo.controller;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductRequest;
import com.example.demo.service.ProductClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/seller")
public class SellerProductPageController {

    private final ProductClientService productClientService;

    public SellerProductPageController(ProductClientService productClientService) {
        this.productClientService = productClientService;
    }

    @GetMapping("/products/new")
    public String showCreateForm(Model model, HttpSession session) {
        if (session.getAttribute("user") == null)
            return "redirect:/login";
        model.addAttribute("product", new ProductDto());
        model.addAttribute("categories", productClientService.getAllCategories());

        return "seller-create-product";
    }

    @PostMapping("/products")
    public String createProduct(@ModelAttribute ProductDto product,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        var user = (com.example.demo.dto.AuthResponse) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        if (product.getQuantity() == null || product.getQuantity() < 0) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Quantity cannot be negative");
            return "redirect:/seller/products/new";
        }

        try {
            if (product.getImageFile() != null && !product.getImageFile().isEmpty()) {

                String originalName = product.getImageFile().getOriginalFilename();
                String fileName = System.currentTimeMillis() + "_" + originalName;

                Path uploadPath = Paths.get("uploads/images");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(product.getImageFile().getInputStream(),
                        uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);

                product.setImageUrl(fileName);
            }

            System.out.println("Before the create product api call");

            ProductRequest req = new ProductRequest();
            req.setName(product.getName());
            req.setDescription(product.getDescription());
            req.setMrp(product.getMrp());
            req.setDiscountPercent(product.getDiscountPercent());
            req.setQuantity(product.getQuantity());
            req.setLowStockThreshold(product.getLowStockThreshold());
            req.setImageUrl(product.getImageUrl());
            req.setCategoryId(product.getCategoryId());

            productClientService.createProduct(req, user.getId());

            System.out.println("after the create product api call");

            redirectAttributes.addFlashAttribute("successMessage",
                    "Product added successfully");

        } catch (Exception e) {

            String msg = e.getMessage();
            if (msg == null || msg.contains("500")) {
                msg = "You already added this product in this category";
            }
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/seller/products/new";
        }

        return "redirect:/seller/products";
    }

    @GetMapping("/products")
    public String sellerProducts(HttpSession session, Model model) {
        if (session.getAttribute("user") == null)
            return "redirect:/login";
        var user = (com.example.demo.dto.AuthResponse) session.getAttribute("user");
        model.addAttribute("products", productClientService.getSellerProducts(user.getId()));
        return "seller-products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {

        ProductDto product = productClientService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", productClientService.getAllCategories());

        return "seller-create-product";
    }

    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute ProductDto product,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        var user = (com.example.demo.dto.AuthResponse) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        if (product.getQuantity() != null && product.getQuantity() < 0) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Quantity cannot be negative");
            return "redirect:/seller/products/edit/" + id;
        }

        try {
            if (product.getImageFile() != null && !product.getImageFile().isEmpty()) {

                String originalName = product.getImageFile().getOriginalFilename();
                String fileName = System.currentTimeMillis() + "_" + originalName;

                Path uploadPath = Paths.get("uploads/images");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

                Files.copy(product.getImageFile().getInputStream(),
                        uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);

                product.setImageUrl(fileName);
            }

            ProductRequest req = new ProductRequest();
            req.setName(product.getName());
            req.setDescription(product.getDescription());
            req.setDiscountPercent(product.getDiscountPercent());
            req.setMrp(product.getMrp());
            req.setQuantity(product.getQuantity());
            req.setLowStockThreshold(product.getLowStockThreshold());
            req.setImageUrl(product.getImageUrl());
            req.setCategoryId(product.getCategoryId());

            productClientService.updateProduct(id, req);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Product updated successfully");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Update failed");
            return "redirect:/seller/products/edit/" + id;
        }

        return "redirect:/seller/products";
    }

    @PostMapping("/products/stock/{id}")
    public String updateStock(@PathVariable Long id,
                              @RequestParam Integer quantity,
                              RedirectAttributes redirectAttributes) {

        if (quantity == null || quantity < 0) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Quantity cannot be negative");
            return "redirect:/seller/products";
        }

        productClientService.updateStock(id, quantity);

        redirectAttributes.addFlashAttribute("successMessage",
                "Stock updated successfully");

        return "redirect:/seller/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {

        productClientService.deleteProduct(id);
        return "redirect:/seller/products";
    }

    @GetMapping("/products/{id}")
    public String viewProductDetails(@PathVariable Long id,
                                     HttpSession session,
                                     Model model) {

        if (session.getAttribute("user") == null)
            return "redirect:/login";

        ProductDto product = productClientService.getProductById(id);

        model.addAttribute("product", product);

        return "seller-product-details";
    }
}
