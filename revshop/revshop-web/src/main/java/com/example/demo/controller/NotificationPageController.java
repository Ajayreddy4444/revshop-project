package com.example.demo.controller;

import com.example.demo.dto.NotificationDto;
import com.example.demo.service.NotificationClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationPageController {

    @Autowired
    private NotificationClientService notificationService;

    @GetMapping
    public String viewNotifications(Model model, HttpSession session) {

        try {
            // Get userId from session (make sure you store it during login)
            Long userId = (Long) session.getAttribute("userId");

            if (userId == null) {
                model.addAttribute("notifications", List.of());
                return "notifications";
            }

            List<NotificationDto> notifications =
                    notificationService.getAllNotifications(userId);

            model.addAttribute("notifications", notifications);

        } catch (Exception e) {
            model.addAttribute("notifications", List.of());
        }

        return "notifications";
    }

    @PostMapping("/read/{id}")
    public String markAsRead(@PathVariable Long id,
                             HttpSession session) {

        try {
            notificationService.markAsRead(id);
        } catch (Exception ignored) {}

        return "redirect:/notifications";
    }
}